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
import java.util.concurrent.ScheduledExecutorService;

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
        // USe one second monitor delay in the tests
        Properties props = super.buildProperties();
        props.put(AbiquoConstants.ASYNC_TASK_MONITOR_DELAY, "1000");
        return props;
    }

    public void testAllPropertiesInjected()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        assertNotNull(service.context);
        assertNotNull(service.scheduler);
        assertNotNull(service.pollingDelay);

        assertTrue(service.context instanceof AbiquoContextImpl);
        assertTrue(service.scheduler instanceof ScheduledExecutorService);
    }

    public void testAwaitCompletion()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        AsyncTask task = new MockTask(service.context, TaskState.FINISHED_SUCCESSFULLY);
        service.awaitCompletion(task);

        assertEquals(task.getState(), TaskState.FINISHED_SUCCESSFULLY);
    }

    public void testMonitorWhenTaskSucceeds()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        AsyncTask task = new MockTask(service.context, TaskState.FINISHED_SUCCESSFULLY);

        BlockingCallback callback = new BlockingCallback();
        service.monitor(task, callback);
        callback.lock();

        assertEquals(task.getState(), TaskState.FINISHED_SUCCESSFULLY);
    }

    public void testMonitorWhenTaskFails()
    {
        BaseMonitoringService service =
            (BaseMonitoringService) injector.getInstance(MonitoringService.class);

        AsyncTask task = new MockTask(service.context, TaskState.FINISHED_UNSUCCESSFULLY);

        BlockingCallback callback = new BlockingCallback();
        service.monitor(task, callback);
        callback.lock();

        assertEquals(task.getState(), TaskState.FINISHED_UNSUCCESSFULLY);
    }

    private static class MockTask extends AsyncTask
    {
        public int finishAfterCount = 1;

        private TaskState completionState;

        public MockTask(final AbiquoContext context, final TaskState completionState)
        {
            super(context, new TaskDto());
            this.target.setState(TaskState.STARTED);
            this.completionState = completionState;
        }

        @Override
        public synchronized void refresh()
        {
            if (finishAfterCount-- <= 0)
            {
                this.target.setState(completionState);
            }
        }
    }

}
