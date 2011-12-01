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

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.cloud.VirtualApplianceDto;

/**
 * Live integration tests for the {@link VirtualAppliance} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class VirtualApplianceLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

    @Override
    protected CloudTestEnvironment environment(final AbiquoContext context)
    {
        return new CloudTestEnvironment(context);
    }

    public void testUpdate()
    {
        env.virtualAppliance.setName("Virtual AppAloha updated");
        env.virtualAppliance.update();

        // Recover the updated virtual datacenter
        VirtualApplianceDto updated =
            env.cloudClient.getVirtualAppliance(env.virtualDatacenter.unwrap(),
                env.virtualAppliance.getId());

        assertEquals(updated.getName(), "Virtual AppAloha updated");
    }

    public void testCreateRepeated()
    {
        VirtualAppliance repeated =
            VirtualAppliance.Builder.fromVirtualAppliance(env.virtualAppliance).build();

        repeated.save();

        List<VirtualApplianceDto> virtualAppliances =
            env.cloudClient.listVirtualAppliances(env.virtualDatacenter.unwrap()).getCollection();

        assertEquals(virtualAppliances.size(), 2);
        assertEquals(virtualAppliances.get(0).getName(), virtualAppliances.get(1).getName());
        repeated.delete();
    }
}
