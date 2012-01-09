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

import static org.jclouds.abiquo.domain.DomainWrapper.wrap;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ScheduledExecutorService;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.monitor.MonitorStatus;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.abiquo.functions.monitor.VirtualMachineDeployMonitor;
import org.jclouds.abiquo.functions.monitor.VirtualMachineUndeployMonitor;
import org.jclouds.abiquo.internal.BaseMonitoringService.BlockingCallback;
import org.testng.annotations.Test;

import com.abiquo.server.core.cloud.VirtualMachineDto;

/**
 * Unit tests for the {@link BaseMonitoringService} class.
 * 
 * @author Ignasi Barrera
 */
// It is important to leave this test single threaded. Do not change it!
@Test(groups = "unit", singleThreaded = true)
public class BaseMonitoringServiceTest extends BaseInjectionTest
{

    public void testAllPropertiesInjected()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        assertNotNull(service.context);
        assertNotNull(service.scheduler);
        assertNotNull(service.pollingDelay);
        assertNotNull(service.deployMonitor);
        assertNotNull(service.undeployMonitor);
    }

    public void testAwaitCompletionWithoutTasks()
    {
        BaseMonitoringService service = mockMonitoringService();

        service.awaitCompletionDeploy();
        assertTrue(service.runningMonitors.isEmpty());

        service.awaitCompletionDeploy((VirtualMachine[]) null);
        assertTrue(service.runningMonitors.isEmpty());

        service.awaitCompletionDeploy(new VirtualMachine[] {});
        assertTrue(service.runningMonitors.isEmpty());
    }

    public void testAwaitCompletion()
    {
        BaseMonitoringService service = mockMonitoringService();
        VirtualMachine vm = wrap(service.context, VirtualMachine.class, new VirtualMachineDto());

        service.awaitCompletionDeploy(vm);
        assertTrue(service.runningMonitors.isEmpty());
    }

    public void testAwaitCompletionMultipleTasks()
    {
        BaseMonitoringService service = mockMonitoringService();
        VirtualMachine vm1 = wrap(service.context, VirtualMachine.class, new VirtualMachineDto());
        VirtualMachine vm2 = wrap(service.context, VirtualMachine.class, new VirtualMachineDto());

        service.awaitCompletionDeploy(vm1, vm2);
        assertTrue(service.runningMonitors.isEmpty());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMonitorWithNullCallback()
    {
        BaseMonitoringService service = mockMonitoringService();
        service.monitor(null, null, (Object[]) null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBlockingCallbackWithInvalidValues()
    {
        new BlockingCallback<Object>(0);
    }

    private BaseMonitoringService mockMonitoringService()
    {
        return new BaseMonitoringService(injector.getInstance(AbiquoContext.class),
            injector.getInstance(ScheduledExecutorService.class),
            100L, // Use a small delay in tests
            new MockDeployMonitor(),
            new MockUndeployMonitor());
    }

    private static class MockDeployMonitor extends VirtualMachineDeployMonitor
    {
        private int finishAfterCount;

        public MockDeployMonitor()
        {
            this.finishAfterCount = 1; // Simulate task completion after one refresh
        }

        @Override
        public MonitorStatus apply(final VirtualMachine virtualMachine)
        {
            return finishAfterCount-- <= 0 ? MonitorStatus.DONE : MonitorStatus.CONTINUE;
        }
    }

    private static class MockUndeployMonitor extends VirtualMachineUndeployMonitor
    {
        private int finishAfterCount;

        public MockUndeployMonitor()
        {
            this.finishAfterCount = 1; // Simulate task completion after one refresh
        }

        @Override
        public MonitorStatus apply(final VirtualMachine virtualMachine)
        {
            return finishAfterCount-- <= 0 ? MonitorStatus.DONE : MonitorStatus.CONTINUE;
        }
    }

}
