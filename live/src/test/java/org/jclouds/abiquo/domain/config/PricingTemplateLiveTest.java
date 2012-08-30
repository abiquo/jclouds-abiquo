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

import java.math.BigDecimal;
import java.util.Date;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.domain.config.PricingTemplate.Builder;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.internal.BaseAbiquoApiLiveTest;
import org.jclouds.abiquo.predicates.config.PricingTemplatePredicates;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.abiquo.model.enumerator.PricingPeriod;

/**
 * Live integration tests for the {@link PricingTemplate} domain class.
 * 
 * @author Susana Acedo
 */
@Test(groups = "live")
public class PricingTemplateLiveTest extends BaseAbiquoApiLiveTest
{
    PricingTemplate pricingTemplate;

    Currency currency;

    BigDecimal zero = new BigDecimal(0);

    @BeforeClass
    public void setupPricingTemplate()
    {
        Iterable<Currency> currencies = env.context.getPricingService().listCurrencies();
        currency = currencies.iterator().next();

        pricingTemplate =
            PricingTemplate.builder(env.context.getApiContext(), currency).name("pricing_template")
                .description("description").hdGB(zero).standingChargePeriod(zero).vlan(zero)
                .chargingPeriod(PricingPeriod.MONTH).minimumChargePeriod(zero)
                .showChangesBefore(true).showMinimumCharge(false).minimumCharge(PricingPeriod.WEEK)
                .publicIp(zero).vcpu(zero).memoryGB(zero).defaultTemplate(true)
                .lastUpdate(new Date()).build();

        pricingTemplate.save();
    }

    @AfterClass
    public void tearDownPricingTemplate()
    {
        pricingTemplate.delete();
    }

    public void testCreateRepeated()
    {
        PricingTemplate repeated = Builder.fromPricingTemplate(pricingTemplate).build();

        try
        {
            repeated.save();
            fail("Should not be able to create pricingtemplates with the same name");
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.CONFLICT, "PRICINGTEMPLATE-2");
        }
    }

    public void testUpdate()
    {
        pricingTemplate.setName(PREFIX + "pt-updated");
        pricingTemplate.update();

        PricingTemplate apiPricingTemplate =
            env.context.getPricingService().findPricingTemplate(
                PricingTemplatePredicates.name(PREFIX + "pt-updated"));

        assertNotNull(apiPricingTemplate);
        assertEquals(PREFIX + "pt-updated", apiPricingTemplate.getName());

    }
}
