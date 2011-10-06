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

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.infrastructure.Datacenter.Builder;
import org.jclouds.abiquo.environment.InfrastructureTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Unit tests for the {@link Datacenter} domain class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class DatacenterTest extends BaseAbiquoClientLiveTest<InfrastructureTestEnvironment>
{

    @Override
    protected InfrastructureTestEnvironment environment(final AbiquoContext context)
    {
        return new InfrastructureTestEnvironment(context);
    }

    public void testUpdate()
    {
        env.datacenter.setLocation("New York");
        env.datacenter.update();

        // Recover the updated datacenter
        DatacenterDto updated = env.infrastructure.getDatacenter(env.datacenter.getId());

        assertEquals(updated.getLocation(), "New York");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testCreateRepeated()
    {
        Datacenter repeated = Builder.fromDatacenter(env.datacenter).build();
        repeated.save();
    }

}
