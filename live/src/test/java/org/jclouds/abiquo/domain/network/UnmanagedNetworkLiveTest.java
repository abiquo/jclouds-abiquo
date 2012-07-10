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

package org.jclouds.abiquo.domain.network;

import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.jclouds.abiquo.util.Assert.assertHasError;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.network.options.IpOptions;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.jclouds.abiquo.predicates.network.IpPredicates;
import org.jclouds.abiquo.predicates.network.NetworkPredicates;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.network.UnmanagedIpsDto;

/**
 * Live integration tests for the {@link UnmanagedNetwork} domain class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live", enabled = false)
public class UnmanagedNetworkLiveTest extends BaseAbiquoClientLiveTest
{
    private UnmanagedNetwork unmanagedNetwork;

    @BeforeClass
    public void setupNetwork()
    {
        unmanagedNetwork =
            UnmanagedNetwork.Builder.fromUnmanagedNetwork(env.unmanagedNetwork).build();
        unmanagedNetwork.setName(PREFIX + "-unmanagednetwork-test");
        unmanagedNetwork.save();

        assertNotNull(unmanagedNetwork.getId());
    }

    @AfterClass
    public void tearDownNetwork()
    {
        unmanagedNetwork.delete();
    }

    public void testListIps()
    {
        UnmanagedIpsDto ipsDto =
            context.getApiContext().getApi().getInfrastructureClient()
                .listUnmanagedIps(unmanagedNetwork.unwrap(), IpOptions.builder().limit(1).build());
        int totalIps = ipsDto.getTotalSize();

        List<UnmanagedIp> ips = unmanagedNetwork.listIps();

        assertEquals(ips.size(), totalIps);
    }

    public void testListIpsWithOptions()
    {
        List<UnmanagedIp> ips = unmanagedNetwork.listIps(IpOptions.builder().limit(5).build());
        assertEquals(ips.size(), 5);
    }

    public void testListUnusedIps()
    {
        UnmanagedIpsDto ipsDto =
            context.getApiContext().getApi().getInfrastructureClient()
                .listUnmanagedIps(unmanagedNetwork.unwrap(), IpOptions.builder().limit(1).build());
        int totalIps = ipsDto.getTotalSize();

        List<UnmanagedIp> ips = unmanagedNetwork.listUnusedIps();
        assertEquals(ips.size(), totalIps);
    }

    public void testUpdateBasicInfo()
    {
        unmanagedNetwork.setName("Unmanaged network Updated");
        unmanagedNetwork.setPrimaryDNS("8.8.8.8");
        unmanagedNetwork.setSecondaryDNS("8.8.8.8");
        unmanagedNetwork.update();

        assertEquals(unmanagedNetwork.getName(), "Unmanaged network Updated");
        assertEquals(unmanagedNetwork.getPrimaryDNS(), "8.8.8.8");
        assertEquals(unmanagedNetwork.getSecondaryDNS(), "8.8.8.8");

        // Refresh the unmanaged network
        UnmanagedNetwork en =
            env.enterprise.findUnmanagedNetwork(env.datacenter,
                NetworkPredicates.<UnmanagedIp> name(unmanagedNetwork.getName()));

        assertEquals(en.getId(), unmanagedNetwork.getId());
        assertEquals(en.getName(), "Unmanaged network Updated");
        assertEquals(en.getPrimaryDNS(), "8.8.8.8");
        assertEquals(en.getSecondaryDNS(), "8.8.8.8");
    }

    public void testUpdateReadOnlyFields()
    {
        try
        {
            unmanagedNetwork.setTag(20);
            unmanagedNetwork.setAddress("10.2.0.0");
            unmanagedNetwork.setMask(16);
            unmanagedNetwork.update();

            fail("Tag field should not be editable");
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.CONFLICT, "VLAN-19");
        }
    }

    public void testUpdateWithInvalidValues()
    {
        try
        {
            unmanagedNetwork.setMask(60);
            unmanagedNetwork.update();

            fail("Invalid mask value");
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.BAD_REQUEST, "CONSTR-MAX");
        }
    }

    public void testGetEnterprise()
    {
        assertEquals(unmanagedNetwork.getEnterprise().getId(), env.enterprise.getId());
    }

    public void testGetDatacenter()
    {
        assertEquals(unmanagedNetwork.getDatacenter().getId(), env.datacenter.getId());
    }

    public void testGetNetworkFromIp()
    {
        UnmanagedIp ip = unmanagedNetwork.findIp(IpPredicates.<UnmanagedIp> notUsed());
        UnmanagedNetwork network = ip.getNetwork();

        assertEquals(network.getId(), unmanagedNetwork.getId());
    }
}
