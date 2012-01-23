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

package org.jclouds.abiquo.domain.infrastructure;

import static org.jclouds.abiquo.predicates.infrastructure.RemoteServicePredicates.type;
import static org.jclouds.abiquo.util.Assert.assertHasError;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.infrastructure.RemoteService.Builder;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.jclouds.abiquo.reference.AbiquoEdition;
import org.testng.annotations.Test;

import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;
import com.google.common.collect.Iterables;

/**
 * Live integration tests for the {@link RemoteService} domain class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class RemoteServiceLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{
    public void testUpdate()
    {
        // Create a new datacenter
        Datacenter dc =
            Datacenter.builder(context).remoteServices("8.8.8.8", AbiquoEdition.ENTERPRISE).name(
                "dummyTestUpdateRS").location("dummyland").build();
        dc.save();

        // Update the remote service
        RemoteService rs = dc.findRemoteService(type(RemoteServiceType.TARANTINO));
        rs.setUri("http://testuri");
        rs.update();

        // Recover the updated remote service
        RemoteServiceDto updated =
            env.infrastructureClient.getRemoteService(dc.unwrap(), RemoteServiceType.TARANTINO);

        assertEquals(updated.getUri(), rs.getUri());

        // Delete new datacenter to preserve environment state
        dc.delete();
    }

    public void testDelete()
    {
        // Create a new datacenter
        Datacenter dc =
            Datacenter.builder(context).remoteServices("9.9.9.9", AbiquoEdition.ENTERPRISE).name(
                "dummyTestDeleteRS").location("dummyland").build();
        dc.save();

        RemoteService rs = dc.findRemoteService(type(RemoteServiceType.BPM_SERVICE));
        rs.delete();

        // Recover the deleted remote service
        RemoteServiceDto deleted =
            env.infrastructureClient.getRemoteService(dc.unwrap(), RemoteServiceType.BPM_SERVICE);

        assertNull(deleted);

        // Delete new datacenter to preserve environment state
        dc.delete();
    }

    public void testIsAvailableNonCheckeable()
    {
        RemoteService rs = env.datacenter.findRemoteService(type(RemoteServiceType.DHCP_SERVICE));
        assertTrue(rs.isAvailable());
    }

    public void testIsAvailable()
    {
        RemoteService rs = env.datacenter.findRemoteService(type(RemoteServiceType.NODE_COLLECTOR));
        assertTrue(rs.isAvailable());
    }

    public void testCreateRepeated()
    {
        RemoteService repeated = Builder.fromRemoteService(env.remoteServices.get(1)).build();

        try
        {
            repeated.save();
            fail("Should not be able to create duplicated remote services in the datacenter");
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.CONFLICT, "RS-6");
        }
    }

    public void testListRemoteServices()
    {
        Iterable<RemoteService> remoteServices = env.datacenter.listRemoteServices();
        assertEquals(Iterables.size(remoteServices), env.remoteServices.size());

        remoteServices = env.datacenter.listRemoteServices(type(RemoteServiceType.NODE_COLLECTOR));
        assertEquals(Iterables.size(remoteServices), 1);
    }

    public void testFindRemoteService()
    {
        RemoteService remoteService =
            env.datacenter.findRemoteService(type(RemoteServiceType.NODE_COLLECTOR));
        assertNotNull(remoteService);
    }

}
