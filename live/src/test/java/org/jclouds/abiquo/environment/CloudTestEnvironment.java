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

package org.jclouds.abiquo.environment;

import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.network.Network;
import org.jclouds.abiquo.features.CloudClient;
import org.jclouds.abiquo.predicates.enterprise.EnterprisePredicates;

/**
 * Test environment for cloud live tests.
 * 
 * @author Francesc Montserrat
 */
public class CloudTestEnvironment extends InfrastructureTestEnvironment
{

    // Environment data made public so tests can use them easily
    public CloudClient cloudClient;

    public VirtualDatacenter virtualDatacenter;

    public VirtualAppliance virtualAppliance;

    public Network network;

    public Enterprise defaultEnterprise;

    // Login with created user
    AbiquoContext enterpriseContext;

    public CloudTestEnvironment(final AbiquoContext context)
    {
        super(context);
        this.cloudClient = context.getApi().getCloudClient();
    }

    @Override
    public void setup() throws Exception
    {
        createDatacenter();
        createRack();
        createMachine();
        createEnterprise();
        findDefaultEnterprise();
        createVirtualDatacenter();
        createVirtualAppliance();
    }

    @Override
    public void tearDown() throws Exception
    {
        deleteVirtualDatacenter();
        deleteMachine();
        deleteRack();
        deleteDatacenter();
        deleteEnterprise();
    }

    // Setup

    protected void findDefaultEnterprise()
    {
        defaultEnterprise =
            context.getAdministrationService().findEnterprise(EnterprisePredicates.name("Abiquo"));
    }

    protected void createVirtualDatacenter()
    {
        Network network =
            Network.builder(context).name("DefaultNetwork").gateway("192.168.1.1").address(
                "192.168.1.0").mask(24).defaultNetwork(true).build();

        virtualDatacenter =
            VirtualDatacenter.builder(context, datacenter, defaultEnterprise).name(
                PREFIX + "Virtual Aloha").cpuCountLimits(18, 20).hdLimitsInMb(279172872, 279172872)
                .publicIpsLimits(2, 2).ramLimits(19456, 20480).storageLimits(289910292, 322122547)
                .vlansLimits(1, 2).hypervisorType(machine.getType()).network(network).build();

        virtualDatacenter.save();
        assertNotNull(virtualDatacenter.getId());
    }

    protected void createVirtualAppliance()
    {
        virtualAppliance =
            VirtualAppliance.builder(context, virtualDatacenter).name("Virtual AppAloha").build();

        virtualAppliance.save();
        assertNotNull(virtualAppliance.getId());
    }

    // Tear down

    protected void deleteVirtualDatacenter()
    {
        if (virtualDatacenter != null && enterprise != null && datacenter != null)
        {
            Integer idVirtualDatacenter = virtualDatacenter.getId();
            virtualDatacenter.delete();
            assertNull(cloudClient.getVirtualDatacenter(idVirtualDatacenter));
        }
    }

    protected void deleteVirtualAppliance()
    {
        if (virtualAppliance != null && virtualDatacenter != null)
        {
            Integer idVirtualAppliance = virtualAppliance.getId();
            virtualAppliance.delete();
            assertNull(cloudClient.getVirtualAppliance(virtualDatacenter.unwrap(),
                idVirtualAppliance));
        }
    }
}
