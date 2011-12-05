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
import org.jclouds.abiquo.binders.BindLinkToPath;
import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.binders.BindToXMLPayloadAndPath;
import org.jclouds.abiquo.domain.cloud.options.VirtualApplianceOptions;
import org.jclouds.abiquo.domain.cloud.options.VirtualDatacenterOptions;
import org.jclouds.abiquo.http.filters.AbiquoAuthentication;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.AcceptedRequestDto;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualAppliancesDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.abiquo.server.core.cloud.VirtualDatacentersDto;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.abiquo.server.core.cloud.VirtualMachineStateDto;
import com.abiquo.server.core.cloud.VirtualMachinesDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo Cloud API.
 * 
 * @see http://community.abiquo.com/display/ABI20/API+Reference
 * @see CloudClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@RequestFilters(AbiquoAuthentication.class)
@Consumes(MediaType.APPLICATION_XML)
@Path("/cloud")
public interface CloudAsyncClient
{
    /*********************** Virtual Datacenter ***********************/

    /**
     * @see CloudClient#listVirtualDatacenters(VirtualDatacenterOptions)
     */
    @GET
    @Path("/virtualdatacenters")
    ListenableFuture<VirtualDatacentersDto> listVirtualDatacenters(
        @BinderParam(AppendOptionsToPath.class) VirtualDatacenterOptions options);

    /**
     * @see CloudClient#getVirtualDatacenter(Integer)
     */
    @GET
    @Path("/virtualdatacenters/{virtualdatacenter}")
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<VirtualDatacenterDto> getVirtualDatacenter(
        @PathParam("virtualdatacenter") Integer virtualDatacenterId);

    /**
     * @see CloudClient#createVirtualDatacenter(VirtualDatacenterDto, Integer, Integer)
     */
    @POST
    @Path("/virtualdatacenters")
    ListenableFuture<VirtualDatacenterDto> createVirtualDatacenter(
        @BinderParam(BindToXMLPayload.class) final VirtualDatacenterDto virtualDatacenter,
        @QueryParam("datacenter") Integer datacenterId,
        @QueryParam("enterprise") Integer enterpriseId);

    /**
     * @see CloudClient#updateVirtualDatacenter(VirtualDatacenterDto)
     */
    @PUT
    ListenableFuture<VirtualDatacenterDto> updateVirtualDatacenter(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) VirtualDatacenterDto virtualDatacenter);

    /**
     * @see CloudClient#deleteVirtualDatacenter(VirtualDatacenterDto)
     */
    @DELETE
    ListenableFuture<Void> deleteVirtualDatacenter(
        @EndpointLink("edit") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter);

    /*********************** Virtual Appliance ***********************/

    /**
     * @see CloudClient#listVirtualAppliances(VirtualDatacenterDto)
     */
    @GET
    ListenableFuture<VirtualAppliancesDto> listVirtualAppliances(
        @EndpointLink("virtualappliance") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter);

    /**
     * @see CloudClient#getVirtualAppliance(VirtualDatacenterDto, Integer)
     */
    @GET
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<VirtualApplianceDto> getVirtualAppliance(
        @EndpointLink("virtualappliance") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter,
        @BinderParam(AppendToPath.class) Integer virtualDatacenterId);

    /**
     * @see CloudClient#getVirtualAppliance(RESTLink)
     */
    @GET
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<VirtualApplianceDto> getVirtualAppliance(
        @BinderParam(BindLinkToPath.class) RESTLink link);

    /**
     * @see CloudClient#createVirtualAppliance(VirtualDatacenterDto, VirtualApplianceDto)
     */
    @POST
    ListenableFuture<VirtualApplianceDto> createVirtualAppliance(
        @EndpointLink("virtualappliance") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter,
        @BinderParam(BindToXMLPayload.class) VirtualApplianceDto virtualAppliance);

    /**
     * @see CloudClient#updateVirtualAppliance(VirtualApplianceDto)
     */
    @PUT
    ListenableFuture<VirtualApplianceDto> updateVirtualAppliance(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) VirtualApplianceDto virtualAppliance);

    /**
     * @see CloudClient#deleteVirtualAppliance(VirtualApplianceDto)
     */
    @DELETE
    ListenableFuture<Void> deleteVirtualAppliance(
        @EndpointLink("edit") @BinderParam(BindToPath.class) VirtualApplianceDto virtualAppliance);

    /**
     * @see CloudClient#deleteVirtualAppliance(VirtualApplianceDto, VirtualApplianceOptions)
     */
    @DELETE
    ListenableFuture<Void> deleteVirtualAppliance(
        @EndpointLink("edit") @BinderParam(BindToPath.class) VirtualApplianceDto virtualAppliance,
        @BinderParam(AppendOptionsToPath.class) VirtualApplianceOptions options);

    /**
     * @see CloudClient#actionVirtualAppliance(VirtualDatacenterDto, VirtualApplianceDto)
     */
    @POST
    ListenableFuture<AcceptedRequestDto<String>> actionVirtualAppliance(
        @BinderParam(BindLinkToPath.class) RESTLink link);

    /*********************** Virtual Machine ***********************/

    /**
     * @see CloudClient#listVirtualMachines(VirtualApplianceDto)
     */
    @GET
    ListenableFuture<VirtualMachinesDto> listVirtualMachines(
        @EndpointLink("virtualmachines") @BinderParam(BindToPath.class) VirtualApplianceDto virtualAppliance);

    /**
     * @see CloudClient#getVirtualMachine(VirtualApplianceDto, Integer)
     */
    @GET
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<VirtualMachineDto> getVirtualMachine(
        @EndpointLink("virtualmachines") @BinderParam(BindToPath.class) VirtualApplianceDto virtualAppliance,
        @BinderParam(AppendToPath.class) Integer virtualMachineId);

    /**
     * @see CloudClient#createVirtualMachine(VirtualApplianceDto, VirtualMachineDto)
     */
    @POST
    ListenableFuture<VirtualMachineDto> createVirtualMachine(
        @EndpointLink("virtualmachines") @BinderParam(BindToPath.class) VirtualApplianceDto virtualAppliance,
        @BinderParam(BindToXMLPayload.class) VirtualMachineDto virtualMachine);

    /**
     * @see CloudClient#deleteVirtualMachine(VirtualMachineDto)
     */
    @DELETE
    ListenableFuture<Void> deleteVirtualMachine(
        @EndpointLink("edit") @BinderParam(BindToPath.class) VirtualMachineDto virtualMachine);

    /**
     * @see CloudClient#updateVirtualMachine(VirtualMachineDto)
     */
    @PUT
    ListenableFuture<VirtualMachineDto> updateVirtualMachine(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) VirtualMachineDto virtualMachine);

    /**
     * @see CloudClient#updateVirtualMachineState(VirtualMachineDto)
     */
    @PUT
    ListenableFuture<AcceptedRequestDto> changerVirtualMachineState(
        @EndpointLink("state") @BinderParam(BindToXMLPayloadAndPath.class) VirtualMachineDto virtualMachine,
        VirtualMachineStateDto state);
}
