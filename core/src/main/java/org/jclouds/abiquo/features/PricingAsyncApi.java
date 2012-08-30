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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.binders.BindToXMLPayloadAndPath;
import org.jclouds.abiquo.http.filters.AbiquoAuthentication;
import org.jclouds.abiquo.http.filters.AppendApiVersionToMediaType;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.server.core.pricing.CostCodeDto;
import com.abiquo.server.core.pricing.CostCodesDto;
import com.abiquo.server.core.pricing.CurrenciesDto;
import com.abiquo.server.core.pricing.CurrencyDto;
import com.abiquo.server.core.pricing.PricingTemplateDto;
import com.abiquo.server.core.pricing.PricingTemplatesDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides synchronous access to Abiquo Pricing API.
 * 
 * @see API: <a href="http://community.abiquo.com/display/ABI20/API+Reference">
 *      http://community.abiquo.com/display/ABI20/API+Reference</a>
 * @see PricingAsyncApi
 * @author Ignasi Barrera
 * @author Susana Acedo
 */
@RequestFilters({AbiquoAuthentication.class, AppendApiVersionToMediaType.class})
@Path("/config")
public interface PricingAsyncApi
{
    /*********************** Currency ********************** */

    /**
     * @see PricingApi#listCurrencies()
     */
    @GET
    @Path("/currencies")
    @Consumes(CurrenciesDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<CurrenciesDto> listCurrencies();

    /**
     * @see PricingApi#getCurrency(Integer)
     */
    @GET
    @Path("/currencies/{currency}")
    @Consumes(CurrencyDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<CurrencyDto> getCurrency(@PathParam("currency") Integer currencyId);

    /**
     * @see PricingApi#createCurrency(CurrencyDto)
     */
    @POST
    @Path("/currencies")
    @Produces(CurrencyDto.BASE_MEDIA_TYPE)
    @Consumes(CurrencyDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<CurrencyDto> createCurrency(
        @BinderParam(BindToXMLPayload.class) CurrencyDto currency);

    /**
     * @see PricingApi#updateCurrency(CurrencyDto)
     */
    @PUT
    @Produces(CurrencyDto.BASE_MEDIA_TYPE)
    @Consumes(CurrencyDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<CurrencyDto> updateCurrency(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) CurrencyDto currency);

    /**
     * @see PricingApi#deleteCurrency(CurrencyDto)
     */
    @DELETE
    ListenableFuture<Void> deleteCurrency(
        @EndpointLink("edit") @BinderParam(BindToPath.class) CurrencyDto currency);

    /*********************** CostCode ********************** */

    /**
     * @see PricingApi#listCostCodes()
     */
    @GET
    @Path("/costcodes")
    @Consumes(CostCodesDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<CostCodesDto> listCostCodes();

    /**
     * @see PricingApi#getCostCode(Integer)
     */
    @GET
    @Path("/costcodes/{costcode}")
    @Consumes(CostCodeDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<CostCodeDto> getCostCode(@PathParam("costcode") Integer costcodeId);

    /**
     * @see PricingApi#createCostCode(CostCodeDto)
     */
    @POST
    @Path("/costcodes")
    @Produces(CostCodeDto.BASE_MEDIA_TYPE)
    @Consumes(CostCodeDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<CostCodeDto> createCostCode(
        @BinderParam(BindToXMLPayload.class) CostCodeDto costcode);

    /**
     * @see PricingApi#updateCostCode(CostCodeDto)
     */
    @PUT
    @Produces(CostCodeDto.BASE_MEDIA_TYPE)
    @Consumes(CostCodeDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<CostCodeDto> updateCostCode(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) CostCodeDto costcode);

    /**
     * @see PricingApi#deleteCostCode(CostCodeDto)
     */
    @DELETE
    ListenableFuture<Void> deleteCostCode(
        @EndpointLink("edit") @BinderParam(BindToPath.class) CostCodeDto costcode);

    /*********************** PricingTemplate ********************** */

    /**
     * @see PricingApi#listPricingTemplates()
     */
    @GET
    @Path("/pricingtemplates")
    @Consumes(PricingTemplatesDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<PricingTemplatesDto> listPricingTemplates();

    /**
     * @see PricingApi#getPricingTemplate(Integer)
     */
    @GET
    @Path("/pricingtemplates/{pricingtemplate}")
    @Consumes(PricingTemplateDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<PricingTemplateDto> getPricingTemplate(
        @PathParam("pricingtemplate") Integer pricingTemplateId);

    /**
     * @see PricingApi#createPricingTemplate(PricingTemplateDto)
     */
    @POST
    @Path("/pricingtemplates")
    @Produces(PricingTemplateDto.BASE_MEDIA_TYPE)
    @Consumes(PricingTemplateDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<PricingTemplateDto> createPricingTemplate(
        @BinderParam(BindToXMLPayload.class) PricingTemplateDto pricingtemplate);

    /**
     * @see PricingApi#updatePricingTemplate(PricingTemplateDto)
     */
    @PUT
    @Produces(PricingTemplateDto.BASE_MEDIA_TYPE)
    @Consumes(PricingTemplateDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<PricingTemplateDto> updatePricingTemplate(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) PricingTemplateDto pricingtemplate);

    /**
     * @see PricingApi#deletePricingTemplate(PricingTemplateDto)
     */
    @DELETE
    ListenableFuture<Void> deletePricingTemplate(
        @EndpointLink("edit") @BinderParam(BindToPath.class) PricingTemplateDto pricingtemplate);

}
