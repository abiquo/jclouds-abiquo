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
import org.jclouds.http.HttpRequest;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.http.functions.ReturnStringIf2xx;
import org.jclouds.rest.RestClientTest;
import org.jclouds.rest.RestContextSpec;
import org.jclouds.rest.functions.ReturnEmptySetOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnVoidOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.google.common.collect.Iterables;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code AbiquoAsyncClient}
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit", testName = "abiquo.AbiquoAsyncClientTest")
public class AbiquoAsyncClientTest extends RestClientTest<AbiquoAsyncClient>
{

    public void testList() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = AbiquoAsyncClient.class.getMethod("list");
        GeneratedHttpRequest<AbiquoAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/items HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: text/plain\n");
        assertPayloadEquals(request, null, null, false);

        // now make sure request filters apply by replaying
        Iterables.getOnlyElement(request.getFilters()).filter(request);
        Iterables.getOnlyElement(request.getFilters()).filter(request);

        assertRequestLineEquals(request, "GET http://localhost/api/items HTTP/1.1");
        // for example, using basic authentication, we should get "only one" header
        assertNonPayloadHeadersEqual(request,
            "Accept: text/plain\nAuthorization: Basic aWRlbnRpdHk6Y3JlZGVudGlhbA==\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReturnStringIf2xx.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnEmptySetOnNotFoundOr404.class);

        checkFilters(request);

    }

    public void testGet() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = AbiquoAsyncClient.class.getMethod("get", long.class);
        GeneratedHttpRequest<AbiquoAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/items/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: text/plain\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReturnStringIf2xx.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);

    }

    public void testDelete() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = AbiquoAsyncClient.class.getMethod("delete", long.class);
        GeneratedHttpRequest<AbiquoAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request, "DELETE http://localhost/api/items/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnVoidOnNotFoundOr404.class);

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
