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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.abiquo.binders.AppendOptionsToPath;
import org.jclouds.abiquo.binders.AppendToPath;
import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.binders.BindToXMLPayload;
import org.jclouds.abiquo.binders.BindToXMLPayloadAndPath;
import org.jclouds.abiquo.binders.infrastructure.AppendRemoteServiceTypeToPath;
import org.jclouds.abiquo.binders.infrastructure.BindRemoteServiceCheck;
import org.jclouds.abiquo.domain.infrastructure.options.MachineOptions;
import org.jclouds.abiquo.functions.ReturnFalseOn5xx;
import org.jclouds.abiquo.functions.infrastructure.ParseDatacenter;
import org.jclouds.abiquo.functions.infrastructure.ParseDatacenters;
import org.jclouds.abiquo.functions.infrastructure.ParseMachine;
import org.jclouds.abiquo.functions.infrastructure.ParseMachines;
import org.jclouds.abiquo.functions.infrastructure.ParseRack;
import org.jclouds.abiquo.functions.infrastructure.ParseRacks;
import org.jclouds.abiquo.functions.infrastructure.ParseRemoteService;
import org.jclouds.abiquo.functions.infrastructure.ParseRemoteServices;
import org.jclouds.abiquo.reference.rest.AbiquoMediaType;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.DatacentersDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.MachinesDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.abiquo.server.core.infrastructure.RacksDto;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;
import com.abiquo.server.core.infrastructure.RemoteServicesDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo Infrastructure API.
 * 
 * @see http://community.abiquo.com/display/ABI18/API+Reference
 * @see InfrastructureClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@RequestFilters(BasicAuthentication.class)
@Consumes(MediaType.APPLICATION_XML)
@Path("/admin")
public interface InfrastructureAsyncClient
{
    // Datacenter

    /**
     * @see InfrastructureClient#listDatacenters()
     */
    @GET
    @Path("/datacenters")
    @ResponseParser(ParseDatacenters.class)
    ListenableFuture<DatacentersDto> listDatacenters();

    /**
     * @see InfrastructureClient#createDatacenter(DatacenterDto)
     */
    @POST
    @Path("/datacenters")
    @ResponseParser(ParseDatacenter.class)
    ListenableFuture<DatacenterDto> createDatacenter(
        @BinderParam(BindToXMLPayload.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#getDatacenter(Integer)
     */
    @GET
    @Path("/datacenters/{datacenter}")
    @ResponseParser(ParseDatacenter.class)
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<DatacenterDto> getDatacenter(@PathParam("datacenter") Integer datacenterId);

    /**
     * @see InfrastructureClient#updateDatacenter(DatacenterDto)
     */
    @PUT
    @ResponseParser(ParseDatacenter.class)
    ListenableFuture<DatacenterDto> updateDatacenter(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#deleteDatacenter(DatacenterDto)
     */
    @DELETE
    ListenableFuture<Void> deleteDatacenter(
        @EndpointLink("edit") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#discoverSingleMachine(DatacenterDto, String, HypervisorType,
     *      String, String)
     */
    @GET
    @ResponseParser(ParseMachine.class)
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<MachineDto> discoverSingleMachine(
        @EndpointLink("discoversingle") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @QueryParam("ip") String ip, @QueryParam("hypervisor") HypervisorType hypervisorType,
        @QueryParam("user") String user, @QueryParam("password") String password);

    /**
     * @see InfrastructureClient#discoverSingleMachine(DatacenterDto, String, HypervisorType,
     *      String, String, MachineOptions)
     */
    @GET
    @ResponseParser(ParseMachine.class)
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<MachineDto> discoverSingleMachine(
        @EndpointLink("discoversingle") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @QueryParam("ip") String ip, @QueryParam("hypervisor") HypervisorType hypervisorType,
        @QueryParam("user") String user, @QueryParam("password") String password,
        @BinderParam(AppendOptionsToPath.class) MachineOptions options);

    /**
     * @see InfrastructureClient#discoverMultipleMachines(DatacenterDto, String, String,
     *      HypervisorType, String, String)
     */
    @GET
    @ResponseParser(ParseMachines.class)
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<MachineDto> discoverMultipleMachines(
        @EndpointLink("discovermultiple") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @QueryParam("ipFrom") String ipFrom, @QueryParam("ipTo") String ipTo,
        @QueryParam("hypervisor") HypervisorType hypervisorType, @QueryParam("user") String user,
        @QueryParam("password") String password);

    /**
     * @see InfrastructureClient#discoverMultipleMachines(DatacenterDto, String, String,
     *      HypervisorType, String, String, MachineOptions)
     */
    @GET
    @ResponseParser(ParseMachines.class)
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<MachineDto> discoverMultipleMachines(
        @EndpointLink("discovermultiple") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @QueryParam("ipFrom") String ipFrom, @QueryParam("ipTo") String ipTo,
        @QueryParam("hypervisor") HypervisorType hypervisorType, @QueryParam("user") String user,
        @QueryParam("password") String password,
        @BinderParam(AppendOptionsToPath.class) MachineOptions options);

    // Rack

    /**
     * @see InfrastructureClient#listRacks(DatacenterDto)
     */
    @GET
    @Consumes(AbiquoMediaType.APPLICATION_NOTMANAGEDRACKSDTO_XML)
    @ResponseParser(ParseRacks.class)
    ListenableFuture<RacksDto> listRacks(
        @EndpointLink("racks") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#createRack(DatacenterDto, RackDto)
     */
    @POST
    @ResponseParser(ParseRack.class)
    ListenableFuture<RackDto> createRack(
        @EndpointLink("racks") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(BindToXMLPayload.class) RackDto rack);

    /**
     * @see InfrastructureClient#getRack(DatacenterDto, Integer)
     */
    @GET
    @ResponseParser(ParseRack.class)
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<RackDto> getRack(
        @EndpointLink("racks") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(AppendToPath.class) Integer rackId);

    /**
     * @see InfrastructureClient#updateRack(RackDto)
     */
    @PUT
    @ResponseParser(ParseRack.class)
    ListenableFuture<RackDto> updateRack(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) RackDto rack);

    /**
     * @see InfrastructureClient#deleteRack(RackDto)
     */
    @DELETE
    ListenableFuture<Void> deleteRack(
        @EndpointLink("edit") @BinderParam(BindToPath.class) RackDto rack);

    // Remote service

    /**
     * @see InfrastructureClient#listRemoteServices(DatacenterDto)
     */
    @GET
    @ResponseParser(ParseRemoteServices.class)
    ListenableFuture<RemoteServicesDto> listRemoteServices(
        @EndpointLink("remoteservices") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#createRemoteService(DatacenterDto, RemoteServiceDto)
     */
    @POST
    @ResponseParser(ParseRemoteService.class)
    ListenableFuture<RemoteServiceDto> createRemoteService(
        @EndpointLink("remoteservices") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(BindToXMLPayload.class) RemoteServiceDto remoteService);

    /**
     * @see InfrastructureClient#getRemoteService(DatacenterDto, RemoteServiceType)
     */
    @GET
    @ResponseParser(ParseRemoteService.class)
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<RemoteServiceDto> getRemoteService(
        @EndpointLink("remoteservices") @BinderParam(BindToPath.class) final DatacenterDto datacenter,
        @BinderParam(AppendRemoteServiceTypeToPath.class) final RemoteServiceType remoteServiceType);

    /**
     * @see InfrastructureClient#updateRemoteService(RemoteServiceDto)
     */
    @PUT
    @ResponseParser(ParseRemoteService.class)
    ListenableFuture<RemoteServiceDto> updateRemoteService(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) RemoteServiceDto remoteService);

    /**
     * @see InfrastructureClient#deleteRemoteService(RemoteServiceDto)
     */
    @DELETE
    ListenableFuture<Void> deleteRemoteService(
        @EndpointLink("edit") @BinderParam(BindToPath.class) RemoteServiceDto remoteService);

    /**
     * @see InfrastructureClient#isAvailable(RemoteServiceDto)
     */
    @GET
    @ExceptionParser(ReturnFalseOn5xx.class)
    ListenableFuture<Boolean> isAvailable(
        @BinderParam(BindRemoteServiceCheck.class) RemoteServiceDto remoteService);

    // Machine

    /**
     * @see InfrastructureClient#listMachines(RackDto)
     */
    @GET
    @ResponseParser(ParseMachines.class)
    ListenableFuture<MachinesDto> listMachines(
        @EndpointLink("machines") @BinderParam(BindToPath.class) RackDto rack);

    /**
     * @see InfrastructureClient#createMachine(RackDto, MachineDto)
     */
    @POST
    @ResponseParser(ParseMachine.class)
    ListenableFuture<MachineDto> createMachine(
        @EndpointLink("machines") @BinderParam(BindToPath.class) RackDto rack,
        @BinderParam(BindToXMLPayload.class) MachineDto machine);

    /**
     * @see InfrastructureClient#getMachine(RackDto, Integer)
     */
    @GET
    @ResponseParser(ParseMachine.class)
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<MachineDto> getMachine(
        @EndpointLink("machines") @BinderParam(BindToPath.class) final RackDto rack,
        @BinderParam(AppendToPath.class) Integer machineId);

    /**
     * @see InfrastructureClient#updateMachine(MachineDto)
     */
    @PUT
    @ResponseParser(ParseMachine.class)
    ListenableFuture<MachineDto> updateMachine(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) MachineDto machine);

    /**
     * @see InfrastructureClient#deleteMachine(MachineDto)
     */
    @DELETE
    ListenableFuture<Void> deleteMachine(
        @EndpointLink("edit") @BinderParam(BindToPath.class) MachineDto machine);
}
