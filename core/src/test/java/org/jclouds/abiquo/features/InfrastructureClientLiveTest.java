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

package org.jclouds.abiquo.features;

import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.Random;

import org.jclouds.abiquo.domain.Infrastructure;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.DatacentersDto;

/**
 * Tests behavior of {@code AbiquoClient}
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class InfrastructureClientLiveTest extends BaseAbiquoClientLiveTest
{
    private DatacenterDto datacenter;

    @Override
    protected void setupEntities() throws Exception
    {
        datacenter = createDatacenter();
    }

    @Override
    protected void teardownEntities() throws Exception
    {
        deleteDatacenter();
    }

    public void testListDatacenters() throws Exception
    {
        DatacentersDto datacenters = infrastructureClient.listDatacenters();
        assertNotNull(datacenters);
        assertFalse(datacenters.getCollection().isEmpty());
    }

    public void testGetDatacenter() throws Exception
    {
        DatacenterDto dc = infrastructureClient.getDatacenter(datacenter.getId());
        assertNotNull(dc);
    }

    public void testGetUnexistingDatacenter() throws Exception
    {
        DatacenterDto dc = infrastructureClient.getDatacenter(datacenter.getId() + 100);
        assertNull(dc);
    }

    public void testUpdateDatacenter() throws Exception
    {
        datacenter.setLocation("Anotherone");
        DatacenterDto updated =
            infrastructureClient.updateDatacenter(datacenter.getId(), datacenter);
        assertNotNull(updated);
        assertEquals(updated.getLocation(), "Anotherone");
    }

    private DatacenterDto createDatacenter() throws Exception
    {
        Random generator = new Random(System.currentTimeMillis());
        DatacenterDto datacenter = Infrastructure.datacenterPost();
        datacenter.setName(PREFIX + datacenter.getName() + generator.nextInt(100));
        DatacenterDto created = infrastructureClient.createDatacenter(datacenter);
        assertNotNull(created);
        assertNotNull(created.getId());
        return created;
    }

    private void deleteDatacenter() throws Exception
    {
        infrastructureClient.deleteDatacenter(datacenter.getId());
    }

}
