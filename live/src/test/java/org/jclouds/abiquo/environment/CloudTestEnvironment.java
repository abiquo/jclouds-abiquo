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
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.cloud.Volume;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.network.Network;
import org.jclouds.abiquo.domain.network.PrivateNetwork;
import org.jclouds.abiquo.features.CloudClient;
import org.jclouds.abiquo.predicates.cloud.VirtualMachineTemplatePredicates;
import org.jclouds.abiquo.predicates.enterprise.EnterprisePredicates;
import org.jclouds.abiquo.util.Config;

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

    public VirtualMachine virtualMachine;

    public VirtualMachineTemplate template;

    public Volume volume;

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
        // Create base infrastructure
        super.setup();
        findDefaultEnterprise();
        createVirtualDatacenter();
        createVirtualAppliance();
        refreshTemplateRepository();
        // createVirtualMachine();
    }

    @Override
    public void tearDown() throws Exception
    {
        // deleteVirtualMachine();
        deleteVirtualAppliance();
        deleteVirtualDatacenter();
        // Delete base infrastructure
        super.tearDown();
    }

    // Setup

    protected void findDefaultEnterprise()
    {
        defaultEnterprise =
            context.getAdministrationService().findEnterprise(EnterprisePredicates.name("Abiquo"));
    }

    protected void createVirtualDatacenter()
    {
        PrivateNetwork network =
            PrivateNetwork.builder(context, virtualDatacenter).name("DefaultNetwork").gateway(
                "192.168.1.1").address("192.168.1.0").mask(24).build();

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
            VirtualAppliance.builder(context, virtualDatacenter).name(PREFIX + "Virtual AppAloha")
                .build();

        virtualAppliance.save();
        assertNotNull(virtualAppliance.getId());
    }

    protected void createVirtualMachine()
    {
        String templatename = Config.get("abiquo.template.name");

        template =
            defaultEnterprise.findTemplateInRepository(datacenter, VirtualMachineTemplatePredicates
                .name(templatename));
        assertNotNull(template);

        virtualMachine =
            VirtualMachine.builder(context, virtualAppliance, template).cpu(2).name(
                PREFIX + "VM Aloha").ram(128).build();

        virtualMachine.save();
        assertNotNull(virtualMachine.getId());

    }

    protected void refreshTemplateRepository()
    {
        defaultEnterprise.refreshTemplateRepository(datacenter);
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

    protected void deleteVirtualMachine()
    {
        if (virtualMachine != null && virtualAppliance != null && virtualDatacenter != null)
        {
            Integer idVirtualMachine = virtualMachine.getId();
            virtualMachine.delete();
            assertNull(cloudClient.getVirtualMachine(virtualAppliance.unwrap(), idVirtualMachine));
        }

    }
}
