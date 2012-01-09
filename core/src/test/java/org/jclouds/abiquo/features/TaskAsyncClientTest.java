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

import org.jclouds.abiquo.domain.CloudResources;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.SingleResourceTransportDto;
import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code TaskAsyncClient}
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Test(groups = "unit")
public class TaskAsyncClientTest extends BaseAbiquoAsyncClientTest<TaskAsyncClient>
{
    /*********************** Task ***********************/

    public void testGetTask() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = TaskAsyncClient.class.getMethod("getTask", RESTLink.class);
        GeneratedHttpRequest<TaskAsyncClient> request =
            processor
                .createRequest(
                    method,
                    new RESTLink("task",
                        "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1/tasks/169f1877-5f17-4f62-9563-974001295c54"));

        assertRequestLineEquals(
            request,
            "GET http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1/tasks/169f1877-5f17-4f62-9563-974001295c54 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);
        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListTasks() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            TaskAsyncClient.class.getMethod("listTasks", SingleResourceTransportDto.class);
        GeneratedHttpRequest<TaskAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualMachinePut());

        assertRequestLineEquals(
            request,
            "GET http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1/tasks HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);
        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<TaskAsyncClient>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<TaskAsyncClient>>()
        {
        };
    }
}
