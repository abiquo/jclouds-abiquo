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

import static org.jclouds.abiquo.util.Assert.assertHasError;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.network.ExternalIp;
import org.jclouds.abiquo.domain.network.Ip;
import org.jclouds.abiquo.domain.network.PrivateIp;
import org.jclouds.abiquo.domain.network.PublicIp;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.internal.BaseAbiquoClientLiveTest;
import org.jclouds.abiquo.predicates.network.IpPredicates;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Live integration tests for the {@link VirtualMachine} networking operations.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class VirtualMachineNetworkingLiveTest extends BaseAbiquoClientLiveTest
{
    private PrivateIp privateIp;

    private ExternalIp externalIp;

    private PublicIp publicIpInfrastructure;

    private PublicIp publicIpCloud;

    @BeforeClass
    public void setupIps()
    {
        externalIp = env.externalNetwork.listUnusedIps().get(0);
        assertNotNull(externalIp);

        privateIp = env.privateNetwork.listUnusedIps().get(0);
        assertNotNull(privateIp);

        publicIpInfrastructure = env.virtualDatacenter.listAvailablePublicIps().get(0);
        env.virtualDatacenter.purchasePublicIp(publicIpInfrastructure);

        publicIpCloud =
            env.virtualDatacenter.findPurchasedPublicIp(IpPredicates
                .<PublicIp> address(publicIpInfrastructure.getIp()));
        assertNotNull(publicIpCloud);
    }

    @AfterClass
    public void restorePrivateIp()
    {
        AsyncTask task = env.virtualMachine.setNics(privateIp);
        assertNull(task);

        List<Ip< ? , ? >> nics = env.virtualMachine.listAttachedNics();
        assertEquals(nics.size(), 1);
        assertEquals(nics.get(0).getId(), privateIp.getId());
    }

    // TODO: Infrastructure edit link for public ips can not be used to attach
    @Test(enabled = false)
    public void testAttachInfrastructurePublicIp()
    {
        AsyncTask task = env.virtualMachine.setNics(publicIpInfrastructure);
        assertNull(task);

        List<Ip< ? , ? >> nics = env.virtualMachine.listAttachedNics();
        assertEquals(nics.size(), 1);
        assertEquals(nics.get(0).getId(), publicIpInfrastructure.getId());
    }

    public void testAttachPublicIp()
    {
        AsyncTask task = env.virtualMachine.setNics(publicIpCloud);
        assertNull(task);

        List<Ip< ? , ? >> nics = env.virtualMachine.listAttachedNics();
        assertEquals(nics.size(), 1);
        assertEquals(nics.get(0).getId(), publicIpCloud.getId());
    }

    @Test(dependsOnMethods = "testAttachPublicIp")
    public void testAttachPrivateIp()
    {
        List<Ip< ? , ? >> nics = env.virtualMachine.listAttachedNics();
        nics.add(privateIp);

        Ip< ? , ? >[] ips = new Ip< ? , ? >[nics.size()];
        AsyncTask task = env.virtualMachine.setNics(nics.toArray(ips));
        assertNull(task);

        nics = env.virtualMachine.listAttachedNics();
        assertEquals(nics.size(), 2);
        assertEquals(nics.get(0).getId(), publicIpCloud.getId());
        assertEquals(nics.get(1).getId(), privateIp.getId());
    }

    @Test(dependsOnMethods = {"testAttachPublicIp", "testAttachPrivateIp"})
    public void testAttachExternalIp()
    {
        List<Ip< ? , ? >> nics = env.virtualMachine.listAttachedNics();
        nics.add(externalIp);

        Ip< ? , ? >[] ips = new Ip< ? , ? >[nics.size()];
        AsyncTask task = env.virtualMachine.setNics(nics.toArray(ips));
        assertNull(task);

        nics = env.virtualMachine.listAttachedNics();
        assertEquals(nics.size(), 3);
        assertEquals(nics.get(0).getId(), publicIpCloud.getId());
        assertEquals(nics.get(1).getId(), privateIp.getId());
        assertEquals(nics.get(2).getId(), externalIp.getId());
    }

    @Test(dependsOnMethods = {"testAttachPublicIp", "testAttachPrivateIp", "testAttachExternalIp"})
    public void testReorderNics()
    {
        List<Ip< ? , ? >> nics = env.virtualMachine.listAttachedNics();

        AsyncTask task = env.virtualMachine.setNics(nics.get(2), nics.get(1), nics.get(0));
        assertNull(task);

        nics = env.virtualMachine.listAttachedNics();
        assertEquals(nics.size(), 3);
        assertEquals(nics.get(0).getId(), externalIp.getId());
        assertEquals(nics.get(1).getId(), privateIp.getId());
        assertEquals(nics.get(2).getId(), publicIpCloud.getId());
    }

    @Test(dependsOnMethods = {"testAttachPublicIp", "testAttachPrivateIp", "testAttachExternalIp",
    "testReorderNics"})
    public void testDetachSingleNic()
    {
        List<Ip< ? , ? >> nics = env.virtualMachine.listAttachedNics();

        AsyncTask task = env.virtualMachine.setNics(nics.get(1), nics.get(2));
        assertNull(task);

        nics = env.virtualMachine.listAttachedNics();
        assertEquals(nics.size(), 2);
        assertEquals(nics.get(0).getId(), privateIp.getId());
        assertEquals(nics.get(1).getId(), publicIpCloud.getId());
    }

    public void testDetachAllNics()
    {
        try
        {
            env.virtualMachine.setNics();
        }
        catch (AbiquoException ex)
        {
            // At least one nic must be configured
            assertHasError(ex, Status.BAD_REQUEST, "VM-46");
        }
    }
}
