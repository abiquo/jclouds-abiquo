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

import java.io.IOException;
import java.lang.reflect.Method;

import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code VirtualMachineTemplateAsyncClient}
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Test(groups = "unit")
public class VirtualMachineTemplateAsyncClientTest extends
    BaseAbiquoAsyncClientTest<VirtualMachineTemplateAsyncClient>
{
    /*********************** Virtual Machine Template ***********************/

    public void testListVirtualMachineTemplates() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            VirtualMachineTemplateAsyncClient.class.getMethod("listVirtualMachineTemplates",
                Integer.class, Integer.class);
        GeneratedHttpRequest<VirtualMachineTemplateAsyncClient> request =
            processor.createRequest(method, 1, 1);

        assertRequestLineEquals(
            request,
            "GET http://localhost/api/admin/enterprises/1/datacenterrepositories/1/virtualmachinetemplates HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetVirtualMachineTemplate() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            VirtualMachineTemplateAsyncClient.class.getMethod("getVirtualMachineTemplate",
                Integer.class, Integer.class, Integer.class);
        GeneratedHttpRequest<VirtualMachineTemplateAsyncClient> request =
            processor.createRequest(method, 1, 1, 1);

        assertRequestLineEquals(
            request,
            "GET http://localhost/api/admin/enterprises/1/datacenterrepositories/1/virtualmachinetemplates/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<VirtualMachineTemplateAsyncClient>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<VirtualMachineTemplateAsyncClient>>()
        {
        };
    }
}
