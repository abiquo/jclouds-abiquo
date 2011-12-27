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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.abiquo.reference.AbiquoConstants.ASYNC_TASK_MONITOR_DELAY;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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

    /**
     * A map containing all running monitors indexed by task id.
     * <p>
     * This map will be used to cancel concrete tasks when they are completed.
     */
    @VisibleForTesting
    protected Map<String, ScheduledFuture< ? >> runningMonitors;

    @Inject
    public BaseMonitoringService(final AbiquoContext context,
        final ScheduledExecutorService scheduler,
        @Named(ASYNC_TASK_MONITOR_DELAY) final Long pollingDelay)
    {
        this.context = checkNotNull(context, "context");
        this.scheduler = checkNotNull(scheduler, "scheduler");
        this.pollingDelay = checkNotNull(pollingDelay, "pollingDelay");
        this.runningMonitors = new HashMap<String, ScheduledFuture< ? >>();
    }

    @Override
    public void awaitCompletion(final AsyncTask... tasks)
    {
        if (tasks != null && tasks.length > 0)
        {
            BlockingCallback monitorLock = new BlockingCallback(tasks.length);
            monitor(monitorLock, tasks);
            monitorLock.lock();
        }
    }

    @Override
    public void monitor(final AsyncTaskCallback callback, final AsyncTask... tasks)
    {
        checkNotNull(callback, "callback");

        if (tasks != null && tasks.length > 0)
        {
            for (AsyncTask task : tasks)
            {
                ScheduledFuture< ? > future =
                    scheduler.scheduleWithFixedDelay(new AsyncTaskMonitor(task, callback), 0,
                        pollingDelay, TimeUnit.MILLISECONDS);
                // Store the future in the monitors map so we can cancel it when the task completes
                runningMonitors.put(task.getTaskId(), future);
            }
        }
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

        private void stopMonitoring(AsyncTask task)
        {
            ScheduledFuture< ? > future = runningMonitors.get(task.getTaskId());
            if (!future.isCancelled() && !future.isDone())
            {
                // Do not force future cancel. Let it finish gracefully
                future.cancel(false);
            }
            runningMonitors.remove(task.getTaskId());
        }

        @Override
        public void run()
        {
            // Do not use Thread.interrupted() since it will clear the interrupted flag
            // and subsequent calls to it may not return the appropriate value
            if (Thread.currentThread().isInterrupted())
            {
                // If the thread as already been interrupted, just stop monitoring the task and
                // return
                stopMonitoring(task);
                return;
            }

            task.refresh();

            switch (task.getState())
            {
                case FINISHED_SUCCESSFULLY:
                    callback.onTaskCompleted(task);
                    stopMonitoring(task);
                    break;
                case FINISHED_UNSUCCESSFULLY:
                    callback.onTaskFailed(task);
                    stopMonitoring(task);
                    break;
                default:
                    // Task has not finished, continue monitoring it
                    break;
            }
        }
    }

    public static class BlockingCallback implements AsyncTaskCallback
    {
        private CountDownLatch completeSignal;

        public BlockingCallback(int countDownToCompletion)
        {
            super();
            checkArgument(countDownToCompletion > 0, "countDownToCompletion must be greater than 0");
            completeSignal = new CountDownLatch(countDownToCompletion);
        }

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
