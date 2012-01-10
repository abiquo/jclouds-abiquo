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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.abiquo.functions.monitor.VirtualMachineDeployMonitor;
import org.jclouds.abiquo.functions.monitor.VirtualMachineUndeployMonitor;
import org.jclouds.abiquo.internal.BaseMonitoringService.BlockingEventHandler;
import org.jclouds.abiquo.monitor.MonitorStatus;
import org.jclouds.abiquo.monitor.events.MonitorEvent;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;

/**
 * Unit tests for the {@link BaseMonitoringService} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class BaseMonitoringServiceTest extends BaseInjectionTest
{
    private static final long TEST_MONITOR_TIMEOUT = 1000L;

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
        mockMonitoringService().awaitCompletion(null, new Object[] {});
    }

    public void testAwaitCompletionWithoutTasks()
    {
        BaseMonitoringService service = mockMonitoringService();

        service.awaitCompletion(new MockMonitor());
        service.awaitCompletion(new MockMonitor(), (Object[]) null);
        service.awaitCompletion(new MockMonitor(), new Object[] {});
    }

    @Test
    public void testAwaitCompletion()
    {
        mockMonitoringService().awaitCompletion(new MockMonitor(), new Object());
    }

    @Test
    public void testAwaitCompletionMultipleTasks()
    {
        mockMonitoringService().awaitCompletion(new MockMonitor(), new Object(), new Object());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMonitorWithNullCompletecondition()
    {
        mockMonitoringService().monitor(null, (Object[]) null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBlockingHandlerWithoutArguments()
    {
        new BlockingEventHandler<Object>();
    }

    public void testMonitor()
    {
        BaseMonitoringService service = mockMonitoringService();

        Object monitoredObject = new Object();
        CountingHandler handler = new CountingHandler(monitoredObject);
        service.eventBus.register(handler);

        service.monitor(new MockMonitor(), monitoredObject);
        handler.lock();

        service.eventBus.unregister(handler);

        assertEquals(handler.numCompletes, 1);
        assertEquals(handler.numFailures, 0);
        assertEquals(handler.numTimeouts, 0);
    }

    public void testMonitorMultipleTasks()
    {
        BaseMonitoringService service = mockMonitoringService();

        Object monitoredObject1 = new Object();
        Object monitoredObject2 = new Object();
        CountingHandler handler = new CountingHandler(monitoredObject1, monitoredObject2);
        service.eventBus.register(handler);

        service.monitor(new MockMonitor(), monitoredObject1, monitoredObject2);
        handler.lock();

        service.eventBus.unregister(handler);

        assertEquals(handler.numCompletes, 2);
        assertEquals(handler.numFailures, 0);
        assertEquals(handler.numTimeouts, 0);
    }

    public void testMonitorReachesTimeout()
    {
        BaseMonitoringService service = mockMonitoringService();

        Object monitoredObject = new Object();
        CountingHandler handler = new CountingHandler(monitoredObject);
        service.eventBus.register(handler);

        service.monitor(TEST_MONITOR_TIMEOUT, TimeUnit.MILLISECONDS, new MockInfiniteMonitor(),
            monitoredObject);
        handler.lock();

        service.eventBus.unregister(handler);

        assertEquals(handler.numCompletes, 0);
        assertEquals(handler.numFailures, 0);
        assertEquals(handler.numTimeouts, 1);
    }

    public void testMonitorMultipleTasksReachesTimeout()
    {
        BaseMonitoringService service = mockMonitoringService();

        Object monitoredObject1 = new Object();
        Object monitoredObject2 = new Object();
        CountingHandler handler = new CountingHandler(monitoredObject1, monitoredObject2);
        service.eventBus.register(handler);

        service.monitor(TEST_MONITOR_TIMEOUT, TimeUnit.MILLISECONDS, new MockInfiniteMonitor(),
            monitoredObject1, monitoredObject2);
        handler.lock();

        service.eventBus.unregister(handler);

        assertEquals(handler.numCompletes, 0);
        assertEquals(handler.numFailures, 0);
        assertEquals(handler.numTimeouts, 2);
    }

    private BaseMonitoringService mockMonitoringService()
    {
        return new BaseMonitoringService(injector.getInstance(AbiquoContext.class),
            injector.getInstance(ScheduledExecutorService.class),
            TEST_MONITOR_TIMEOUT, // Use a small delay in tests
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

    static class CountingHandler extends BlockingEventHandler<Object>
    {
        public int numCompletes = 0;

        public int numFailures = 0;

        public int numTimeouts = 0;

        public CountingHandler(final Object... lockedObjects)
        {
            super(lockedObjects);
        }

        @Override
        @Subscribe
        public void release(final MonitorEvent<Object> event)
        {
            System.err.println("Handling: " + event.getType().name() + " on " + event.getTarget());

            if (lockedObjects.contains(event.getTarget()))
            {
                switch (event.getType())
                {
                    case COMPLETED:
                        numCompletes++;
                        break;
                    case FAILED:
                        numFailures++;
                        break;
                    case TIMEOUT:
                        numTimeouts++;
                        break;
                }

                super.release(event);
            }
        }

    }

}
