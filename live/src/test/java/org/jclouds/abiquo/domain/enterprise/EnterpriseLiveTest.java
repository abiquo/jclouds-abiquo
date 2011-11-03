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

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Enterprise.Builder;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.environment.EnterpriseTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;

/**
 * Live integration tests for the {@link Enterprise} domain class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class EnterpriseLiveTest extends BaseAbiquoClientLiveTest<EnterpriseTestEnvironment>
{

    @Override
    protected EnterpriseTestEnvironment environment(final AbiquoContext context)
    {
        return new EnterpriseTestEnvironment(context);
    }

    public void testUpdate()
    {
        env.enterprise.setName("Updated Enterprise");
        env.enterprise.update();

        // Recover the updated enterprise
        EnterpriseDto updated = env.enterpriseClient.getEnterprise(env.enterprise.getId());

        assertEquals(updated.getName(), "Updated Enterprise");
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
        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);

        DatacentersLimitsDto limitsDto =
            env.enterpriseClient.getLimits(env.enterprise.unwrap(), env.datacenter.unwrap());
        assertNotNull(limitsDto);
        assertFalse(limitsDto.isEmpty());

        tearDownLimits();
    }

    public void testAllowTwiceFails()
    {
        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);

        try
        {
            env.enterprise.allowDatacenter(env.datacenter);
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.CONFLICT, "LIMIT-7");
        }

        tearDownLimits();
    }

    public void testListLimits()
    {
        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);

        List<Limits> allLimits = env.enterprise.listLimits();
        assertNotNull(allLimits);
        assertEquals(allLimits.size(), 1);

        tearDownLimits();
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

        tearDownLimits();
    }

    public void testUpdateLimits()
    {
        Limits limits = env.enterprise.allowDatacenter(env.datacenter);
        assertNotNull(limits);

        limits.setCpuCountLimits(4, 5);
        limits.update();

        DatacentersLimitsDto limitsDto =
            env.enterpriseClient.getLimits(env.enterprise.unwrap(), env.datacenter.unwrap());
        assertNotNull(limitsDto);
        assertEquals(limitsDto.getCollection().get(0).getCpuCountHardLimit(), 5);
        assertEquals(limitsDto.getCollection().get(0).getCpuCountSoftLimit(), 4);

        tearDownLimits();
    }

    private void tearDownLimits()
    {
        // Cleanup with the prohibe method
        env.enterprise.prohibitDatacenter(env.datacenter);
        DatacentersLimitsDto limitsDto =
            env.enterpriseClient.getLimits(env.enterprise.unwrap(), env.datacenter.unwrap());
        assertNotNull(limitsDto);
        assertTrue(limitsDto.isEmpty());
    }
}
