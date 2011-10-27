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

import static com.google.common.collect.Iterables.find;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.List;
import java.util.UUID;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.Datastore;
import org.jclouds.abiquo.domain.infrastructure.Machine;
import org.jclouds.abiquo.domain.infrastructure.Rack;
import org.jclouds.abiquo.domain.infrastructure.RemoteService;
import org.jclouds.abiquo.features.InfrastructureClient;
import org.jclouds.abiquo.predicates.infrastructure.RemoteServicePredicates;
import org.jclouds.abiquo.reference.AbiquoEdition;
import org.jclouds.abiquo.util.Config;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.RemoteServiceType;

/**
 * Test environment for infrastructure live tests.
 * 
 * @author Ignasi Barrera
 */
public class InfrastructureTestEnvironment implements TestEnvironment
{
    /** The rest context. */
    private AbiquoContext context;

    // Environment data made public so tests can use them easily
    public InfrastructureClient infrastructure;

    public Datacenter datacenter;

    public List<RemoteService> remoteServices;

    public Rack rack;

    private Machine machine;

    public InfrastructureTestEnvironment(final AbiquoContext context)
    {
        super();
        this.context = context;
        this.infrastructure = context.getApi().getInfrastructureClient();
    }

    @Override
    public void setup() throws Exception
    {
        createDatacenter();
        createRack();
        createMachine();
    }

    @Override
    public void tearDown() throws Exception
    {
        deleteMachine();
        deleteRack();
        deleteDatacenter();
    }

    // Setup

    private void createDatacenter()
    {
        String remoteServicesAddress = Config.get("abiquo.remoteservices.address");

        datacenter =
            Datacenter.builder(context).name(randomName()).location("Honolulu")
                .remoteServices(remoteServicesAddress, AbiquoEdition.ENTERPRISE).build();
        datacenter.save();
        assertNotNull(datacenter.getId());

        remoteServices = datacenter.listRemoteServices();
        assertEquals(remoteServices.size(), 7);
    }

    private void createMachine()
    {
        String ip = Config.get("abiquo.hypervisor.address");
        HypervisorType type = HypervisorType.valueOf(Config.get("abiquo.hypervisor.type"));
        String user = Config.get("abiquo.hypervisor.user");
        String pass = Config.get("abiquo.hypervisor.pass");

        machine = datacenter.discoverSingleMachine(ip, type, user, pass);

        String vswitch =
            machine.findAvailableVirtualSwitch(Config.get("abiquo.hypervisor.vswitch"));
        machine.setVirtualSwitch(vswitch);

        Datastore datastore = machine.findDatastore(Config.get("abiquo.hypervisor.datastore"));
        datastore.setEnabled(true);

        machine.setRack(rack);
        machine.save();
    }

    private void createRack()
    {
        rack = Rack.builder(context, datacenter).name("Aloha").build();
        rack.save();
        assertNotNull(rack.getId());
    }

    // Teardown

    private void deleteMachine()
    {
        if (machine != null && rack != null)
        {
            Integer idMachine = machine.getId();
            machine.delete();
            assertNull(infrastructure.getMachine(rack.unwrap(), idMachine));
        }
    }

    private void deleteRack()
    {
        if (rack != null && datacenter != null)
        {
            Integer idRack = rack.getId();
            rack.delete();
            assertNull(infrastructure.getRack(datacenter.unwrap(), idRack));
        }
    }

    private void deleteDatacenter()
    {
        if (datacenter != null)
        {
            Integer idDatacenter = datacenter.getId();
            datacenter.delete(); // Abiquo API will delete remote services too
            assertNull(infrastructure.getDatacenter(idDatacenter));
        }
    }

    private static String randomName()
    {
        return UUID.randomUUID().toString().substring(0, 15);
    }

    // Utility methods

    public RemoteService findRemoteService(final RemoteServiceType type)
    {
        return find(remoteServices, RemoteServicePredicates.remoteServiceType(type));
    }
}
