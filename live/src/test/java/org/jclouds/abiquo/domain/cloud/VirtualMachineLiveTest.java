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

import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jclouds.abiquo.domain.infrastructure.Tier;
import org.jclouds.abiquo.domain.network.Nic;
import org.jclouds.abiquo.domain.network.PrivateIPAddress;
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
    private Volume volume;

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

    @Test(enabled = false)
    public void testCreateNic()
    {
        // TODO not working -> ip self link missing in API
        PrivateIPAddress ip = env.virtualDatacenter.listPrivateNetworks().get(0).listIps().get(0);

        Nic nic = env.virtualMachine.createNic(ip);

        assertNotNull(nic);
        assertNotNull(nic.getId());
    }

    public void testAttachVolumes()
    {
        volume = createVolume();

        // Since the virtual machine is not deployed, this should not generate a task
        AsyncTask task = env.virtualMachine.attachVolumes(volume);
        assertNull(task);

        List<Volume> attached = env.virtualMachine.listAttachedVolumes();
        assertEquals(attached.size(), 1);
        assertEquals(attached.get(0).getId(), volume.getId());
    }

    @Test(dependsOnMethods = "testAttachVolumes")
    public void detachVolumes()
    {
        env.virtualMachine.detachVolumes(volume);
        List<Volume> attached = env.virtualMachine.listAttachedVolumes();
        assertTrue(attached.isEmpty());
    }

    @Test(dependsOnMethods = {"testAttachVolumes", "detachVolumes"})
    public void detachAllVolumes()
    {
        // Since the virtual machine is not deployed, this should not generate a task
        AsyncTask task = env.virtualMachine.attachVolumes(volume);
        assertNull(task);

        env.virtualMachine.detachAllVolumes();
        List<Volume> attached = env.virtualMachine.listAttachedVolumes();
        assertTrue(attached.isEmpty());

        deleteVolume(volume);
    }

    private Volume createVolume()
    {
        Tier tier = env.virtualDatacenter.listStorageTiers().get(0);
        Volume volume =
            Volume.builder(context, env.virtualDatacenter, tier).name(PREFIX + "Hawaian volume")
                .sizeInMb(128).build();
        volume.save();

        assertNotNull(volume.getId());
        assertNotNull(env.virtualDatacenter.getVolume(volume.getId()));

        return volume;
    }

    private void deleteVolume(final Volume volume)
    {
        Integer id = volume.getId();
        volume.delete();
        assertNull(env.virtualDatacenter.getVolume(id));
    }
}
