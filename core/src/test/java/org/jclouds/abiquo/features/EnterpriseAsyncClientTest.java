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

import org.jclouds.abiquo.domain.EnterpriseResources;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code EnterpriseAsyncClient}
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Test(groups = "unit")
public class EnterpriseAsyncClientTest extends BaseAbiquoAsyncClientTest<EnterpriseAsyncClient>
{
    // Enterprise

    public void testListEnterprises() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = EnterpriseAsyncClient.class.getMethod("listEnterprises");
        GeneratedHttpRequest<EnterpriseAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/enterprises HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateEnterprise() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("createEnterprise", EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePost());

        assertRequestLineEquals(request, "POST http://localhost/api/admin/enterprises HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(EnterpriseResources.enterprisePostPayload()),
            "application/xml", false);

        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetEnterprise() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = EnterpriseAsyncClient.class.getMethod("getEnterprise", Integer.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/enterprises/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateEnterprise() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("updateEnterprise", EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut());

        assertRequestLineEquals(request, "PUT http://localhost/api/admin/enterprises/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(EnterpriseResources.enterprisePutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteEnterprise() throws SecurityException, NoSuchMethodException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("deleteEnterprise", EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut());

        assertRequestLineEquals(request, "DELETE http://localhost/api/admin/enterprises/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<EnterpriseAsyncClient>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<EnterpriseAsyncClient>>()
        {
        };
    }
}
