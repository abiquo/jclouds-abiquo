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

package org.jclouds.abiquo.domain.cloud;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.cloud.VirtualMachineState;

/**
 * Live integration tests for the {@link VirtualMachine} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class VirtualMachineLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

    public void testGetTasks()
    {
        List<AsyncTask> tasks = env.virtualMachine.listTasks();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    public void testGetState()
    {
        VirtualMachineState state = env.virtualMachine.getState();
        assertEquals(state, VirtualMachineState.NOT_ALLOCATED);
    }
}
