/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclouds.abiquo.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.abiquo.reference.AbiquoConstants.ASYNC_TASK_MONITOR_DELAY;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.config.SchedulerModule;
import org.jclouds.abiquo.domain.task.AsyncJob;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.domain.task.AsyncTaskCallback;
import org.jclouds.abiquo.features.services.MonitoringService;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.inject.Inject;

/**
 * Utility service to monitor {@link AsyncTask} and {@link AsyncJob} progress.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see SchedulerModule
 */
@Singleton
public class BaseMonitoringService implements MonitoringService
{
    @VisibleForTesting
    protected AbiquoContext context;

    @VisibleForTesting
    protected ScheduledExecutorService scheduler;

    @VisibleForTesting
    protected Long pollingDelay;

    @Inject
    public BaseMonitoringService(final AbiquoContext context,
        final ScheduledExecutorService scheduler,
        @Named(ASYNC_TASK_MONITOR_DELAY) final Long pollingDelay)
    {
        this.context = checkNotNull(context, "context");
        this.scheduler = checkNotNull(scheduler, "scheduler");
        this.pollingDelay = checkNotNull(pollingDelay, "pollingDelay");
    }

    @Override
    public void awaitCompletion(final AsyncTask task)
    {
        BlockingCallback monitorLock = new BlockingCallback();
        monitor(task, monitorLock);
        monitorLock.lock();
    }

    @Override
    public void monitor(final AsyncTask task, final AsyncTaskCallback callback)
    {
        scheduler.scheduleWithFixedDelay(new AsyncTaskMonitor(task, callback), 0, pollingDelay,
            TimeUnit.MILLISECONDS);
    }

    private class AsyncTaskMonitor implements Runnable
    {
        private AsyncTask task;

        private AsyncTaskCallback callback;

        public AsyncTaskMonitor(final AsyncTask task, final AsyncTaskCallback callback)
        {
            super();
            this.task = task;
            this.callback = callback;
        }

        @Override
        public void run()
        {
            task.refresh();

            switch (task.getState())
            {
                case FINISHED_SUCCESSFULLY:
                    callback.onTaskCompleted(task);
                    scheduler.shutdown(); // Stop monitoring the task
                    break;
                case FINISHED_UNSUCCESSFULLY:
                    callback.onTaskFailed(task);
                    scheduler.shutdown(); // Stop monitoring the task
                    break;
                default:
                    // Task has not finished, continue monitoring it
                    break;
            }
        }
    }

    public static class BlockingCallback implements AsyncTaskCallback
    {
        private CountDownLatch completeSignal = new CountDownLatch(1);

        public void lock()
        {
            try
            {
                completeSignal.await();
            }
            catch (InterruptedException ex)
            {
                Throwables.propagate(ex);
            }
        }

        public void release()
        {
            completeSignal.countDown();
        }

        @Override
        public void onTaskCompleted(final AsyncTask task)
        {
            release();
        }

        @Override
        public void onTaskFailed(final AsyncTask task)
        {
            release();
        }
    }

}
