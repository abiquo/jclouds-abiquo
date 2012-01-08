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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Properties;
import java.util.UUID;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.abiquo.internal.BaseMonitoringService.BlockingCallback;
import org.jclouds.abiquo.reference.AbiquoConstants;
import org.testng.annotations.Test;

import com.abiquo.server.core.task.TaskDto;
import com.abiquo.server.core.task.enums.TaskState;

/**
 * Unit tests for the {@link BaseMonitoringService} class.
 * 
 * @author Ignasi Barrera
 */
// It is important to leave this test single threaded. Do not change it!
@Test(groups = "unit", singleThreaded = true)
public class BaseMonitoringServiceTest extends BaseInjectionTest
{

    @Override
    protected Properties buildProperties()
    {
        // Use small scheduler delays in tests
        Properties props = super.buildProperties();
        props.put(AbiquoConstants.ASYNC_TASK_MONITOR_DELAY, "100");
        return props;
    }

    public void testAllPropertiesInjected()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        assertNotNull(service.context);
        assertNotNull(service.scheduler);
        assertNotNull(service.pollingDelay);
    }

    public void testAwaitCompletionWithoutTasks()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        service.awaitCompletion();
        assertTrue(service.runningMonitors.isEmpty());

        service.awaitCompletion((AsyncTask[]) null);
        assertTrue(service.runningMonitors.isEmpty());

        service.awaitCompletion(new AsyncTask[] {});
        assertTrue(service.runningMonitors.isEmpty());
    }

    public void testAwaitCompletion()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        AsyncTask task = new MockTask(service.context, TaskState.FINISHED_SUCCESSFULLY);
        service.awaitCompletion(task);

        assertEquals(task.getState(), TaskState.FINISHED_SUCCESSFULLY);
        assertTrue(service.runningMonitors.isEmpty());
    }

    public void testAwaitCompletionMultipleTasks()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        AsyncTask task1 = new MockTask(service.context, TaskState.FINISHED_SUCCESSFULLY);
        AsyncTask task2 = new MockTask(service.context, TaskState.FINISHED_UNSUCCESSFULLY);
        service.awaitCompletion(task1, task2);

        assertEquals(task1.getState(), TaskState.FINISHED_SUCCESSFULLY);
        assertEquals(task2.getState(), TaskState.FINISHED_UNSUCCESSFULLY);
        assertTrue(service.runningMonitors.isEmpty());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMonitorWithNullCallback()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        AsyncTask task = new MockTask(service.context, TaskState.FINISHED_SUCCESSFULLY);
        service.monitor(null, task);
    }

    public void testMonitorWithoutTasks()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);
        BlockingCallback callback = new BlockingCallback(1);

        service.monitor(callback);
        assertTrue(service.runningMonitors.isEmpty());

        service.monitor(callback, (AsyncTask[]) null);
        assertTrue(service.runningMonitors.isEmpty());

        service.monitor(callback, new AsyncTask[] {});
        assertTrue(service.runningMonitors.isEmpty());
    }

    public void testMonitorWhenTaskSucceeds()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        AsyncTask task = new MockTask(service.context, TaskState.FINISHED_SUCCESSFULLY);

        BlockingCallback callback = new BlockingCallback(1);
        service.monitor(callback, task);
        callback.lock();

        assertEquals(task.getState(), TaskState.FINISHED_SUCCESSFULLY);
        assertTrue(service.runningMonitors.isEmpty());
    }

    public void testMonitorWhenTaskFails()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        AsyncTask task = new MockTask(service.context, TaskState.FINISHED_UNSUCCESSFULLY);

        BlockingCallback callback = new BlockingCallback(1);
        service.monitor(callback, task);
        callback.lock();

        assertEquals(task.getState(), TaskState.FINISHED_UNSUCCESSFULLY);
        assertTrue(service.runningMonitors.isEmpty());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBlockingCallbackWithInvalidValues()
    {
        new BlockingCallback(0);
    }

    private static class MockTask extends AsyncTask
    {
        private int finishAfterCount;

        private TaskState completionState;

        public MockTask(final AbiquoContext context, final TaskState completionState)
        {
            super(context, new TaskDto());
            this.finishAfterCount = 1; // Simulate task completion after one refresh
            this.completionState = completionState;
            this.target.setTaskId(UUID.randomUUID().toString());
            this.target.setState(TaskState.STARTED);
        }

        @Override
        public void refresh()
        {
            if (finishAfterCount-- <= 0)
            {
                this.target.setState(completionState);
            }
        }
    }

}
