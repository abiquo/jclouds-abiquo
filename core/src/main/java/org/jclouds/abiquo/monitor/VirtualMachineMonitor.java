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
package org.jclouds.abiquo.monitor;

import java.util.concurrent.TimeUnit;

import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.abiquo.monitor.internal.BaseVirtualMachineMonitor;

import com.google.inject.ImplementedBy;

/**
 * {@link VirtualMachine} monitoring features.
 * 
 * @author Ignasi Barrera
 */
@ImplementedBy(BaseVirtualMachineMonitor.class)
public interface VirtualMachineMonitor extends MonitoringService
{
    /**
     * Monitor the given {@link VirtualMachine}s and block until the deploy finishes.
     * 
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    void awaitCompletionDeploy(final VirtualMachine... virtualMachine);

    /**
     * Monitor the given {@link VirtualMachine}s and populate an event when the deploy finishes.
     * 
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    public void monitorDeploy(final VirtualMachine... vms);

    /**
     * Monitor the given {@link VirtualMachine}s and block until the deploy finishes.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    void awaitCompletionDeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... virtualMachine);

    /**
     * Monitor the given {@link VirtualMachine}s and populate an event when deploy finishes.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    public void monitorDeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... vms);

    /**
     * Monitor the given {@link VirtualMachine}s and block until the undeploy finishes.
     * 
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    void awaitCompletionUndeploy(final VirtualMachine... virtualMachine);

    /**
     * Monitor the given {@link VirtualMachine}s and call populate an event when undeploy finishes.
     * 
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    public void monitorUndeploy(final VirtualMachine... vms);

    /**
     * Monitor the given {@link VirtualMachine}s and blocks until the undeploy finishes.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    void awaitCompletionUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... virtualMachine);

    /**
     * Monitor the given {@link VirtualMachine}s and populate an event when undeploy finishes.
     * 
     * @param maxWait The maximum time to wait.
     * @param timeUnit The time unit for the maxWait parameter.
     * @param callback The callback.
     * @param virtualMachine The {@link VirtualMachine}s to monitor.
     */
    public void monitorUndeploy(final Long maxWait, final TimeUnit timeUnit,
        final VirtualMachine... vms);
}
