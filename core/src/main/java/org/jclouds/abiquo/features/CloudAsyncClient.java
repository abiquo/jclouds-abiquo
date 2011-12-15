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
import org.jclouds.abiquo.binders.cloud.BindVolumeRefsToPayload;
import org.jclouds.abiquo.domain.cloud.options.VirtualApplianceOptions;
import org.jclouds.abiquo.domain.cloud.options.VirtualDatacenterOptions;
import org.jclouds.abiquo.domain.cloud.options.VolumeOptions;
import org.jclouds.abiquo.functions.ReturnTaskReferenceOrNull;
import org.jclouds.abiquo.http.filters.AbiquoAuthentication;
import org.jclouds.abiquo.reference.annotations.EnterpriseEdition;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.AcceptedRequestDto;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualAppliancesDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.abiquo.server.core.cloud.VirtualDatacentersDto;
import com.abiquo.server.core.cloud.VirtualMachineDeployDto;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.abiquo.server.core.cloud.VirtualMachineStateDto;
import com.abiquo.server.core.cloud.VirtualMachinesDto;
import com.abiquo.server.core.infrastructure.storage.TiersDto;
import com.abiquo.server.core.infrastructure.storage.VolumeManagementDto;
import com.abiquo.server.core.infrastructure.storage.VolumesManagementDto;
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

    /**
     * @see CloudClient#listStorageTiers(VirtualDatacenterDto)
     */
    @EnterpriseEdition
    @GET
    ListenableFuture<TiersDto> listStorageTiers(
        @EndpointLink("tiers") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter);

    /**
     * @see CloudClient#getStorageTier(VirtualDatacenterDto, Integer)
     */
    @EnterpriseEdition
    @GET
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<TiersDto> getStorageTier(
        @EndpointLink("tiers") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter,
        @BinderParam(AppendToPath.class) Integer tierId);

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
     * @see CloudClient#deployVirtualApplianceAction(RESTLink)
     */
    @POST
    ListenableFuture<AcceptedRequestDto<String>> deployVirtualApplianceAction(
        @BinderParam(BindLinkToPath.class) RESTLink link);

    /*********************** Virtual Machine ***********************/

    /**
     * @see CloudClient#listVirtualMachines(VirtualApplianceDto)
     */
    @GET
    ListenableFuture<VirtualMachinesDto> listVirtualMachines(
        @EndpointLink("virtualmachine") @BinderParam(BindToPath.class) VirtualApplianceDto virtualAppliance);

    /**
     * @see CloudClient#getVirtualMachine(VirtualApplianceDto, Integer)
     */
    @GET
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<VirtualMachineDto> getVirtualMachine(
        @EndpointLink("virtualmachine") @BinderParam(BindToPath.class) VirtualApplianceDto virtualAppliance,
        @BinderParam(AppendToPath.class) Integer virtualMachineId);

    /**
     * @see CloudClient#createVirtualMachine(VirtualApplianceDto, VirtualMachineDto)
     */
    @POST
    ListenableFuture<VirtualMachineDto> createVirtualMachine(
        @EndpointLink("virtualmachine") @BinderParam(BindToPath.class) VirtualApplianceDto virtualAppliance,
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
     * @see CloudClient#changeVirtualMachineState(VirtualMachineDto)
     */
    @PUT
    ListenableFuture<AcceptedRequestDto<VirtualMachineStateDto>> changeVirtualMachineState(
        @EndpointLink("state") @BinderParam(BindToXMLPayloadAndPath.class) VirtualMachineDto virtualMachine,
        VirtualMachineStateDto state);

    /**
     * @see CloudClient#getVirtualMachineState(VirtualMachineDto)
     */
    @GET
    ListenableFuture<VirtualMachineStateDto> getVirtualMachineState(
        @EndpointLink("state") @BinderParam(BindToPath.class) VirtualMachineDto virtualMachine);

    /**
     * @see CloudClient#listAttachedVolumes(VirtualMachineDto)
     */
    @GET
    ListenableFuture<VolumesManagementDto> listAttachedVolumes(
        @EndpointLink("volumes") @BinderParam(BindToPath.class) VirtualMachineDto virtualMachine);

    /**
     * @see CloudClient#detachAllVolumes(VirtualMachineDto)
     */
    @DELETE
    @ResponseParser(ReturnTaskReferenceOrNull.class)
    ListenableFuture<AcceptedRequestDto< ? >> detachAllVolumes(
        @EndpointLink("volumes") @BinderParam(BindToPath.class) VirtualMachineDto virtualMachine);

    /**
     * @see CloudClient#replaceVolumes(VirtualMachineDto, VolumeManagementDto...)
     */
    @PUT
    @ResponseParser(ReturnTaskReferenceOrNull.class)
    ListenableFuture<AcceptedRequestDto< ? >> replaceVolumes(
        @EndpointLink("volumes") @BinderParam(BindToPath.class) VirtualMachineDto virtualMachine,
        @BinderParam(BindVolumeRefsToPayload.class) VolumeManagementDto... volumes);

    /**
     * @see CloudClient#deployVirtualMachine(RESTLink, VirtualMachineDeployDto)
     */
    @POST
    ListenableFuture<AcceptedRequestDto<String>> deployVirtualMachine(
        @BinderParam(BindLinkToPath.class) RESTLink link,
        @BinderParam(BindToXMLPayload.class) VirtualMachineDeployDto deploy);

    /**
     * @see CloudClient#undeployVirtualMachine(RESTLink)
     */
    @POST
    ListenableFuture<AcceptedRequestDto<String>> undeployVirtualMachine(
        @BinderParam(BindLinkToPath.class) RESTLink link);

    /*********************** Storage ***********************/

    /**
     * @see CloudClient#listVolumes(VirtualDatacenterDto)
     */
    @EnterpriseEdition
    @GET
    ListenableFuture<VolumesManagementDto> listVolumes(
        @EndpointLink("volumes") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter);

    /**
     * @see CloudClient#listVolumes(VirtualDatacenterDto, VolumeOptions)
     */
    @EnterpriseEdition
    @GET
    ListenableFuture<VolumesManagementDto> listVolumes(
        @EndpointLink("volumes") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter,
        @BinderParam(AppendOptionsToPath.class) VolumeOptions options);

    /**
     * @see CloudClient#getVolume(VirtualDatacenterDto, Integer)
     */
    @EnterpriseEdition
    @GET
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<VolumeManagementDto> getVolume(
        @EndpointLink("volumes") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter,
        @BinderParam(AppendToPath.class) Integer volumeId);

    /**
     * @see CloudClient#createVolume(VirtualDatacenterDto, VolumeManagementDto)
     */
    @EnterpriseEdition
    @POST
    ListenableFuture<VolumeManagementDto> createVolume(
        @EndpointLink("volumes") @BinderParam(BindToPath.class) VirtualDatacenterDto virtualDatacenter,
        @BinderParam(BindToXMLPayload.class) VolumeManagementDto volume);

    /**
     * @see CloudClient#updateVolume(VolumeManagementDto)
     */
    @EnterpriseEdition
    @PUT
    ListenableFuture<VolumeManagementDto> updateVolume(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) VolumeManagementDto volume);

    /**
     * @see CloudClient#updateVolume(VolumeManagementDto)
     */
    @EnterpriseEdition
    @DELETE
    ListenableFuture<Void> deleteVolume(
        @EndpointLink("edit") @BinderParam(BindToPath.class) VolumeManagementDto volume);
}
