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

package org.jclouds.abiquo.domain.config;

import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.jclouds.abiquo.util.Assert.assertHasError;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.domain.config.CostCode.Builder;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.internal.BaseAbiquoApiLiveTest;
import org.jclouds.abiquo.predicates.config.CostCodePredicates;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Live integration tests for the {@link CostCode} domain class.
 * 
 * @author Susana Acedo
 */
@Test(groups = "live")
public class CostCodeLiveTest extends BaseAbiquoApiLiveTest
{
    CostCode costcode;

    @BeforeClass
    public void setupCostCode()
    {
        costcode =
            CostCode.builder(env.context.getApiContext()).name(PREFIX + "test-costcode")
                .description("description").build();
        costcode.save();
    }

    @AfterClass
    public void tearDownCostCode()
    {
        costcode.delete();
    }

    public void testCreateRepeated()
    {
        CostCode repeated = Builder.fromCostCode(costcode).build();

        try
        {
            repeated.save();
            fail("Should not be able to create costcodes with the same name");
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.CONFLICT, "COSTCODE-2");
        }
    }

    public void testUpdate()
    {
        costcode.setName(PREFIX + "costcode-updated");
        costcode.update();

        CostCode apiCostCode =
            env.context.getPricingService().findCostCode(
                CostCodePredicates.name(PREFIX + "costcode-updated"));

        assertNotNull(apiCostCode);
        assertEquals(PREFIX + "costcode-updated", apiCostCode.getName());

    }
}
