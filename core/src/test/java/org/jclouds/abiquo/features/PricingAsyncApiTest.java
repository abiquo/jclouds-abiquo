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

import static org.jclouds.abiquo.domain.DomainUtils.withHeader;

import java.io.IOException;
import java.lang.reflect.Method;

import org.jclouds.abiquo.domain.PricingResources;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.abiquo.server.core.pricing.CostCodeDto;
import com.abiquo.server.core.pricing.CostCodesDto;
import com.abiquo.server.core.pricing.CurrenciesDto;
import com.abiquo.server.core.pricing.CurrencyDto;
import com.abiquo.server.core.pricing.PricingTemplateDto;
import com.abiquo.server.core.pricing.PricingTemplatesDto;
import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code PricingAsyncApi}.
 * 
 * @author Ignasi Barrera
 * @author Susana Acedo
 */
@Test(groups = "unit")
public class PricingAsyncApiTest extends BaseAbiquoAsyncApiTest<PricingAsyncApi>
{

    /*********************** Currency ***********************/

    public void testListCurrencies() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = PricingAsyncApi.class.getMethod("listCurrencies");
        GeneratedHttpRequest request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/config/currencies HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + CurrenciesDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetCurrency() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = PricingAsyncApi.class.getMethod("getCurrency", Integer.class);
        GeneratedHttpRequest request = processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/config/currencies/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + CurrencyDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testCreateCurrency() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = PricingAsyncApi.class.getMethod("createCurrency", CurrencyDto.class);
        GeneratedHttpRequest request =
            processor.createRequest(method, PricingResources.currencyPost());

        assertRequestLineEquals(request, "POST http://localhost/api/config/currencies HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + CurrencyDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(PricingResources.currencyPostPayload()),
            CurrencyDto.class, CurrencyDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testUpdateCurrency() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = PricingAsyncApi.class.getMethod("updateCurrency", CurrencyDto.class);
        GeneratedHttpRequest request =
            processor.createRequest(method, PricingResources.currencyPut());

        assertRequestLineEquals(request, "PUT http://localhost/api/config/currencies/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + CurrencyDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(PricingResources.currencyPutPayload()),
            CurrencyDto.class, CurrencyDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteCurrency() throws SecurityException, NoSuchMethodException
    {
        Method method = PricingAsyncApi.class.getMethod("deleteCurrency", CurrencyDto.class);
        GeneratedHttpRequest request =
            processor.createRequest(method, PricingResources.currencyPut());

        assertRequestLineEquals(request, "DELETE http://localhost/api/config/currencies/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Cost Code ***********************/

    public void testListCostCodes() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = PricingAsyncApi.class.getMethod("listCostCodes");
        GeneratedHttpRequest request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/config/costcodes HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + CostCodesDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetCostCode() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = PricingAsyncApi.class.getMethod("getCostCode", Integer.class);
        GeneratedHttpRequest request = processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/config/costcodes/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + CostCodeDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testCreateCostCode() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = PricingAsyncApi.class.getMethod("createCostCode", CostCodeDto.class);
        GeneratedHttpRequest request =
            processor.createRequest(method, PricingResources.costcodePost());

        assertRequestLineEquals(request, "POST http://localhost/api/config/costcodes HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + CostCodeDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(PricingResources.costcodePostPayload()),
            CostCodeDto.class, CostCodeDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testUpdateCostCode() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = PricingAsyncApi.class.getMethod("updateCostCode", CostCodeDto.class);
        GeneratedHttpRequest request =
            processor.createRequest(method, PricingResources.costcodePut());

        assertRequestLineEquals(request, "PUT http://localhost/api/config/costcodes/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + CostCodeDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(PricingResources.costcodePutPayload()),
            CostCodeDto.class, CostCodeDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteCostCode() throws SecurityException, NoSuchMethodException
    {
        Method method = PricingAsyncApi.class.getMethod("deleteCostCode", CostCodeDto.class);
        GeneratedHttpRequest request =
            processor.createRequest(method, PricingResources.costcodePut());

        assertRequestLineEquals(request, "DELETE http://localhost/api/config/costcodes/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Pricing Template ***********************/

    public void testListPricingTemplates() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method = PricingAsyncApi.class.getMethod("listPricingTemplates");
        GeneratedHttpRequest request = processor.createRequest(method);

        assertRequestLineEquals(request,
            "GET http://localhost/api/config/pricingtemplates HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + PricingTemplatesDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetPricingTemplate() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method = PricingAsyncApi.class.getMethod("getPricingTemplate", Integer.class);
        GeneratedHttpRequest request = processor.createRequest(method, 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/config/pricingtemplates/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + PricingTemplateDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testCreatePricingTemplate() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            PricingAsyncApi.class.getMethod("createPricingTemplate", PricingTemplateDto.class);
        GeneratedHttpRequest request =
            processor.createRequest(method, PricingResources.pricingtemplatePost());

        assertRequestLineEquals(request,
            "POST http://localhost/api/config/pricingtemplates HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + PricingTemplateDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, withHeader(PricingResources.pricingtemplatePostPayload()),
            PricingTemplateDto.class, PricingTemplateDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testUpdatePricingTemplate() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            PricingAsyncApi.class.getMethod("updatePricingTemplate", PricingTemplateDto.class);
        GeneratedHttpRequest request =
            processor.createRequest(method, PricingResources.pricingtemplatePut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/config/pricingtemplates/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + PricingTemplateDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, withHeader(PricingResources.pricingtemplatePutPayload()),
            PricingTemplateDto.class, PricingTemplateDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeletePricingTemplate() throws SecurityException, NoSuchMethodException
    {
        Method method =
            PricingAsyncApi.class.getMethod("deletePricingTemplate", PricingTemplateDto.class);
        GeneratedHttpRequest request =
            processor.createRequest(method, PricingResources.pricingtemplatePut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/config/pricingtemplates/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<PricingAsyncApi>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<PricingAsyncApi>>()
        {
        };
    }
}
