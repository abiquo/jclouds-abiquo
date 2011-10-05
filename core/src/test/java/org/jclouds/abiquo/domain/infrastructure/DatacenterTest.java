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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.UUID;

import org.jclouds.abiquo.domain.infrastructure.Datacenter.Builder;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Unit tests for the {@link Datacenter} domain class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class DatacenterTest extends BaseAbiquoClientLiveTest
{
    /** The domain object to test. */
    private Datacenter datacenter;

    @Override
    protected void setup() throws Exception
    {
        datacenter = Datacenter.builder(context).name(randomName()).location("Honolulu").build();
        datacenter.save();

        assertNotNull(datacenter.getId());
    }

    @Override
    protected void tearDown() throws Exception
    {
        Integer idDatacenter = datacenter.getId();
        datacenter.delete();

        assertNull(infrastructureClient.getDatacenter(idDatacenter));
    }

    public void testUpdate()
    {
        datacenter.setLocation("New York");
        datacenter.update();

        // Recover the updated datacenter
        DatacenterDto updated = infrastructureClient.getDatacenter(datacenter.getId());

        assertEquals(updated.getLocation(), "New York");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testCreateRepeated()
    {
        Datacenter repeated = Builder.fromDatacenter(datacenter).build();
        repeated.save();
    }

    private static String randomName()
    {
        return UUID.randomUUID().toString().substring(0, 15);
    }

}
