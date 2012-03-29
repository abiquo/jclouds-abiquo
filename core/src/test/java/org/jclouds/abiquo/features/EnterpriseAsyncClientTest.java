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
import org.jclouds.abiquo.domain.InfrastructureResources;
import org.jclouds.abiquo.domain.enterprise.options.EnterpriseOptions;
import org.jclouds.abiquo.domain.options.search.reference.OrderBy;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.rest.functions.MapHttp4xxCodesToExceptions;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.abiquo.appliancemanager.transport.TemplatesStateDto;
import com.abiquo.server.core.appslibrary.TemplateDefinitionListDto;
import com.abiquo.server.core.appslibrary.TemplateDefinitionListsDto;
import com.abiquo.server.core.cloud.VirtualDatacentersDto;
import com.abiquo.server.core.cloud.VirtualMachinesDto;
import com.abiquo.server.core.enterprise.DatacenterLimitsDto;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.enterprise.EnterprisePropertiesDto;
import com.abiquo.server.core.enterprise.EnterprisesDto;
import com.abiquo.server.core.enterprise.UserDto;
import com.abiquo.server.core.enterprise.UsersDto;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.DatacentersDto;
import com.abiquo.server.core.infrastructure.MachinesDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworksDto;
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
    /*********************** Enterprise ********************** */

    public void testListEnterprises() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = EnterpriseAsyncClient.class.getMethod("listEnterprises");
        GeneratedHttpRequest<EnterpriseAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/enterprises HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + EnterprisesDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListEnterprisesWithOptions() throws SecurityException, NoSuchMethodException,
        IOException
    {
        EnterpriseOptions options =
            EnterpriseOptions.builder().has("abi").orderBy(OrderBy.NAME).ascendant(true).build();

        Method method =
            EnterpriseAsyncClient.class.getMethod("listEnterprises", EnterpriseOptions.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, options);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises?has=abi&by=name&asc=true HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + EnterprisesDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListEnterprisesByDatacenter() throws SecurityException, NoSuchMethodException,
        IOException
    {
        EnterpriseOptions options =
            EnterpriseOptions.builder().startWith(0).limit(25).network(true).build();

        Method method =
            EnterpriseAsyncClient.class.getMethod("listEnterprises", DatacenterDto.class,
                EnterpriseOptions.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(), options);

        assertRequestLineEquals(
            request,
            "GET http://localhost/api/admin/datacenters/1/action/enterprises?network=true&startwith=0&limit=25 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + EnterprisesDto.BASE_MEDIA_TYPE + "\n");
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
        assertNonPayloadHeadersEqual(request, "Accept: " + EnterpriseDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(EnterpriseResources.enterprisePostPayload()),
            EnterpriseDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetEnterprise() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = EnterpriseAsyncClient.class.getMethod("getEnterprise", Integer.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/enterprises/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + EnterpriseDto.BASE_MEDIA_TYPE + "\n");
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
        assertNonPayloadHeadersEqual(request, "Accept: " + EnterpriseDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(EnterpriseResources.enterprisePutPayload()),
            EnterpriseDto.BASE_MEDIA_TYPE, false);

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
        assertNonPayloadHeadersEqual(request, "");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListAllowedDatacenters() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("listAllowedDatacenters", Integer.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters?idEnterprise=1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + DatacentersDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListVirtualDatacentersFromEnterprise() throws SecurityException,
        NoSuchMethodException, IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("listVirtualDatacenters", EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/action/virtualdatacenters HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + VirtualDatacentersDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Enterprise Properties ********************** */

    public void testGetEnterpriseProperties() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("getEnterpriseProperties", Integer.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/properties HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + EnterprisePropertiesDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, MapHttp4xxCodesToExceptions.class);

        checkFilters(request);
    }

    public void testUpdateEnterpriseProperties() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("updateEnterpriseProperties",
                EnterprisePropertiesDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePropertiesPut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/enterprises/1/properties HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + EnterprisePropertiesDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request,
            withHeader(EnterpriseResources.enterprisePropertiesPutPayload()),
            EnterprisePropertiesDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Enterprise Limits ********************** */

    public void testCreateLimits() throws SecurityException, NoSuchMethodException, IOException
    {
        EnterpriseDto enterprise = EnterpriseResources.enterprisePut();
        DatacenterDto datacenter = InfrastructureResources.datacenterPut();
        DatacenterLimitsDto limits = EnterpriseResources.datacenterLimitsPost();

        Method method =
            EnterpriseAsyncClient.class.getMethod("createLimits", EnterpriseDto.class,
                DatacenterDto.class, DatacenterLimitsDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, enterprise, datacenter, limits);

        String limitsUri = enterprise.searchLink("limits").getHref();
        String requestURI =
            String.format("POST %s?datacenter=%d HTTP/1.1", limitsUri, datacenter.getId());

        assertRequestLineEquals(request, requestURI);
        assertNonPayloadHeadersEqual(request, "Accept: " + DatacenterLimitsDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, withHeader(EnterpriseResources.datacenterLimitsPostPayload()),
            DatacenterLimitsDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetLimits() throws SecurityException, NoSuchMethodException, IOException
    {
        EnterpriseDto enterprise = EnterpriseResources.enterprisePut();
        DatacenterDto datacenter = InfrastructureResources.datacenterPut();

        Method method =
            EnterpriseAsyncClient.class.getMethod("getLimits", EnterpriseDto.class,
                DatacenterDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, enterprise, datacenter);

        String limitsUri = enterprise.searchLink("limits").getHref();
        String requestURI =
            String.format("GET %s?datacenter=%d HTTP/1.1", limitsUri, datacenter.getId());

        assertRequestLineEquals(request, requestURI);
        assertNonPayloadHeadersEqual(request, "Accept: " + DatacentersLimitsDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateLimits() throws SecurityException, NoSuchMethodException, IOException
    {
        EnterpriseDto enterprise = EnterpriseResources.enterprisePut();

        Method method =
            EnterpriseAsyncClient.class.getMethod("updateLimits", DatacenterLimitsDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.datacenterLimitsPut(enterprise));

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/enterprises/1/limits/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + DatacenterLimitsDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request,
            withHeader(EnterpriseResources.datacenterLimitsPutPayload(enterprise)),
            DatacenterLimitsDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteLimits() throws SecurityException, NoSuchMethodException
    {
        EnterpriseDto enterprise = EnterpriseResources.enterprisePut();

        Method method =
            EnterpriseAsyncClient.class.getMethod("deleteLimits", DatacenterLimitsDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.datacenterLimitsPut(enterprise));

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/enterprises/1/limits/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListLimitsEnterprise() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method = EnterpriseAsyncClient.class.getMethod("listLimits", EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/limits HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + DatacentersLimitsDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** User ***********************/

    public void testGetUser() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("getUser", EnterpriseDto.class, Integer.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut(), 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/users/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + UserDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testListUsers() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = EnterpriseAsyncClient.class.getMethod("listUsers", EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/users HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + UsersDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateUser() throws SecurityException, NoSuchMethodException, IOException
    {
        EnterpriseDto enterprise = EnterpriseResources.enterprisePut();
        UserDto user = EnterpriseResources.userPost();

        Method method =
            EnterpriseAsyncClient.class.getMethod("createUser", EnterpriseDto.class, UserDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, enterprise, user);

        assertRequestLineEquals(request,
            "POST http://localhost/api/admin/enterprises/1/users HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + UserDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(EnterpriseResources.userPostPayload()),
            UserDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testUpdateUser() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = EnterpriseAsyncClient.class.getMethod("updateUser", UserDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.userPut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/enterprises/1/users/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + UserDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(EnterpriseResources.userPutPayload()),
            UserDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteUser() throws SecurityException, NoSuchMethodException
    {
        Method method = EnterpriseAsyncClient.class.getMethod("deleteUser", UserDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.userPut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/enterprises/1/users/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListVirtualMachinesByUser() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method = EnterpriseAsyncClient.class.getMethod("listVirtualMachines", UserDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.userPut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/users/1/action/virtualmachines HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + VirtualMachinesDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Datacenter Repository ********************** */

    public void testRefreshTemplateRepository() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("refreshTemplateRepository", Integer.class,
                Integer.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut().getId(),
                InfrastructureResources.datacenterPut().getId());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/enterprises/1/datacenterrepositories/1/actions/refresh HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** External Network ********************** */

    public void testListExternalNetworks() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("listExternalNetworks", EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/action/externalnetworks HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + VLANNetworksDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Virtual Machine ********************** */

    public void testListVirtualMachines() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("listVirtualMachines", EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/action/virtualmachines HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + VirtualMachinesDto.BASE_MEDIA_TYPE
            + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Machine ********************** */

    public void testListReservedMachines() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("listReservedMachines", EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/reservedmachines HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + MachinesDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Template definition list ***********************/

    public void testListTemplateDefinitionLists() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("listTemplateDefinitionLists",
                EnterpriseDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/appslib/templateDefinitionLists HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: "
            + TemplateDefinitionListsDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateTemplateDefinitionList() throws SecurityException, NoSuchMethodException,
        IOException
    {
        EnterpriseDto enterprise = EnterpriseResources.enterprisePut();
        TemplateDefinitionListDto template = EnterpriseResources.templateListPost();

        Method method =
            EnterpriseAsyncClient.class.getMethod("createTemplateDefinitionList",
                EnterpriseDto.class, TemplateDefinitionListDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, enterprise, template);

        assertRequestLineEquals(request,
            "POST http://localhost/api/admin/enterprises/1/appslib/templateDefinitionLists HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: "
            + TemplateDefinitionListDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(EnterpriseResources.templateListPostPayload()),
            TemplateDefinitionListDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testUpdateTemplateDefinitionList() throws SecurityException, NoSuchMethodException,
        IOException
    {
        TemplateDefinitionListDto template = EnterpriseResources.templateListPut();

        Method method =
            EnterpriseAsyncClient.class.getMethod("updateTemplateDefinitionList",
                TemplateDefinitionListDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, template);

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/enterprises/1/appslib/templateDefinitionLists/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: "
            + TemplateDefinitionListDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, withHeader(EnterpriseResources.templateListPutPayload()),
            TemplateDefinitionListDto.BASE_MEDIA_TYPE, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteTemplateDefinitionList() throws SecurityException, NoSuchMethodException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("deleteTemplateDefinitionList",
                TemplateDefinitionListDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.templateListPut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/enterprises/1/appslib/templateDefinitionLists/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetTemplateDefinitionList() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("getTemplateDefinitionList", EnterpriseDto.class,
                Integer.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.enterprisePut(), 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/appslib/templateDefinitionLists/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: "
            + TemplateDefinitionListDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testListTemplateListStatus() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            EnterpriseAsyncClient.class.getMethod("listTemplateListStatus",
                TemplateDefinitionListDto.class, DatacenterDto.class);
        GeneratedHttpRequest<EnterpriseAsyncClient> request =
            processor.createRequest(method, EnterpriseResources.templateListPut(),
                InfrastructureResources.datacenterPut());

        assertRequestLineEquals(
            request,
            "GET http://localhost/api/admin/enterprises/1/appslib/templateDefinitionLists/1/actions/repositoryStatus?datacenterId=1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: " + TemplatesStateDto.BASE_MEDIA_TYPE + "\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
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
