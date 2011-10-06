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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.UUID;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.Rack;
import org.jclouds.abiquo.features.InfrastructureClient;

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

    public Rack rack;

    public InfrastructureTestEnvironment(final AbiquoContext context)
    {
        super();
        this.context = context;
        this.infrastructure = context.getApi().getInfrastructureClient();
    }

    @Override
    public void setup() throws Exception
    {
        datacenter = Datacenter.builder(context).name(randomName()).location("Honolulu").build();
        datacenter.save();
        assertNotNull(datacenter.getId());

        rack =
            Rack.builder(context).datacenter(datacenter).name("Aloha")
                .shortDescription("A hawaian rack").haEnabled(false).vlanIdMin(6).vlanIdMax(3024)
                .vlanPerVdcExpected(6).build();
        rack.save();

        assertNotNull(rack.getId());
    }

    @Override
    public void tearDown() throws Exception
    {
        Integer idRack = rack.getId();
        Integer idDatacenter = datacenter.getId();

        rack.delete();
        assertNull(infrastructure.getRack(datacenter.unwrap(), idRack));

        datacenter.delete();
        assertNull(infrastructure.getDatacenter(idDatacenter));
    }

    private static String randomName()
    {
        return UUID.randomUUID().toString().substring(0, 15);
    }
}
