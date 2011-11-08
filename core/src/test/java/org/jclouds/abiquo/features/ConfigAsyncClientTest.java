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

import org.jclouds.abiquo.domain.ConfigResources;
import org.jclouds.abiquo.domain.config.options.LicenseOptions;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.abiquo.server.core.config.LicenseDto;
import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code AdminAsyncClient}
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Test(groups = "unit")
public class ConfigAsyncClientTest extends BaseAbiquoAsyncClientTest<ConfigAsyncClient>
{
    /*********************** License ********************** */

    public void testListLicense() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = ConfigAsyncClient.class.getMethod("listLicenses");
        GeneratedHttpRequest<ConfigAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/config/licenses HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListLicenseOptions() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method = ConfigAsyncClient.class.getMethod("listLicenses", LicenseOptions.class);
        GeneratedHttpRequest<ConfigAsyncClient> request =
            processor.createRequest(method, LicenseOptions.builder().active(true).build());

        assertRequestLineEquals(request,
            "GET http://localhost/api/config/licenses?active=true HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testAddLicense() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = ConfigAsyncClient.class.getMethod("addLicense", LicenseDto.class);
        GeneratedHttpRequest<ConfigAsyncClient> request =
            processor.createRequest(method, ConfigResources.licensePost());

        assertRequestLineEquals(request, "POST http://localhost/api/config/licenses HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(ConfigResources.LicensePostPayload()),
            "application/xml", false);

        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testRemoveLicense() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = ConfigAsyncClient.class.getMethod("removeLicense", LicenseDto.class);
        GeneratedHttpRequest<ConfigAsyncClient> request =
            processor.createRequest(method, ConfigResources.licensePut());

        assertRequestLineEquals(request, "DELETE http://localhost/api/config/licenses/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<ConfigAsyncClient>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<ConfigAsyncClient>>()
        {
        };
    }
}
