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

import org.jclouds.abiquo.domain.InfrastructureResources;
import org.jclouds.abiquo.domain.infrastructure.options.MachineOptions;
import org.jclouds.abiquo.functions.ReturnAbiquoExceptionOnNotFoundOr4xx;
import org.jclouds.abiquo.functions.ReturnFalseIfNotAvailable;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.http.functions.ReturnTrueIf2xx;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;
import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code InfrastructureAsyncClient}
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class InfrastructureAsyncClientTest extends
    BaseAbiquoAsyncClientTest<InfrastructureAsyncClient>
{
    /*         ********************** Datacenter ********************** */

    public void testListDatacenters() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("listDatacenters");
        GeneratedHttpRequest<InfrastructureAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateDatacenter() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("createDatacenter", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPost());

        assertRequestLineEquals(request, "POST http://localhost/api/admin/datacenters HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(InfrastructureResources.datacenterPostPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetDatacenter() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("getDatacenter", Integer.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateDatacenter() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("updateDatacenter", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut());

        assertRequestLineEquals(request, "PUT http://localhost/api/admin/datacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(InfrastructureResources.datacenterPutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteDatacenter() throws SecurityException, NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("deleteDatacenter", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut());

        assertRequestLineEquals(request, "DELETE http://localhost/api/admin/datacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListLimitsDatacenter() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("listLimits", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/action/limits HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*         ********************** Rack ********************** */

    public void testListRacks() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("listRacks", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/notmanagedrackdto+xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateRack() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("createRack", DatacenterDto.class,
                RackDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(),
                InfrastructureResources.rackPost());

        assertRequestLineEquals(request,
            "POST http://localhost/api/admin/datacenters/1/racks HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(InfrastructureResources.rackPostPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetRack() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class
                .getMethod("getRack", DatacenterDto.class, Integer.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(), 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateRack() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("updateRack", RackDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.rackPut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/datacenters/1/racks/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(InfrastructureResources.rackPutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteRack() throws SecurityException, NoSuchMethodException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("deleteRack", RackDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.rackPut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/datacenters/1/racks/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*         ********************** Remote Service ********************** */

    public void testListRemoteServices() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("listRemoteServices", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/remoteservices HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateRemoteService() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("createRemoteService", DatacenterDto.class,
                RemoteServiceDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(),
                InfrastructureResources.remoteServicePost());

        assertRequestLineEquals(request,
            "POST http://localhost/api/admin/datacenters/1/remoteservices HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request,
            withHeader(InfrastructureResources.remoteServicePostPayload()), "application/xml",
            false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetRemoteService() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("getRemoteService", DatacenterDto.class,
                RemoteServiceType.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(),
                RemoteServiceType.STORAGE_SYSTEM_MONITOR);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/remoteservices/storagesystemmonitor HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateRemoteService() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            InfrastructureAsyncClient.class
                .getMethod("updateRemoteService", RemoteServiceDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.remoteServicePut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/datacenters/1/remoteservices/nodecollector HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(InfrastructureResources.remoteServicePutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteRemoteService() throws SecurityException, NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class
                .getMethod("deleteRemoteService", RemoteServiceDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.remoteServicePut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/datacenters/1/remoteservices/nodecollector HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testIsAvailableRemoteService() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("isAvailable", RemoteServiceDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.remoteServicePut());

        String checkUri = InfrastructureResources.remoteServicePut().searchLink("check").getHref();
        assertRequestLineEquals(request, String.format("GET %s HTTP/1.1", checkUri));
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReturnTrueIf2xx.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnFalseIfNotAvailable.class);

        checkFilters(request);
    }

    /*         ********************** Machine ********************** */

    public void testDiscoverSingleMachineWithoutOptions() throws SecurityException,
        NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("discoverSingleMachine", DatacenterDto.class,
                String.class, HypervisorType.class, String.class, String.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(), "10.60.1.222",
                HypervisorType.XENSERVER, "user", "pass");

        String baseUrl = "http://localhost/api/admin/datacenters/1/action/discoversingle";
        String query = "hypervisor=XENSERVER&ip=10.60.1.222&user=user&password=pass";
        String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

        assertRequestLineEquals(request, expectedRequest);
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnAbiquoExceptionOnNotFoundOr4xx.class);

        checkFilters(request);
    }

    public void testDiscoverSingleMachineAllParams() throws SecurityException,
        NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("discoverSingleMachine", DatacenterDto.class,
                String.class, HypervisorType.class, String.class, String.class,
                MachineOptions.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(), "80.80.80.80",
                HypervisorType.KVM, "user", "pass", MachineOptions.builder().port(8889).build());

        String baseUrl = "http://localhost/api/admin/datacenters/1/action/discoversingle";
        String query = "hypervisor=KVM&ip=80.80.80.80&user=user&password=pass&port=8889";
        String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

        assertRequestLineEquals(request, expectedRequest);
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnAbiquoExceptionOnNotFoundOr4xx.class);

        checkFilters(request);
    }

    public void testDiscoverSingleMachineDefaultValues() throws SecurityException,
        NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("discoverSingleMachine", DatacenterDto.class,
                String.class, HypervisorType.class, String.class, String.class,
                MachineOptions.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(), "80.80.80.80",
                HypervisorType.KVM, "user", "pass", MachineOptions.builder().build());

        String baseUrl = "http://localhost/api/admin/datacenters/1/action/discoversingle";
        String query = "hypervisor=KVM&ip=80.80.80.80&user=user&password=pass";
        String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

        assertRequestLineEquals(request, expectedRequest);
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnAbiquoExceptionOnNotFoundOr4xx.class);

        checkFilters(request);
    }

    public void testDiscoverMultipleMachinesWithoutOptions() throws SecurityException,
        NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("discoverMultipleMachines",
                DatacenterDto.class, String.class, String.class, HypervisorType.class,
                String.class, String.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(), "10.60.1.222",
                "10.60.1.250", HypervisorType.XENSERVER, "user", "pass");

        String baseUrl = "http://localhost/api/admin/datacenters/1/action/discovermultiple";
        String query =
            "password=pass&ipTo=10.60.1.250&ipFrom=10.60.1.222&hypervisor=XENSERVER&user=user";
        String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

        assertRequestLineEquals(request, expectedRequest);
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnAbiquoExceptionOnNotFoundOr4xx.class);

        checkFilters(request);
    }

    public void testDiscoverMultipleMachinesAllParams() throws SecurityException,
        NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("discoverMultipleMachines",
                DatacenterDto.class, String.class, String.class, HypervisorType.class,
                String.class, String.class, MachineOptions.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.datacenterPut(), "80.80.80.80",
                "80.80.80.86", HypervisorType.KVM, "user", "pass", MachineOptions.builder().port(
                    8889).build());

        String baseUrl = "http://localhost/api/admin/datacenters/1/action/discovermultiple";
        String query =
            "password=pass&ipTo=80.80.80.86&ipFrom=80.80.80.80&hypervisor=KVM&user=user&port=8889";
        String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

        assertRequestLineEquals(request, expectedRequest);
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnAbiquoExceptionOnNotFoundOr4xx.class);

        checkFilters(request);
    }

    public void testListMachines() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("listMachines", RackDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.rackPut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks/1/machines HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetMachine() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("getMachine", RackDto.class, Integer.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.rackPut(), 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks/1/machines/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testCreateMachine() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("createMachine", RackDto.class,
                MachineDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.rackPut(),
                InfrastructureResources.machinePost());

        assertRequestLineEquals(request,
            "POST http://localhost/api/admin/datacenters/1/racks/1/machines HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(InfrastructureResources.machinePostPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testUpdateMachine() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("updateMachine", MachineDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.machinePut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/datacenters/1/racks/1/machines/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(InfrastructureResources.machinePutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteMachine() throws SecurityException, NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("deleteMachine", MachineDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, InfrastructureResources.machinePut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/datacenters/1/racks/1/machines/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<InfrastructureAsyncClient>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<InfrastructureAsyncClient>>()
        {
        };
    }
}
