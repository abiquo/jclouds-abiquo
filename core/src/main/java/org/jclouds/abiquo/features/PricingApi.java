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

import java.util.concurrent.TimeUnit;

import org.jclouds.concurrent.Timeout;

import com.abiquo.server.core.pricing.CostCodeDto;
import com.abiquo.server.core.pricing.CostCodesDto;
import com.abiquo.server.core.pricing.CurrenciesDto;
import com.abiquo.server.core.pricing.CurrencyDto;
import com.abiquo.server.core.pricing.PricingTemplateDto;
import com.abiquo.server.core.pricing.PricingTemplatesDto;

/**
 * Provides synchronous access to Abiquo Pricing API.
 * 
 * @see API: <a href="http://community.abiquo.com/display/ABI20/API+Reference">
 *      http://community.abiquo.com/display/ABI20/API+Reference</a>
 * @see PricingAsyncApi
 * @author Ignasi Barrera
 * @author Susana Acedo
 */
@Timeout(duration = 30, timeUnit = TimeUnit.SECONDS)
public interface PricingApi
{

    /*********************** Currency ********************** */

    /**
     * List all currencies
     * 
     * @return The list of currencies
     */
    CurrenciesDto listCurrencies();

    /**
     * Get the given currency
     * 
     * @param currencyId The id of the currency
     * @return The currency
     */
    CurrencyDto getCurrency(Integer currencyId);

    /**
     * Create a new currency
     * 
     * @param currency The currency to be created.
     * @return The created currency.
     */
    CurrencyDto createCurrency(CurrencyDto currency);

    /**
     * Updates an existing currency
     * 
     * @param currency The new attributes for the currency
     * @return The updated currency
     */
    CurrencyDto updateCurrency(final CurrencyDto currency);

    /**
     * Deletes an existing currency
     * 
     * @param currency The currency to delete
     */
    void deleteCurrency(final CurrencyDto currency);

    /*********************** CostCode ********************** */

    /**
     * List all costcodes
     * 
     * @return The list of costcodes
     */
    CostCodesDto listCostCodes();

    /**
     * Get the given costcode
     * 
     * @param costcodeId The id of the costcode
     * @return The costcode
     */
    CostCodeDto getCostCode(Integer costcodeId);

    /**
     * Create a new costcode
     * 
     * @param costcode The costcode to be created.
     * @return The created costcode.
     */
    CostCodeDto createCostCode(CostCodeDto costcode);

    /**
     * Updates an existing costcode
     * 
     * @param costcode The new attributes for the costcode
     * @return The updated costcode
     */
    CostCodeDto updateCostCode(CostCodeDto costcode);

    /**
     * Deletes an existing costcode
     * 
     * @param currency The costcode to delete
     */
    void deleteCostCode(CostCodeDto costcode);

    /*********************** PricingTemplate ********************** */

    /**
     * List all pricingtemplates
     * 
     * @return The list of pricingtemplates
     */
    PricingTemplatesDto listPricingTemplates();

    /**
     * Get the given pricingtemplate
     * 
     * @param pricingTemplateId The id of the pricingtemplate
     * @return The pricingtemplate
     */
    PricingTemplateDto getPricingTemplate(Integer pricingTemplateId);

    /**
     * Create a new pricing template
     * 
     * @param pricingtemplate The pricingtemplate to be created
     * @return The created pricingtemplate
     */
    PricingTemplateDto createPricingTemplate(PricingTemplateDto pricingtemplate);

    /**
     * Updates an existing pricing template
     * 
     * @param pricingtemplate The new attributes for the pricingtemplate
     * @return The updated pricingtemplate
     */
    PricingTemplateDto updatePricingTemplate(PricingTemplateDto pricingtemplate);

    /**
     * Deletes an existing pricingtemplate
     * 
     * @param pricingtemplate The pricingtemplate to delete
     */
    void deletePricingTemplate(PricingTemplateDto pricingtemplate);

}
