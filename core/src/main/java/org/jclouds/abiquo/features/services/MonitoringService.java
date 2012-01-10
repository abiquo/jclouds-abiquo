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

package org.jclouds.abiquo.features.services;

import java.util.concurrent.TimeUnit;

import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.monitor.MonitorCallback;
import org.jclouds.abiquo.domain.monitor.MonitorStatus;
import org.jclouds.abiquo.internal.BaseMonitoringService;

import com.google.common.base.Function;
import com.google.inject.ImplementedBy;

/**
 * Utility service to monitor asynchronous operations.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@ImplementedBy(BaseMonitoringService.class)
public interface MonitoringService
{
    /*************** Virtual machine ***************/

    /**
     * Monitor the given {@link VirtualMachine}s and blocks until either the deploy is met or
     * failed.
     * 
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    void awaitCompletionDeploy(final VirtualMachine... virtualMachine);

    /**
     * Monitor the given {@link VirtualMachine}s and call the given callback the deploy is met or
     * failed.
     * 
     * @param callback The callback.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    public void monitorDeploy(final MonitorCallback<VirtualMachine> callback,
        final VirtualMachine... vms);

    /**
     * Monitor the given {@link VirtualMachine}s and blocks until either the deploy is met or
     * failed.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    void awaitCompletionDeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... virtualMachine);

    /**
     * Monitor the given {@link VirtualMachine}s and call the given callback the deploy is met or
     * failed.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param callback The callback.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    public void monitorDeploy(final Long maxWait, final TimeUnit timeUnit,
        final MonitorCallback<VirtualMachine> callback, final VirtualMachine... vms);

    /**
     * Monitor the given {@link VirtualMachine}s and blocks until either the undeploy is met or
     * failed.
     * 
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    void awaitCompletionUndeploy(final VirtualMachine... virtualMachine);

    /**
     * Monitor the given {@link VirtualMachine}s and call the given callback the undeploy is met or
     * failed.
     * 
     * @param callback The callback.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    public void monitorUndeploy(final MonitorCallback<VirtualMachine> callback,
        final VirtualMachine... vms);

    /**
     * Monitor the given {@link VirtualMachine}s and blocks until either the undeploy is met or
     * failed.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    void awaitCompletionUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... virtualMachine);

    /**
     * Monitor the given {@link VirtualMachine}s and call the given callback the undeploy is met or
     * failed.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param callback The callback.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    public void monitorUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final MonitorCallback<VirtualMachine> callback, final VirtualMachine... vms);

    /*************** Virtual appliance *************/
    /**
     * Monitor the given {@link VirtualAppliance}s and blocks until either the deploy is met or
     * failed.
     * 
     * @param virtualAppliances The {@link VirtualAppliance}s to monitor.
     */
    void awaitCompletionDeploy(final VirtualAppliance... virtualAppliances);

    /**
     * Monitor the given {@link VirtualAppliance}s and blocks until either the deploy is met or
     * failed.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param virtualAppliances The {@link VirtualAppliance}s to monitor.
     */
    void awaitCompletionDeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualAppliance... virtualAppliances);

    /**
     * Monitor the given {@link VirtualAppliance}s and blocks until either the undeploy is met or
     * failed.
     * 
     * @param virtualAppliances The {@link VirtualAppliance}s to monitor.
     */
    void awaitCompletionUndeploy(final VirtualAppliance... virtualAppliances);

    /**
     * Monitor the given {@link VirtualAppliance}s and blocks until either the undeploy is met or
     * failed.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param virtualAppliances The {@link VirtualAppliance}s to monitor.
     */
    void awaitCompletionUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualAppliance... virtualAppliances);

    /*************** Generic methods ***************/

    /**
     * Monitor the given objects using the given complete condition.
     * 
     * @param completeCondition The function that will be used to decide if the asynchronous
     *            operations have finished.
     * @param objects The objects to monitor.
     */
    public <T> void awaitCompletion(final Function<T, MonitorStatus> completeCondition,
        final T... objects);

    /**
     * Monitor the given objects using the given complete condition.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param completeCondition The function that will be used to decide if the asynchronous
     *            operations have finished.
     * @param objects The objects to monitor.
     */
    public <T> void awaitCompletion(final Long maxWait, final TimeUnit timeUnit,
        final Function<T, MonitorStatus> completeCondition, final T... objects);

    /**
     * Monitor the given objects using the given complete condition.
     * 
     * @param callback The callback to be invoked when an asynchronous operation completes.
     * @param completeCondition The function that will be used to decide if the asynchronous
     *            operations have finished.
     * @param objects The objects to monitor.
     */
    public <T> void monitor(final MonitorCallback<T> callback,
        final Function<T, MonitorStatus> completeCondition, final T... objects);

    /**
     * Monitor the given objects using the given complete condition.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param callback The callback to be invoked when an asynchronous operation completes.
     * @param completeCondition The function that will be used to decide if the asynchronous
     *            operations have finished.
     * @param objects The objects to monitor.
     */
    public <T> void monitor(final Long maxWait, final TimeUnit timeUnit,
        final MonitorCallback<T> callback, final Function<T, MonitorStatus> completeCondition,
        final T... objects);

}
