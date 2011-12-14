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
import static org.testng.Assert.assertNull;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter.Builder;
import org.jclouds.abiquo.domain.cloud.options.VirtualDatacenterOptions;
import org.jclouds.abiquo.domain.infrastructure.Tier;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.jclouds.abiquo.predicates.cloud.VolumePredicates;
import org.testng.annotations.Test;

import com.abiquo.server.core.cloud.VirtualDatacenterDto;

/**
 * Live integration tests for the {@link VirtualDatacenter} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class VirtualDatacenterLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

    @Override
    protected CloudTestEnvironment environment(final AbiquoContext context)
    {
        return new CloudTestEnvironment(context);
    }

    public void testUpdate()
    {
        env.virtualDatacenter.setName("Aloha updated");
        env.virtualDatacenter.update();

        // Recover the updated virtual datacenter
        VirtualDatacenterDto updated =
            env.cloudClient.getVirtualDatacenter(env.virtualDatacenter.getId());

        assertEquals(updated.getName(), "Aloha updated");
    }

    public void testCreateRepeated()
    {
        VirtualDatacenter repeated = Builder.fromVirtualDatacenter(env.virtualDatacenter).build();

        // XXX the network must not exist (should have no id)
        repeated.getNetwork().unwrap().setId(null);

        repeated.save();

        List<VirtualDatacenterDto> virtualDatacenters =
            env.cloudClient.listVirtualDatacenters(VirtualDatacenterOptions.builder().build())
                .getCollection();

        assertEquals(virtualDatacenters.size(), 2);
        assertEquals(virtualDatacenters.get(0).getName(), virtualDatacenters.get(1).getName());
        repeated.delete();
    }

    @Test(enabled = false)
    public void testCreateVolume()
    {
        Tier tier = env.virtualDatacenter.listStorageTiers().get(0);
        Volume volume =
            Volume.builder(context, env.virtualDatacenter, tier).name("Hawaian volume")
                .sizeInMb(128).build();
        volume.save();

        assertNotNull(volume.getId());
        assertNotNull(env.virtualDatacenter.getVolume(volume.getId()));
    }

    @Test(dependsOnMethods = "testCreateVolume", enabled = false)
    public void testUpdateVolume()
    {
        Volume volume = env.virtualDatacenter.findVolume(VolumePredicates.name("Hawaian volume"));
        assertNotNull(volume);

        volume.setName("Hawaian volume updated");
        volume.update();

        // Reload the volume to check
        Volume updated = env.virtualDatacenter.getVolume(volume.getId());
        assertEquals(updated.getName(), "Hawaian volume updated");
    }

    @Test(dependsOnMethods = "testUpdateVolume", enabled = false)
    public void testDeleteVolume()
    {
        Volume volume =
            env.virtualDatacenter.findVolume(VolumePredicates.name("Hawaian volume updated"));
        volume.delete();

        assertNull(env.virtualDatacenter.getVolume(volume.getId()));
    }
}
