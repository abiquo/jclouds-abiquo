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

package org.jclouds.abiquo.domain.enterprise;

import static org.jclouds.abiquo.util.Assert.assertHasError;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.domain.enterprise.Enterprise.Builder;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.enterprise.DatacenterLimitsDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;

/**
 * Live integration tests for the {@link Enterprise} domain class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class EnterpriseLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

    public void testUpdate()
    {
        Enterprise ent = Enterprise.builder(context).name("dummyTestUpdateRS").build();
        ent.save();
        ent.setName("Updated Enterprise");
        ent.update();

        // Recover the updated enterprise
        EnterpriseDto updated = env.enterpriseClient.getEnterprise(ent.getId());

        assertEquals(updated.getName(), "Updated Enterprise");

        ent.delete();
    }

    public void testCreateRepeated()
    {
        Enterprise repeated = Builder.fromEnterprise(env.enterprise).build();

        try
        {
            repeated.save();
            fail("Should not be able to create enterprises with the same name");
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.CONFLICT, "ENTERPRISE-4");
        }
    }

    public void testAllowProhibitDatacenter()
    {
        tearDownLimits();

        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);

        DatacenterLimitsDto limitsDto =
            env.enterpriseClient.getLimits(env.enterprise.unwrap(), env.datacenter.unwrap());
        assertNotNull(limitsDto);
    }

    public void testAllowTwiceWorks()
    {
        tearDownLimits();

        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);
        limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);
        tearDownLimits();
    }

    public void testDeleteTwiceWorks()
    {
        env.enterprise.prohibitDatacenter(env.datacenter);
        env.enterprise.prohibitDatacenter(env.datacenter);

        env.enterprise.allowDatacenter(env.datacenter);
    }

    public void testListLimits()
    {
        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);

        List<Limits> allLimits = env.enterprise.listLimits();
        assertNotNull(allLimits);
        assertEquals(allLimits.size(), 1);
    }

    public void testUpdateInvalidLimits()
    {
        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);

        // CPU soft remains to 0 => conflict because hard is smaller
        limits.setCpuCountHardLimit(2);

        try
        {
            limits.update();
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.BAD_REQUEST, "CONSTR-LIMITRANGE");
        }
    }

    public void testUpdateLimits()
    {
        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);

        limits.setCpuCountLimits(4, 5);
        limits.update();

        DatacenterLimitsDto limitsDto =
            env.enterpriseClient.getLimits(env.enterprise.unwrap(), env.datacenter.unwrap());
        assertNotNull(limitsDto);
        assertEquals(limitsDto.getCpuCountHardLimit(), 5);
        assertEquals(limitsDto.getCpuCountSoftLimit(), 4);
    }

    public void testListAllowedDatacenters()
    {
        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);

        List<Datacenter> allowed = env.enterprise.listAllowedDatacenters();

        assertNotNull(allowed);
        assertFalse(allowed.isEmpty());
        assertEquals(allowed.get(0).getId(), env.datacenter.getId());
    }

    public void testListAllowedDatacentersWithNoAllowedDatacenters()
    {
        tearDownLimits();

        List<Datacenter> allowed = env.enterprise.listAllowedDatacenters();

        assertNotNull(allowed);
        assertTrue(allowed.isEmpty());

        env.enterprise.allowDatacenter(env.datacenter);
    }

    private void tearDownLimits()
    {
        // Cleanup with the prohibe method
        env.enterprise.prohibitDatacenter(env.datacenter);
        DatacenterLimitsDto limitsDto =
            env.enterpriseClient.getLimits(env.enterprise.unwrap(), env.datacenter.unwrap());
        assertNotNull(limitsDto);
    }
}
