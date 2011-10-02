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

package org.jclouds.abiquo;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Method;

import org.jclouds.abiquo.config.AbiquoRestClientModule;
import org.jclouds.abiquo.functions.ParseDatacenter;
import org.jclouds.abiquo.functions.ParseDatacenters;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.RestClientTest;
import org.jclouds.rest.RestContextSpec;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code AbiquoAsyncClient}
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class AbiquoAsyncClientTest extends RestClientTest<AbiquoAsyncClient>
{

    public void testListDatacenters() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = AbiquoAsyncClient.class.getMethod("listDatacenters");
        GeneratedHttpRequest<AbiquoAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseDatacenters.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetDatacenter() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = AbiquoAsyncClient.class.getMethod("getDatacenter", Integer.class);
        GeneratedHttpRequest<AbiquoAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseDatacenter.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    @Override
    protected void checkFilters(HttpRequest request)
    {
        assertEquals(request.getFilters().size(), 1);
        assertEquals(request.getFilters().get(0).getClass(), BasicAuthentication.class);
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<AbiquoAsyncClient>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<AbiquoAsyncClient>>()
        {
        };
    }

    @Override
    protected Module createModule()
    {
        return new AbiquoRestClientModule();
    }

    @Override
    public RestContextSpec<AbiquoClient, AbiquoAsyncClient> createContextSpec()
    {
        String identity =
            checkNotNull(System.getProperty("test.abiquo.identity"), "test.abiquo.identity");
        String credential =
            checkNotNull(System.getProperty("test.abiquo.credential"), "test.abiquo.credential");
        String endpoint =
            checkNotNull(System.getProperty("test.abiquo.endpoint"), "test.abiquo.endpoint");
        String apiVersion =
            checkNotNull(System.getProperty("test.abiquo.apiversion"), "test.abiquo.apiversion");

        return new RestContextSpec<AbiquoClient, AbiquoAsyncClient>("abiquo",
            endpoint,
            apiVersion,
            null,
            identity,
            credential,
            AbiquoClient.class,
            AbiquoAsyncClient.class);
    }
}
