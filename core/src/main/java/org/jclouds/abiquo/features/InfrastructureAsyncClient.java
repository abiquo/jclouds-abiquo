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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.abiquo.binders.AppendOptionsToPath;
import org.jclouds.abiquo.binders.AppendToPath;
import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.binders.BindToXMLPayloadAndPath;
import org.jclouds.abiquo.binders.infrastructure.AppendRemoteServiceTypeToPath;
import org.jclouds.abiquo.domain.infrastructure.options.DatacenterOptions;
import org.jclouds.abiquo.domain.infrastructure.options.MachineOptions;
import org.jclouds.abiquo.domain.infrastructure.options.StoragePoolOptions;
import org.jclouds.abiquo.domain.network.options.IpOptions;
import org.jclouds.abiquo.domain.network.options.NetworkOptions;
import org.jclouds.abiquo.domain.options.QueryOptions;
import org.jclouds.abiquo.domain.options.search.FilterOptions;
import org.jclouds.abiquo.functions.ReturnAbiquoExceptionOnNotFoundOr4xx;
import org.jclouds.abiquo.functions.ReturnFalseIfNotAvailable;
import org.jclouds.abiquo.functions.infrastructure.ParseDatacenterId;
import org.jclouds.abiquo.http.filters.AbiquoAuthentication;
import org.jclouds.abiquo.http.filters.AppendApiVersionToMediaType;
import org.jclouds.abiquo.reference.annotations.EnterpriseEdition;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.http.functions.ReturnStringIf2xx;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.ParamParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.cloud.HypervisorTypesDto;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.DatacentersDto;
import com.abiquo.server.core.infrastructure.LogicServersDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.MachineStateDto;
import com.abiquo.server.core.infrastructure.MachinesDto;
import com.abiquo.server.core.infrastructure.OrganizationsDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.abiquo.server.core.infrastructure.RacksDto;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;
import com.abiquo.server.core.infrastructure.RemoteServicesDto;
import com.abiquo.server.core.infrastructure.UcsRackDto;
import com.abiquo.server.core.infrastructure.UcsRacksDto;
import com.abiquo.server.core.infrastructure.network.IpsPoolManagementDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworkDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworksDto;
import com.abiquo.server.core.infrastructure.network.VlanTagAvailabilityDto;
import com.abiquo.server.core.infrastructure.storage.StorageDeviceDto;
import com.abiquo.server.core.infrastructure.storage.StorageDevicesDto;
import com.abiquo.server.core.infrastructure.storage.StoragePoolDto;
import com.abiquo.server.core.infrastructure.storage.StoragePoolsDto;
import com.abiquo.server.core.infrastructure.storage.TierDto;
import com.abiquo.server.core.infrastructure.storage.TiersDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo Infrastructure API.
 * 
 * @see http://community.abiquo.com/display/ABI20/API+Reference
 * @see InfrastructureClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@RequestFilters({AbiquoAuthentication.class, AppendApiVersionToMediaType.class})
@Path("/admin")
public interface InfrastructureAsyncClient
{
    /*********************** Datacenter ***********************/

    /**
     * @see InfrastructureClient#listDatacenters()
     */
    @GET
    @Path("/datacenters")
    @Consumes(DatacentersDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<DatacentersDto> listDatacenters();

    /**
     * @see InfrastructureClient#createDatacenter(DatacenterDto)
     */
    @POST
    @Path("/datacenters")
    @Produces(DatacenterDto.BASE_MEDIA_TYPE)
    @Consumes(DatacenterDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<DatacenterDto> createDatacenter(
        @BinderParam(BindToXMLPayload.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#getDatacenter(Integer)
     */
    @GET
    @Path("/datacenters/{datacenter}")
    @Consumes(DatacenterDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<DatacenterDto> getDatacenter(@PathParam("datacenter") Integer datacenterId);

    /**
     * @see InfrastructureClient#updateDatacenter(DatacenterDto)
     */
    @PUT
    @Produces(DatacenterDto.BASE_MEDIA_TYPE)
    @Consumes(DatacenterDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
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
    @Consumes(MachineDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnAbiquoExceptionOnNotFoundOr4xx.class)
    ListenableFuture<MachineDto> discoverSingleMachine(
        @EndpointLink("discoversingle") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @QueryParam("ip") String ip, @QueryParam("hypervisor") HypervisorType hypervisorType,
        @QueryParam("user") String user, @QueryParam("password") String password);

    /**
     * @see InfrastructureClient#discoverSingleMachine(DatacenterDto, String, HypervisorType,
     *      String, String, MachineOptions)
     */
    @GET
    @Consumes(MachineDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnAbiquoExceptionOnNotFoundOr4xx.class)
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
    @Consumes(MachinesDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnAbiquoExceptionOnNotFoundOr4xx.class)
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
    @Consumes(MachinesDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnAbiquoExceptionOnNotFoundOr4xx.class)
    ListenableFuture<MachineDto> discoverMultipleMachines(
        @EndpointLink("discovermultiple") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @QueryParam("ipFrom") String ipFrom, @QueryParam("ipTo") String ipTo,
        @QueryParam("hypervisor") HypervisorType hypervisorType, @QueryParam("user") String user,
        @QueryParam("password") String password,
        @BinderParam(AppendOptionsToPath.class) MachineOptions options);

    /**
     * @see InfrastructureClient#listLimits(DatacenterDto)
     */
    @GET
    @Consumes(DatacentersLimitsDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<DatacentersLimitsDto> listLimits(
        @EndpointLink("getLimits") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /*********************** Hypervisor ***********************/

    /**
     * @see InfrastructureClient#getHypervisorTypeFromMachine(DatacenterDto, DatacenterOptions)
     */
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @ResponseParser(ReturnStringIf2xx.class)
    ListenableFuture<String> getHypervisorTypeFromMachine(
        @EndpointLink("hypervisor") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(AppendOptionsToPath.class) DatacenterOptions options);

    /**
     * @see InfrastructureClient#getHypervisorTypes(DatacenterDto)
     */
    @GET
    @Consumes(HypervisorTypesDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<HypervisorTypesDto> getHypervisorTypes(
        @EndpointLink("hypervisors") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /*********************** Unmanaged Rack ***********************/

    /**
     * @see InfrastructureClient#listRacks(DatacenterDto)
     */
    @GET
    @Consumes(RacksDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<RacksDto> listRacks(
        @EndpointLink("racks") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#createRack(DatacenterDto, RackDto)
     */
    @POST
    @Produces(RackDto.BASE_MEDIA_TYPE)
    @Consumes(RackDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<RackDto> createRack(
        @EndpointLink("racks") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(BindToXMLPayload.class) RackDto rack);

    /**
     * @see InfrastructureClient#getRack(DatacenterDto, Integer)
     */
    @GET
    @Consumes(RackDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<RackDto> getRack(
        @EndpointLink("racks") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(AppendToPath.class) Integer rackId);

    /**
     * @see InfrastructureClient#updateRack(RackDto)
     */
    @PUT
    @Consumes(RackDto.BASE_MEDIA_TYPE)
    @Produces(RackDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<RackDto> updateRack(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) RackDto rack);

    /**
     * @see InfrastructureClient#deleteRack(RackDto)
     */
    @DELETE
    ListenableFuture<Void> deleteRack(
        @EndpointLink("edit") @BinderParam(BindToPath.class) RackDto rack);

    /*********************** Managed Rack ***********************/

    /**
     * @see InfrastructureClient#listManagedRacks(DatacenterDto)
     */
    @GET
    @Consumes(UcsRacksDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<UcsRacksDto> listManagedRacks(
        @EndpointLink("racks") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#createManagedRack(DatacenterDto, UcsRackDto)
     */
    @POST
    @Produces(UcsRackDto.BASE_MEDIA_TYPE)
    @Consumes(UcsRackDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<UcsRackDto> createManagedRack(
        @EndpointLink("racks") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(BindToXMLPayload.class) UcsRackDto rack);

    /**
     * @see InfrastructureClient#getManagedRack(DatacenterDto, Integer)
     */
    @GET
    @Consumes(UcsRackDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<UcsRackDto> getManagedRack(
        @EndpointLink("racks") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(AppendToPath.class) Integer rackId);

    /**
     * @see InfrastructureClient#updateManagedRack(UcsRackDto)
     */
    @PUT
    @Consumes(UcsRackDto.BASE_MEDIA_TYPE)
    @Produces(UcsRackDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<UcsRackDto> updateManagedRack(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) UcsRackDto rack);

    /**
     * @see InfrastructureClient#listServiceProfiles(UcsRackDto)
     */
    @GET
    @Consumes(LogicServersDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<LogicServersDto> listServiceProfiles(
        @EndpointLink("logicservers") @BinderParam(BindToPath.class) UcsRackDto rack);

    /**
     * @see InfrastructureClient#listServiceProfiles(UcsRackDto, QueryOptions)
     */
    @GET
    @Consumes(LogicServersDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<LogicServersDto> listServiceProfiles(
        @EndpointLink("logicservers") @BinderParam(BindToPath.class) UcsRackDto rack,
        @BinderParam(AppendOptionsToPath.class) FilterOptions options);

    /**
     * @see InfrastructureClient#listServiceProfileTemplates(UcsRackDto)
     */
    @GET
    @Consumes(LogicServersDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<LogicServersDto> listServiceProfileTemplates(
        @EndpointLink("ls-templates") @BinderParam(BindToPath.class) UcsRackDto rack);

    /**
     * @see InfrastructureClient#listServiceProfileTemplates(UcsRackDto, LogicServerOptions)
     */
    @GET
    @Consumes(LogicServersDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<LogicServersDto> listServiceProfileTemplates(
        @EndpointLink("ls-templates") @BinderParam(BindToPath.class) UcsRackDto rack,
        @BinderParam(AppendOptionsToPath.class) FilterOptions options);

    /**
     * @see InfrastructureClient#listOrganizations(UcsRackDto)
     */
    @GET
    @Consumes(OrganizationsDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<OrganizationsDto> listOrganizations(
        @EndpointLink("organizations") @BinderParam(BindToPath.class) UcsRackDto rack);

    /**
     * @see InfrastructureClient#listOrganizations(UcsRackDto, OrganizationOptions)
     */
    @GET
    @Consumes(OrganizationsDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<OrganizationsDto> listOrganizations(
        @EndpointLink("organizations") @BinderParam(BindToPath.class) UcsRackDto rack,
        @BinderParam(AppendOptionsToPath.class) FilterOptions options);

    /*********************** Remote Service ***********************/

    /**
     * @see InfrastructureClient#listRemoteServices(DatacenterDto)
     */
    @GET
    @Consumes(RemoteServicesDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<RemoteServicesDto> listRemoteServices(
        @EndpointLink("remoteservices") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#createRemoteService(DatacenterDto, RemoteServiceDto)
     */
    @POST
    @Produces(RemoteServiceDto.BASE_MEDIA_TYPE)
    @Consumes(RemoteServiceDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<RemoteServiceDto> createRemoteService(
        @EndpointLink("remoteservices") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(BindToXMLPayload.class) RemoteServiceDto remoteService);

    /**
     * @see InfrastructureClient#getRemoteService(DatacenterDto, RemoteServiceType)
     */
    @GET
    @Consumes(RemoteServiceDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<RemoteServiceDto> getRemoteService(
        @EndpointLink("remoteservices") @BinderParam(BindToPath.class) final DatacenterDto datacenter,
        @BinderParam(AppendRemoteServiceTypeToPath.class) final RemoteServiceType remoteServiceType);

    /**
     * @see InfrastructureClient#updateRemoteService(RemoteServiceDto)
     */
    @PUT
    @Consumes(RemoteServiceDto.BASE_MEDIA_TYPE)
    @Produces(RemoteServiceDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
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
    @ExceptionParser(ReturnFalseIfNotAvailable.class)
    ListenableFuture<Boolean> isAvailable(
        @EndpointLink("check") @BinderParam(BindToPath.class) RemoteServiceDto remoteService);

    /*********************** Machine ***********************/

    /**
     * @see InfrastructureClient#listMachines(RackDto)
     */
    @GET
    @Consumes(MachinesDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<MachinesDto> listMachines(
        @EndpointLink("machines") @BinderParam(BindToPath.class) RackDto rack);

    /**
     * @see InfrastructureClient#createMachine(RackDto, MachineDto)
     */
    @POST
    @Produces(MachineDto.BASE_MEDIA_TYPE)
    @Consumes(MachineDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<MachineDto> createMachine(
        @EndpointLink("machines") @BinderParam(BindToPath.class) RackDto rack,
        @BinderParam(BindToXMLPayload.class) MachineDto machine);

    /**
     * @see InfrastructureClient#getMachine(RackDto, Integer)
     */
    @GET
    @Consumes(MachineDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<MachineDto> getMachine(
        @EndpointLink("machines") @BinderParam(BindToPath.class) final RackDto rack,
        @BinderParam(AppendToPath.class) Integer machineId);

    /**
     * @see InfrastructureClient#checkMachineState(MachineDto)
     */
    @GET
    @Consumes(MachineStateDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<MachineStateDto> checkMachineState(
        @EndpointLink("checkstate") @BinderParam(BindToPath.class) final MachineDto machine,
        @QueryParam("sync") boolean sync);

    /**
     * @see InfrastructureClient#updateMachine(MachineDto)
     */
    @PUT
    @Produces(MachineDto.BASE_MEDIA_TYPE)
    @Consumes(MachineDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<MachineDto> updateMachine(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) MachineDto machine);

    /**
     * @see InfrastructureClient#deleteMachine(MachineDto)
     */
    @DELETE
    ListenableFuture<Void> deleteMachine(
        @EndpointLink("edit") @BinderParam(BindToPath.class) MachineDto machine);

    /*********************** Storage Device ***********************/

    /**
     * @see InfrastructureClient#listStorageDevices(DatacenterDto)
     */
    @EnterpriseEdition
    @GET
    @Consumes(StorageDevicesDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<StorageDevicesDto> listStorageDevices(
        @EndpointLink("devices") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#getStorageDevice(DatacenterDto, Integer)
     */
    @EnterpriseEdition
    @GET
    @Consumes(StorageDeviceDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<StorageDeviceDto> getStorageDevice(
        @EndpointLink("devices") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(AppendToPath.class) Integer storageDeviceId);

    /**
     * @see InfrastructureClient#createStorageDevice(DatacenterDto, StorageDeviceDto)
     */
    @EnterpriseEdition
    @POST
    @Produces(StorageDeviceDto.BASE_MEDIA_TYPE)
    @Consumes(StorageDeviceDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<StorageDeviceDto> createStorageDevice(
        @EndpointLink("devices") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(BindToXMLPayload.class) StorageDeviceDto storageDevice);

    /**
     * @see InfrastructureClient#deleteStorageDevice(StorageDeviceDto)
     */
    @EnterpriseEdition
    @DELETE
    ListenableFuture<Void> deleteStorageDevice(
        @EndpointLink("edit") @BinderParam(BindToPath.class) StorageDeviceDto storageDevice);

    /**
     * @see InfrastructureClient#updateStorageDevice(StorageDeviceDto)
     */
    @EnterpriseEdition
    @PUT
    @Produces(StorageDeviceDto.BASE_MEDIA_TYPE)
    @Consumes(StorageDeviceDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<StorageDeviceDto> updateStorageDevice(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) StorageDeviceDto storageDevice);

    /*********************** Tier ***********************/

    /**
     * @see InfrastructureClient#listTiers(DatacenterDto)
     */
    @EnterpriseEdition
    @GET
    @Consumes(TiersDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<TiersDto> listTiers(
        @EndpointLink("tiers") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#updateTier(TierDto)
     */
    @EnterpriseEdition
    @PUT
    @Produces(TierDto.BASE_MEDIA_TYPE)
    @Consumes(TierDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<TierDto> updateTier(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) TierDto tier);

    /**
     * @see InfrastructureClient#getTier(DatacenterDto, Integer)
     */
    @EnterpriseEdition
    @GET
    @Consumes(TierDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<TierDto> getTier(
        @EndpointLink("tiers") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(AppendToPath.class) Integer tierId);

    /*********************** Storage Pool ***********************/

    /**
     * @see InfrastructureClient#listStoragePools(StorageDeviceDto, StoragePoolOptions)
     */
    @EnterpriseEdition
    @GET
    @Consumes(StoragePoolsDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<StoragePoolsDto> listStoragePools(
        @EndpointLink("pools") @BinderParam(BindToPath.class) StorageDeviceDto storageDevice,
        @BinderParam(AppendOptionsToPath.class) StoragePoolOptions options);

    /**
     * @see InfrastructureClient#listStoragePools(TierDto)
     */
    @EnterpriseEdition
    @GET
    @Consumes(StoragePoolsDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<StoragePoolsDto> listStoragePools(
        @EndpointLink("pools") @BinderParam(BindToPath.class) TierDto tier);

    /**
     * @see InfrastructureClient#createStoragePool(StorageDeviceDto, StoragePoolDto)
     */
    @EnterpriseEdition
    @POST
    @Consumes(StoragePoolDto.BASE_MEDIA_TYPE)
    @Produces(StoragePoolDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<StoragePoolDto> createStoragePool(
        @EndpointLink("pools") @BinderParam(BindToPath.class) StorageDeviceDto storageDevice,
        @BinderParam(BindToXMLPayload.class) StoragePoolDto storagePool);

    /**
     * @see InfrastructureClient#updateStoragePool(StoragePoolDto)
     */
    @EnterpriseEdition
    @PUT
    // For the most strangest reason in world, compiler does not accept 
    // constants StoragePoolDto.BASE_MEDIA_TYPE for this method.
    @Consumes("application/vnd.abiquo.storagepool+xml")
    @Produces("application/vnd.abiquo.storagepool+xml")
    @JAXBResponseParser
    ListenableFuture<StoragePoolDto> updateStoragePool(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) StoragePoolDto StoragePoolDto);

    /**
     * @see InfrastructureClient#deleteStoragePool(StoragePoolDto)
     */
    @EnterpriseEdition
    @DELETE
    ListenableFuture<Void> deleteStoragePool(
        @EndpointLink("edit") @BinderParam(BindToPath.class) StoragePoolDto storagePool);

    /**
     * @see InfrastructureClient#getStoragePool(StorageDeviceDto, String)
     */
    @EnterpriseEdition
    @GET
    @Consumes(StoragePoolDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<StoragePoolDto> getStoragePool(
        @EndpointLink("pools") @BinderParam(BindToPath.class) final StorageDeviceDto storageDevice,
        @BinderParam(AppendToPath.class) final String storagePoolId);

    /**
     * @see InfrastructureClient#refreshStoragePool(StoragePoolDto, StoragePoolOptions)
     */
    @EnterpriseEdition
    @GET
    ListenableFuture<StoragePoolDto> refreshStoragePool(
        @EndpointLink("edit") @BinderParam(BindToPath.class) StoragePoolDto storagePool,
        @BinderParam(AppendOptionsToPath.class) StoragePoolOptions options);

    /*********************** Network ***********************/

    /**
     * @see InfrastructureClient#listNetworks(DatacenterDto)
     */
    @EnterpriseEdition
    @GET
    @Consumes(VLANNetworksDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<VLANNetworksDto> listNetworks(
        @EndpointLink("network") @BinderParam(BindToPath.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#listNetwork(DatacenterDto, NetworkOptions)
     */
    @EnterpriseEdition
    @GET
    @Consumes(VLANNetworksDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<VLANNetworksDto> listNetworks(
        @EndpointLink("network") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(AppendOptionsToPath.class) NetworkOptions options);

    /**
     * @see InfrastructureClient#getNetwork(DatacenterDto, Integer)
     */
    @EnterpriseEdition
    @GET
    @Consumes(VLANNetworkDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<VLANNetworkDto> getNetwork(
        @EndpointLink("network") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(AppendToPath.class) Integer networkId);

    /**
     * @see InfrastructureClient#createNetwork(DatacenterDto, VLANNetworkDto)
     */
    @EnterpriseEdition
    @POST
    @Produces(VLANNetworkDto.BASE_MEDIA_TYPE)
    @Consumes(VLANNetworkDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<VLANNetworkDto> createNetwork(
        @EndpointLink("network") @BinderParam(BindToPath.class) DatacenterDto datacenter,
        @BinderParam(BindToXMLPayload.class) VLANNetworkDto network);

    /**
     * @see InfrastructureClient#updateNetwork(VLANNetworkDto)
     */
    @EnterpriseEdition
    @PUT
    @Produces(VLANNetworkDto.BASE_MEDIA_TYPE)
    @Consumes(VLANNetworkDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<VLANNetworkDto> updateNetwork(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) VLANNetworkDto network);

    /**
     * @see InfrastructureClient#deleteNetwork(VLANNetworkDto)
     */
    @EnterpriseEdition
    @DELETE
    ListenableFuture<Void> deleteNetwork(
        @EndpointLink("edit") @BinderParam(BindToPath.class) VLANNetworkDto network);

    /**
     * @see InfrastructureClient#checkTagAvailability(DatacenterDto, Integer)
     */
    @EnterpriseEdition
    @GET
    @Path("/datacenters/{datacenter}/network/action/checkavailability")
    @Consumes(VlanTagAvailabilityDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<VlanTagAvailabilityDto> checkTagAvailability(
        @PathParam("datacenter") @ParamParser(ParseDatacenterId.class) DatacenterDto datacenter,
        @QueryParam("tag") Integer tag);

    /*********************** Public Network IPs ***********************/

    /**
     * @see CloudClient#listNetworkIps(VLANNetworkDto)
     */
    @GET
    @Consumes(IpsPoolManagementDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<IpsPoolManagementDto> listNetworkIps(
        @EndpointLink("ips") @BinderParam(BindToPath.class) VLANNetworkDto network);

    /**
     * @see CloudClient#listNetworkIps(VLANNetworkDto, IpOptions)
     */
    @GET
    @Consumes(IpsPoolManagementDto.BASE_MEDIA_TYPE)
    @JAXBResponseParser
    ListenableFuture<IpsPoolManagementDto> listNetworkIps(
        @EndpointLink("ips") @BinderParam(BindToPath.class) VLANNetworkDto network,
        @BinderParam(AppendOptionsToPath.class) IpOptions options);
}
