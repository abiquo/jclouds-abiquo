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

import static com.google.common.collect.Iterables.size;
import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jclouds.abiquo.domain.cloud.VirtualDatacenter.Builder;
import org.jclouds.abiquo.domain.cloud.options.VirtualDatacenterOptions;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.network.PrivateNetwork;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;

/**
 * Live integration tests for the {@link VirtualDatacenter} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class VirtualDatacenterLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

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
        PrivateNetwork newnet =
            PrivateNetwork.builder(context).name("Newnet").gateway("10.0.0.1").address("10.0.0.0")
                .mask(24).build();

        VirtualDatacenter repeated =
            Builder.fromVirtualDatacenter(env.virtualDatacenter).network(newnet).build();

        repeated.save();

        List<VirtualDatacenterDto> virtualDatacenters =
            env.cloudClient.listVirtualDatacenters(VirtualDatacenterOptions.builder().build())
                .getCollection();

        assertEquals(virtualDatacenters.size(), 2);
        assertEquals(virtualDatacenters.get(0).getName(), virtualDatacenters.get(1).getName());
        repeated.delete();
    }

    public void testCreateFromEnterprise()
    {
        // Datacenter must be allowed to enterprise
        env.enterprise.allowDatacenter(env.datacenter);

        Enterprise enterprise =
            env.plainUserContext.getAdministrationService().getCurrentUserInfo().getEnterprise();
        assertNotNull(enterprise);

        List<Datacenter> datacenters = enterprise.listAllowedDatacenters();
        assertNotNull(datacenters);
        assertTrue(size(datacenters) > 0);

        Datacenter datacenter = datacenters.get(0);
        assertNotNull(datacenter);

        List<HypervisorType> hypervisors = datacenter.listAvailableHypervisors();
        assertNotNull(datacenters);
        assertTrue(size(datacenters) > 0);

        HypervisorType hypervisor = hypervisors.get(0);

        PrivateNetwork network =
            PrivateNetwork.builder(env.plainUserContext).name("DefaultNetwork")
                .gateway("192.168.1.1").address("192.168.1.0").mask(24).build();

        VirtualDatacenter virtualDatacenter =
            VirtualDatacenter.builder(context, datacenters.get(0), enterprise)
                .name(PREFIX + "Plain Virtual Aloha").cpuCountLimits(18, 20)
                .hdLimitsInMb(279172872, 279172872).publicIpsLimits(2, 2).ramLimits(19456, 20480)
                .storageLimits(289910292, 322122547).vlansLimits(1, 2).hypervisorType(hypervisor)
                .network(network).build();

        virtualDatacenter.save();
        assertNotNull(virtualDatacenter.getId());

        virtualDatacenter.delete();
    }

    public void testGetDefaultNetwork()
    {
        PrivateNetwork network = env.virtualDatacenter.getDefaultNetwork().toPrivateNetwork();

        assertNotNull(network);
        assertEquals(network.getName(), env.network.getName());
        assertEquals(network.getType(), env.network.getType());
    }

}
