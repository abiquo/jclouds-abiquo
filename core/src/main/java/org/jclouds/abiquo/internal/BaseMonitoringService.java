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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.config.SchedulerModule;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.monitor.MonitorCallback;
import org.jclouds.abiquo.domain.monitor.MonitorStatus;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.abiquo.functions.monitor.VirtualApplianceDeployMonitor;
import org.jclouds.abiquo.functions.monitor.VirtualApplianceUndeployMonitor;
import org.jclouds.abiquo.functions.monitor.VirtualMachineDeployMonitor;
import org.jclouds.abiquo.functions.monitor.VirtualMachineUndeployMonitor;
import org.jclouds.logging.Logger;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
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

    @VisibleForTesting
    protected ScheduledExecutorService scheduler;

    @VisibleForTesting
    protected Long pollingDelay;

    @VisibleForTesting
    protected VirtualMachineDeployMonitor deployMonitor;

    @VisibleForTesting
    protected VirtualMachineUndeployMonitor undeployMonitor;

    @VisibleForTesting
    protected VirtualApplianceDeployMonitor deployVirtualApplianceMonitor;

    @VisibleForTesting
    protected VirtualApplianceUndeployMonitor undeployVirtualApplianceMonitor;

    @Resource
    private Logger logger = Logger.NULL;

    /**
     * A map containing all running monitors indexed by the monitored object.
     * <p>
     * This map will be used to cancel concrete monitors when they are completed.
     */
    @VisibleForTesting
    protected Map<Object, ScheduledFuture< ? >> runningMonitors;

    /**
     * A map containing all timeouts used in object monitors.
     * <p>
     * This map will be used to cancel monitors when the timeout reaches.
     */
    @VisibleForTesting
    protected Map<Object, Long> timeouts;

    @Inject
    public BaseMonitoringService(final AbiquoContext context,
        final ScheduledExecutorService scheduler,
        @Named(ASYNC_TASK_MONITOR_DELAY) final Long pollingDelay,
        final VirtualMachineDeployMonitor deployMonitor,
        final VirtualMachineUndeployMonitor undeployMonitor,
        final VirtualApplianceDeployMonitor deployVirtualApplianceMonitor,
        final VirtualApplianceUndeployMonitor undeployVirtualApplianceMonitor)
    {
        this.context = checkNotNull(context, "context");
        this.scheduler = checkNotNull(scheduler, "scheduler");
        this.pollingDelay = checkNotNull(pollingDelay, "pollingDelay");
        this.deployMonitor = checkNotNull(deployMonitor, "deployMonitor");
        this.undeployMonitor = checkNotNull(undeployMonitor, "undeployMonitor");
        this.deployVirtualApplianceMonitor =
            checkNotNull(deployVirtualApplianceMonitor, "deployVirtualApplianceMonitor");
        this.undeployVirtualApplianceMonitor =
            checkNotNull(undeployVirtualApplianceMonitor, "undeployVirtualApplianceMonitor");
        this.runningMonitors =
            Collections.synchronizedMap(new HashMap<Object, ScheduledFuture< ? >>());
        this.timeouts = Collections.synchronizedMap(new HashMap<Object, Long>());
    }

    /*************** Virtual machine ***************/

    @Override
    public void awaitCompletionDeploy(final VirtualMachine... vms)
    {
        awaitCompletion(deployMonitor, vms);
    }

    @Override
    public void monitorDeploy(final MonitorCallback<VirtualMachine> callback,
        final VirtualMachine... vms)
    {
        monitor(callback, deployMonitor, vms);
    }

    @Override
    public void awaitCompletionDeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... vms)
    {
        awaitCompletion(maxWait, timeUnit, deployMonitor, vms);
    }

    @Override
    public void monitorDeploy(final Long maxWait, final TimeUnit timeUnit,
        final MonitorCallback<VirtualMachine> callback, final VirtualMachine... vms)
    {
        monitor(maxWait, timeUnit, callback, deployMonitor, vms);
    }

    @Override
    public void awaitCompletionUndeploy(final VirtualMachine... vms)
    {
        awaitCompletion(undeployMonitor, vms);
    }

    @Override
    public void monitorUndeploy(final MonitorCallback<VirtualMachine> callback,
        final VirtualMachine... vms)
    {
        monitor(callback, undeployMonitor, vms);
    }

    @Override
    public void awaitCompletionUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... vms)
    {
        awaitCompletion(maxWait, timeUnit, undeployMonitor, vms);
    }

    @Override
    public void monitorUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final MonitorCallback<VirtualMachine> callback, final VirtualMachine... vms)
    {
        monitor(maxWait, timeUnit, callback, undeployMonitor, vms);
    }

    @Override
    public void awaitCompletionDeploy(final VirtualAppliance... virtualAppliances)
    {
        awaitCompletion(deployVirtualApplianceMonitor, virtualAppliances);
    }

    @Override
    public void awaitCompletionDeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualAppliance... virtualAppliances)
    {
        awaitCompletion(maxWait, timeUnit, deployVirtualApplianceMonitor, virtualAppliances);
    }

    @Override
    public void awaitCompletionUndeploy(final VirtualAppliance... virtualAppliances)
    {
        awaitCompletion(undeployVirtualApplianceMonitor, virtualAppliances);
    }

    @Override
    public void awaitCompletionUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualAppliance... virtualAppliances)
    {
        awaitCompletion(maxWait, timeUnit, undeployVirtualApplianceMonitor, virtualAppliances);
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
            BlockingCallback<T> monitorLock = new BlockingCallback<T>(objects.length);
            monitor(maxWait, timeUnit, monitorLock, completeCondition, objects);
            monitorLock.lock();
        }
    }

    @Override
    public <T> void monitor(final MonitorCallback<T> callback,
        final Function<T, MonitorStatus> completeCondition, final T... objects)
    {
        monitor(null, null, callback, completeCondition, objects);
    }

    @Override
    public <T> void monitor(final Long maxWait, final TimeUnit timeUnit,
        final MonitorCallback<T> callback, final Function<T, MonitorStatus> completeCondition,
        final T... objects)
    {
        checkNotNull(callback, "callback");
        checkNotNull(completeCondition, "completeCondition");
        if (maxWait != null)
        {
            checkNotNull(timeUnit, "timeUnit");
        }

        if (objects != null && objects.length > 0)
        {
            for (T object : objects)
            {
                ScheduledFuture< ? > future =
                    scheduler.scheduleWithFixedDelay(new AsyncMonitor<T>(object,
                        completeCondition,
                        callback), 0, pollingDelay, TimeUnit.MILLISECONDS);

                // Store the future in the monitors map so we can cancel it when the task completes
                runningMonitors.put(object, future);

                // Store the timeout, if present
                if (maxWait != null)
                {
                    timeouts.put(object, System.currentTimeMillis() + timeUnit.toMillis(maxWait));
                }
            }
        }
    }

    private class AsyncMonitor<T> implements Runnable
    {
        private T monitoredObject;

        private Function<T, MonitorStatus> completeCondition;

        private MonitorCallback<T> callback;

        public AsyncMonitor(final T monitoredObject,
            final Function<T, MonitorStatus> completeCondition, final MonitorCallback<T> callback)
        {
            super();
            this.monitoredObject = monitoredObject;
            this.completeCondition = completeCondition;
            this.callback = callback;
        }

        private void stopMonitoring(final T monitoredObject)
        {
            ScheduledFuture< ? > future = runningMonitors.get(monitoredObject);

            try
            {
                if (!future.isCancelled() && !future.isDone())
                {
                    // Do not force future cancel. Let it finish gracefully
                    future.cancel(false);
                }

                // Remove it from the map only if the future has been cancelled. If cancel fails,
                // next run will remove it.
                runningMonitors.remove(monitoredObject);
                timeouts.remove(monitoredObject);
            }
            catch (Exception ex)
            {
                logger.warn(ex, "failed to stop monitor job for %s", monitoredObject);
            }
        }

        private boolean isTimeout(final T monitoredObject)
        {
            Long timeout = timeouts.get(monitoredObject);
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
                    callback.onCompleted(monitoredObject);
                    break;
                case FAILED:
                    stopMonitoring(monitoredObject);
                    callback.onFailed(monitoredObject);
                    break;
                case CONTINUE:
                default:
                    if (isTimeout(monitoredObject))
                    {
                        logger.warn("monitor for object %s timed out. Shutting down monitor.",
                            monitoredObject);
                        stopMonitoring(monitoredObject);
                        callback.onTimeout(monitoredObject);
                    }
                    break;
            }
        }
    }

    public static class BlockingCallback<T> implements MonitorCallback<T>
    {
        private CountDownLatch completeSignal;

        public BlockingCallback(final int countDownToCompletion)
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
        public void onCompleted(final T object)
        {
            release();
        }

        @Override
        public void onFailed(final T object)
        {
            release();
        }

        @Override
        public void onTimeout(final T object)
        {
            release();
        }
    }

}
