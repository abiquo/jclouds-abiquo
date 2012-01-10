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

import static org.easymock.EasyMock.createMock;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ScheduledExecutorService;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.abiquo.functions.monitor.VirtualMachineDeployMonitor;
import org.jclouds.abiquo.functions.monitor.VirtualMachineUndeployMonitor;
import org.jclouds.abiquo.internal.BaseMonitoringService.BlockingEventHandler;
import org.jclouds.abiquo.monitor.MonitorStatus;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.eventbus.AsyncEventBus;

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
        assertNotNull(service.eventBus);
        assertNotNull(service.deployMonitor);
        assertNotNull(service.undeployMonitor);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testAwaitCompletionWithNullFunction()
    {
        BaseMonitoringService service = mockMonitoringService();
        service.awaitCompletion(null, new Object[] {});
    }

    public void testAwaitCompletionWithoutTasks()
    {
        BaseMonitoringService service = mockMonitoringService();

        service.awaitCompletion(new MockMonitor());
        assertTrue(service.runningMonitors.isEmpty());

        service.awaitCompletion(new MockMonitor(), (Object[]) null);
        assertTrue(service.runningMonitors.isEmpty());

        service.awaitCompletion(new MockMonitor(), new Object[] {});
        assertTrue(service.runningMonitors.isEmpty());
    }

    public void testAwaitCompletion()
    {
        BaseMonitoringService service = mockMonitoringService();

        service.awaitCompletion(new MockMonitor(), new Object());
        assertTrue(service.runningMonitors.isEmpty());
    }

    @Test(enabled = false)
    public void testAwaitCompletionMultipleTasks()
    {
        BaseMonitoringService service = mockMonitoringService();

        service.awaitCompletion(new MockMonitor(), new Object(), new Object());
        assertTrue(service.runningMonitors.isEmpty());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMonitorWithNullCompletecondition()
    {
        BaseMonitoringService service = mockMonitoringService();
        service.monitor(null, (Object[]) null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBlockingHandlerWithoutArguments()
    {
        new BlockingEventHandler<Object>();
    }

    // public void testMonitor()
    // {
    // BaseMonitoringService service = mockMonitoringService();
    // CountingCallback callback = new CountingCallback(1);
    //
    // service.monitor(callback, new MockMonitor(), new Object());
    // callback.lock();
    //
    // assertTrue(service.runningMonitors.isEmpty());
    // assertEquals(callback.numCompletes, 1);
    // assertEquals(callback.numFailures, 0);
    // assertEquals(callback.numTimeouts, 0);
    // }
    //
    // public void testMonitorMultipleTasks()
    // {
    // BaseMonitoringService service = mockMonitoringService();
    // CountingCallback callback = new CountingCallback(2);
    //
    // service.monitor(callback, new MockMonitor(), new Object(), new Object());
    // callback.lock();
    //
    // assertTrue(service.runningMonitors.isEmpty());
    // assertEquals(callback.numCompletes, 2);
    // assertEquals(callback.numFailures, 0);
    // assertEquals(callback.numTimeouts, 0);
    // }
    //
    // public void testMonitorReachesTimeout()
    // {
    // BaseMonitoringService service = mockMonitoringService();
    // CountingCallback callback = new CountingCallback(1);
    //
    // service.monitor(100L, TimeUnit.MILLISECONDS, callback, new MockInfiniteMonitor(),
    // new Object());
    // callback.lock();
    //
    // assertTrue(service.runningMonitors.isEmpty());
    // assertEquals(callback.numCompletes, 0);
    // assertEquals(callback.numFailures, 0);
    // assertEquals(callback.numTimeouts, 1);
    // }
    //
    // public void testMonitorMultipleTasksReachesTimeout()
    // {
    // BaseMonitoringService service = mockMonitoringService();
    // CountingCallback callback = new CountingCallback(2);
    //
    // service.monitor(100L, TimeUnit.MILLISECONDS, callback, new MockInfiniteMonitor(),
    // new Object(), new Object());
    // callback.lock();
    //
    // assertTrue(service.runningMonitors.isEmpty());
    // assertEquals(callback.numCompletes, 0);
    // assertEquals(callback.numFailures, 0);
    // assertEquals(callback.numTimeouts, 2);
    // }

    private BaseMonitoringService mockMonitoringService()
    {
        return new BaseMonitoringService(injector.getInstance(AbiquoContext.class),
            injector.getInstance(ScheduledExecutorService.class),
            100L, // Use a small delay in tests
            injector.getInstance(AsyncEventBus.class),
            createMock(VirtualMachineDeployMonitor.class),
            createMock(VirtualMachineUndeployMonitor.class));
    }

    private static class MockMonitor implements Function<Object, MonitorStatus>
    {
        private int finishAfterCount;

        public MockMonitor()
        {
            this.finishAfterCount = 1; // Simulate task completion after one refresh
        }

        @Override
        public MonitorStatus apply(final Object object)
        {
            return finishAfterCount-- <= 0 ? MonitorStatus.DONE : MonitorStatus.CONTINUE;
        }
    }

    private static class MockInfiniteMonitor implements Function<Object, MonitorStatus>
    {

        @Override
        public MonitorStatus apply(final Object object)
        {
            return MonitorStatus.CONTINUE;
        }
    }

    private static class CountingCallback extends BlockingEventHandler<Object>
    {
        public int numCompletes = 0;

        public int numFailures = 0;

        public int numTimeouts = 0;

        public CountingCallback(final int countDownToCompletion)
        {
            super(countDownToCompletion);
        }

        // @Override
        // public void onCompleted(final Object object)
        // {
        // numCompletes++;
        // super.onCompleted(object);
        // }
        //
        // @Override
        // public void onFailed(final Object object)
        // {
        // numFailures++;
        // super.onFailed(object);
        // }
        //
        // @Override
        // public void onTimeout(final Object object)
        // {
        // numTimeouts++;
        // super.onTimeout(object);
        // }

    }

}
