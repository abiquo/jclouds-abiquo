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
import org.jclouds.abiquo.domain.config.options.PropertyOptions;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.abiquo.server.core.config.LicenseDto;
import com.abiquo.server.core.config.SystemPropertyDto;
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
    /*********************** License ***********************/

    public void testListLicenses() throws SecurityException, NoSuchMethodException, IOException
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

    public void testListLicenseWithOptions() throws SecurityException, NoSuchMethodException,
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
        assertPayloadEquals(request, withHeader(ConfigResources.licensePostPayload()),
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

    /*********************** Privilege ***********************/

    public void testListPrivileges() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = ConfigAsyncClient.class.getMethod("listPrivileges");
        GeneratedHttpRequest<ConfigAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/config/privileges HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetPrivilege() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = ConfigAsyncClient.class.getMethod("getPrivilege", Integer.class);
        GeneratedHttpRequest<ConfigAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/config/privileges/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    /*********************** System Properties ***********************/

    public void testListProperties() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = ConfigAsyncClient.class.getMethod("listSystemProperties");
        GeneratedHttpRequest<ConfigAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/config/properties HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListPropertiesWithOptions() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            ConfigAsyncClient.class.getMethod("listSystemProperties", PropertyOptions.class);
        GeneratedHttpRequest<ConfigAsyncClient> request =
            processor.createRequest(method, PropertyOptions.builder().component("client").build());

        assertRequestLineEquals(request,
            "GET http://localhost/api/config/properties?component=client HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetProperty() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = ConfigAsyncClient.class.getMethod("getSystemProperty", Integer.class);
        GeneratedHttpRequest<ConfigAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/config/properties/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateSystemProperty() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            ConfigAsyncClient.class.getMethod("updateSystemProperty", SystemPropertyDto.class);
        GeneratedHttpRequest<ConfigAsyncClient> request =
            processor.createRequest(method, ConfigResources.propertyPut());

        assertRequestLineEquals(request, "PUT http://localhost/api/config/properties/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(ConfigResources.propertyPutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Icon ***********************/

    public void testListIcons() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = ConfigAsyncClient.class.getMethod("listIcons");
        GeneratedHttpRequest<ConfigAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/config/icons HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
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
