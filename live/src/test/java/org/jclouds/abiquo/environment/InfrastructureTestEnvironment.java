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
import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.List;
import java.util.UUID;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.Datastore;
import org.jclouds.abiquo.domain.infrastructure.Machine;
import org.jclouds.abiquo.domain.infrastructure.Rack;
import org.jclouds.abiquo.domain.infrastructure.RemoteService;
import org.jclouds.abiquo.domain.infrastructure.StorageDevice;
import org.jclouds.abiquo.features.EnterpriseClient;
import org.jclouds.abiquo.features.InfrastructureClient;
import org.jclouds.abiquo.predicates.infrastructure.RemoteServicePredicates;
import org.jclouds.abiquo.reference.AbiquoEdition;
import org.jclouds.abiquo.util.Config;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.model.enumerator.StorageTechnologyType;

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
    public InfrastructureClient infrastructureClient;

    public EnterpriseClient enterpriseClient;

    public Datacenter datacenter;

    public List<RemoteService> remoteServices;

    public Rack rack;

    public Machine machine;

    public Enterprise enterprise;

    public StorageDevice storageDevice;

    public InfrastructureTestEnvironment(final AbiquoContext context)
    {
        super();
        this.context = context;
        this.infrastructureClient = context.getApi().getInfrastructureClient();
        this.enterpriseClient = context.getApi().getEnterpriseClient();
    }

    @Override
    public void setup() throws Exception
    {
        createDatacenter();
        createRack();
        createMachine();
        createStorageDevice();
        createEnterprise();
    }

    @Override
    public void tearDown() throws Exception
    {
        deleteStorageDevice();
        deleteMachine();
        deleteRack();
        deleteDatacenter();
        deleteEnterprise();
    }

    // Setup

    private void createDatacenter()
    {
        String remoteServicesAddress = Config.get("abiquo.remoteservices.address");

        datacenter =
            Datacenter.builder(context).name(randomName()).location("Honolulu").remoteServices(
                remoteServicesAddress, AbiquoEdition.ENTERPRISE).build();
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
        rack = Rack.builder(context, datacenter).name(PREFIX + "Aloha").build();
        rack.save();
        assertNotNull(rack.getId());
    }

    private void createStorageDevice()
    {
        String ip = Config.get("abiquo.hypervisor.address");
        StorageTechnologyType type =
            StorageTechnologyType.valueOf(Config.get("abiquo.storage.type"));
        String user = Config.get("abiquo.storage.user");
        String pass = Config.get("abiquo.storage.pass");

        storageDevice =
            StorageDevice.builder(context, datacenter).iscsiIp(ip).iscsiPort(3260).managementIp(ip)
                .name(PREFIX + "Storage Device").managementPort(8180).username(user).password(pass)
                .storageTechnology(type).build();

        storageDevice.save();
        assertNotNull(storageDevice.getId());
    }

    private void createEnterprise()
    {
        enterprise = Enterprise.builder(context).name(randomName()).build();
        enterprise.save();
        assertNotNull(enterprise.getId());
    }

    // Tear down

    private void deleteStorageDevice()
    {
        if (storageDevice != null)
        {
            Integer idStorageDevice = storageDevice.getId();
            storageDevice.delete();
            assertNull(infrastructureClient.getStorageDevice(datacenter.unwrap(), idStorageDevice));
        }
    }

    private void deleteMachine()
    {
        if (machine != null && rack != null)
        {
            Integer idMachine = machine.getId();
            machine.delete();
            assertNull(infrastructureClient.getMachine(rack.unwrap(), idMachine));
        }
    }

    private void deleteRack()
    {
        if (rack != null && datacenter != null)
        {
            Integer idRack = rack.getId();
            rack.delete();
            assertNull(infrastructureClient.getRack(datacenter.unwrap(), idRack));
        }
    }

    private void deleteDatacenter()
    {
        if (datacenter != null)
        {
            Integer idDatacenter = datacenter.getId();
            datacenter.delete(); // Abiquo API will delete remote services too
            assertNull(infrastructureClient.getDatacenter(idDatacenter));
        }
    }

    private void deleteEnterprise()
    {
        if (enterprise != null)
        {
            Integer idEnterprise = enterprise.getId();
            enterprise.delete();
            assertNull(enterpriseClient.getEnterprise(idEnterprise));
        }
    }

    private static String randomName()
    {
        return PREFIX + UUID.randomUUID().toString().substring(0, 12);
    }

    // Utility methods

    public RemoteService findRemoteService(final RemoteServiceType type)
    {
        return find(remoteServices, RemoteServicePredicates.remoteServiceType(type));
    }
}
