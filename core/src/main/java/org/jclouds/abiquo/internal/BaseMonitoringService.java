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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.config.SchedulerModule;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.abiquo.functions.monitor.VirtualMachineDeployMonitor;
import org.jclouds.abiquo.functions.monitor.VirtualMachineUndeployMonitor;
import org.jclouds.abiquo.monitor.MonitorStatus;
import org.jclouds.abiquo.monitor.events.CompletedEvent;
import org.jclouds.abiquo.monitor.events.FailedEvent;
import org.jclouds.abiquo.monitor.events.TimeoutEvent;
import org.jclouds.abiquo.monitor.events.handlers.BlockingEventHandler;
import org.jclouds.logging.Logger;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;

/**
 * Utility service to monitor asynchronous operations.
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

    /** The scheduler used to perform monitoring tasks. */
    @VisibleForTesting
    protected ScheduledExecutorService scheduler;

    @VisibleForTesting
    protected Long pollingDelay;

    /** The event bus used to dispatch monitoring events. */
    @VisibleForTesting
    protected AsyncEventBus eventBus;

    @VisibleForTesting
    protected VirtualMachineDeployMonitor deployMonitor;

    @VisibleForTesting
    protected VirtualMachineUndeployMonitor undeployMonitor;

    @Resource
    private Logger logger = Logger.NULL;

    /**
     * A map containing all running monitors indexed by the monitored object.
     * <p>
     * This map will be used to cancel concrete monitors when they are completed.
     */
    @VisibleForTesting
    protected Map<Object, RunningMonitor< ? >> runningMonitors;

    @Inject
    public BaseMonitoringService(final AbiquoContext context,
        final ScheduledExecutorService scheduler,
        @Named(ASYNC_TASK_MONITOR_DELAY) final Long pollingDelay, final AsyncEventBus eventBus,
        final VirtualMachineDeployMonitor deployMonitor,
        final VirtualMachineUndeployMonitor undeployMonitor)
    {
        this.context = checkNotNull(context, "context");
        this.scheduler = checkNotNull(scheduler, "scheduler");
        this.pollingDelay = checkNotNull(pollingDelay, "pollingDelay");
        this.eventBus = checkNotNull(eventBus, "eventBus");
        this.deployMonitor = checkNotNull(deployMonitor, "deployMonitor");
        this.undeployMonitor = checkNotNull(undeployMonitor, "undeployMonitor");
        this.runningMonitors =
            Collections.synchronizedMap(new HashMap<Object, RunningMonitor< ? >>());
    }

    /*************** Virtual machine ***************/

    @Override
    public void awaitCompletionDeploy(final VirtualMachine... vms)
    {
        awaitCompletion(deployMonitor, vms);
    }

    @Override
    public void monitorDeploy(final VirtualMachine... vms)
    {
        monitor(deployMonitor, vms);
    }

    @Override
    public void awaitCompletionDeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... vms)
    {
        awaitCompletion(maxWait, timeUnit, deployMonitor, vms);
    }

    @Override
    public void monitorDeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... vms)
    {
        monitor(maxWait, timeUnit, deployMonitor, vms);
    }

    @Override
    public void awaitCompletionUndeploy(final VirtualMachine... vms)
    {
        awaitCompletion(undeployMonitor, vms);
    }

    @Override
    public void monitorUndeploy(final VirtualMachine... vms)
    {
        monitor(undeployMonitor, vms);
    }

    @Override
    public void awaitCompletionUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... vms)
    {
        awaitCompletion(maxWait, timeUnit, undeployMonitor, vms);
    }

    @Override
    public void monitorUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... vms)
    {
        monitor(maxWait, timeUnit, undeployMonitor, vms);
    }

    /*************** Generic methods ***************/

    @Override
    public <T> void awaitCompletion(final Function<T, MonitorStatus> completeCondition,
        final T... objects)
    {
        awaitCompletion(null, null, completeCondition, objects);
    }

    @Override
    public <T> void awaitCompletion(final Long maxWait, final TimeUnit timeUnit,
        final Function<T, MonitorStatus> completeCondition, final T... objects)
    {
        checkNotNull(completeCondition, "completeCondition");

        if (objects != null && objects.length > 0)
        {
            BlockingEventHandler<T> blockingHandler = new BlockingEventHandler<T>(logger, objects);
            eventBus.register(blockingHandler);

            monitor(maxWait, timeUnit, completeCondition, objects);
            blockingHandler.lock();

            eventBus.unregister(blockingHandler);
        }
    }

    @Override
    public <T> void monitor(final Function<T, MonitorStatus> completeCondition, final T... objects)
    {
        monitor(null, null, completeCondition, objects);
    }

    @Override
    public <T> void monitor(final Long maxWait, final TimeUnit timeUnit,
        final Function<T, MonitorStatus> completeCondition, final T... objects)
    {
        checkNotNull(completeCondition, "completeCondition");
        if (maxWait != null)
        {
            checkNotNull(timeUnit, "timeUnit");
        }

        if (objects != null && objects.length > 0)
        {
            for (T object : objects)
            {
                // We force a delayed start to ensure events will not get too much fast. It is
                // important when using the BlockingEventHandler, to prevent event notifications
                // arrive before the thread lock has been obtained
                ScheduledFuture< ? > future =
                    scheduler.scheduleWithFixedDelay(
                        new AsyncMonitor<T>(object, completeCondition), 500L, pollingDelay,
                        TimeUnit.MILLISECONDS);

                Long timeout =
                    maxWait == null ? null : System.currentTimeMillis()
                        + timeUnit.toMillis(maxWait);

                // Store the monitor so we can cancel the job when the task completes
                runningMonitors.put(object, new RunningMonitor<T>(future, timeout));
            }
        }
    }

    private class AsyncMonitor<T> implements Runnable
    {
        private T monitoredObject;

        private Function<T, MonitorStatus> completeCondition;

        public AsyncMonitor(final T monitoredObject,
            final Function<T, MonitorStatus> completeCondition)
        {
            super();
            this.monitoredObject = monitoredObject;
            this.completeCondition = completeCondition;
        }

        private void stopMonitoring(final T monitoredObject)
        {
            Future< ? > future = runningMonitors.get(monitoredObject).future;

            try
            {
                if (!future.isCancelled() && !future.isDone())
                {
                    // Do not force future cancel. Let it finish gracefully
                    future.cancel(false);
                }

                // Remove it from the map only if the future has been cancelled. If cancel
                // fails, next run will remove it.
                runningMonitors.remove(monitoredObject);
            }
            catch (Exception ex)
            {
                logger.warn(ex, "failed to stop monitor job for %s", monitoredObject);
            }
        }

        private boolean isTimeout(final T monitoredObject)
        {
            Long timeout = runningMonitors.get(monitoredObject).timeout;
            return timeout != null && timeout < System.currentTimeMillis();
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
                stopMonitoring(monitoredObject);
                return;
            }

            MonitorStatus status = completeCondition.apply(monitoredObject);
            logger.debug("monitored object %s status %s", monitoredObject, status.name());

            switch (status)
            {
                case DONE:
                    stopMonitoring(monitoredObject);
                    eventBus.post(new CompletedEvent<T>(monitoredObject));
                    break;
                case FAILED:
                    stopMonitoring(monitoredObject);
                    eventBus.post(new FailedEvent<T>(monitoredObject));
                    break;
                case CONTINUE:
                default:
                    if (isTimeout(monitoredObject))
                    {
                        logger.warn("monitor for object %s timed out. Shutting down monitor.",
                            monitoredObject);
                        stopMonitoring(monitoredObject);
                        eventBus.post(new TimeoutEvent<T>(monitoredObject));
                    }
                    break;
            }
        }
    }

    /**
     * Running monitor.
     * 
     * @author Ignasi Barrera
     * @param <T> The monitored object.
     */
    private static class RunningMonitor<T>
    {
        /** The timeout for the monitor. */
        Long timeout;

        /** The asynchronous job. */
        Future< ? > future;

        public RunningMonitor(final Future< ? > future, final Long timeout)
        {
            super();
            this.future = checkNotNull(future, "future");
            this.timeout = timeout; // Timeout can be null
        }
    }

}
