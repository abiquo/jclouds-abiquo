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

package org.jclouds.abiquo.domain.infrastructure;

import static com.google.common.collect.Iterables.filter;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.Limits;
import org.jclouds.abiquo.domain.infrastructure.options.DatacenterOptions;
import org.jclouds.abiquo.domain.infrastructure.options.MachineOptions;
import org.jclouds.abiquo.domain.network.ExternalNetwork;
import org.jclouds.abiquo.domain.network.Network;
import org.jclouds.abiquo.domain.network.PublicNetwork;
import org.jclouds.abiquo.domain.network.UnmanagedNetwork;
import org.jclouds.abiquo.domain.network.options.NetworkOptions;
import org.jclouds.abiquo.reference.AbiquoEdition;
import org.jclouds.abiquo.reference.annotations.EnterpriseEdition;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.NetworkType;
import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.model.enumerator.VlanTagAvailabilityType;
import com.abiquo.server.core.appslibrary.VirtualMachineTemplateDto;
import com.abiquo.server.core.appslibrary.VirtualMachineTemplatesDto;
import com.abiquo.server.core.cloud.HypervisorTypeDto;
import com.abiquo.server.core.cloud.HypervisorTypesDto;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.MachinesDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.abiquo.server.core.infrastructure.RacksDto;
import com.abiquo.server.core.infrastructure.RemoteServicesDto;
import com.abiquo.server.core.infrastructure.UcsRackDto;
import com.abiquo.server.core.infrastructure.UcsRacksDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworkDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworksDto;
import com.abiquo.server.core.infrastructure.network.VlanTagAvailabilityDto;
import com.abiquo.server.core.infrastructure.storage.StorageDeviceDto;
import com.abiquo.server.core.infrastructure.storage.StorageDevicesDto;
import com.abiquo.server.core.infrastructure.storage.TiersDto;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Adds high level functionality to {@link DatacenterDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see API: <a href="http://community.abiquo.com/display/ABI20/Datacenter+Resource">
 *      http://community.abiquo.com/display/ABI20/Datacenter+Resource</a>
 */
public class Datacenter extends DomainWrapper<DatacenterDto>
{
    /**
     * IP address of the datacenter (used to create all remote services with the same ip).
     */
    private String ip;

    /**
     * Indicates the Abiquo edition to create the available remote services.
     * 
     * @see API: <a href="http://community.abiquo.com/display/ABI20/Introduction+-+The+Abiquo+Platform">
     *      http://community.abiquo.com/display/ABI20/Introduction+-+The+Abiquo+Platform</a>
     */
    private AbiquoEdition edition;

    /**
     * Constructor to be used only by the builder.
     */
    protected Datacenter(final AbiquoContext context, final DatacenterDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-DeleteanexistingDatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-DeleteanexistingDatacenter</a>
     */
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteDatacenter(target);
        target = null;
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-CreateanewDatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-CreateanewDatacenter</a>
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Remote+Service+Resource#RemoteServiceResource-CreateanewRemoteService">
     *      http://community.abiquo.com/display/ABI20/Remote+Service+Resource#RemoteServiceResource-CreateanewRemoteService</a>
     */
    public void save()
    {
        // Datacenter must be persisted first, so links get populated in the target object
        target = context.getApi().getInfrastructureClient().createDatacenter(target);

        // If remote services data is set, create remote services.
        if (ip != null && edition != null)
        {
            createRemoteServices();
        }
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-UpdateanexistingDatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-UpdateanexistingDatacenter</a>
     */
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateDatacenter(target);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-ChecktheTagavailability">
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-ChecktheTagavailability</a>
     */
    public VlanTagAvailabilityType checkTagAvailability(final int tag)
    {
        VlanTagAvailabilityDto availability =
            context.getApi().getInfrastructureClient().checkTagAvailability(target, tag);

        return availability.getAvailable();
    }

    // Children access

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-RetrievealistofRacks">
     *      http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-RetrievealistofRacks</a>
     */
    public List<Rack> listRacks()
    {
        RacksDto racks = context.getApi().getInfrastructureClient().listRacks(target);
        return wrap(context, Rack.class, racks.getCollection());
    }

    public List<Rack> listRacks(final Predicate<Rack> filter)
    {
        return Lists.newLinkedList(filter(listRacks(), filter));
    }

    public Rack findRack(final Predicate<Rack> filter)
    {
        return Iterables.getFirst(filter(listRacks(), filter), null);
    }

    public Rack getRack(final Integer id)
    {
        RackDto rack = context.getApi().getInfrastructureClient().getRack(target, id);
        return wrap(context, Rack.class, rack);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-RetrievethelistofUcsRacks">
     *      http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-RetrievethelistofUcsRacks</a>
     */
    public List<ManagedRack> listManagedRacks()
    {
        UcsRacksDto racks = context.getApi().getInfrastructureClient().listManagedRacks(target);
        return wrap(context, ManagedRack.class, racks.getCollection());
    }

    public List<ManagedRack> listManagedRacks(final Predicate<ManagedRack> filter)
    {
        return Lists.newLinkedList(filter(listManagedRacks(), filter));
    }

    public ManagedRack findManagedRack(final Predicate<ManagedRack> filter)
    {
        return Iterables.getFirst(filter(listManagedRacks(), filter), null);
    }

    public ManagedRack getManagedRack(final Integer id)
    {
        UcsRackDto rack = context.getApi().getInfrastructureClient().getManagedRack(target, id);
        return wrap(context, ManagedRack.class, rack);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-RetrievethelistofStorageDevices">
     *      http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-RetrievethelistofStorageDevices</a>
     */
    @EnterpriseEdition
    public List<StorageDevice> listStorageDevices()
    {
        StorageDevicesDto devices =
            context.getApi().getInfrastructureClient().listStorageDevices(target);
        return wrap(context, StorageDevice.class, devices.getCollection());
    }

    @EnterpriseEdition
    public List<StorageDevice> listStorageDevices(final Predicate<StorageDevice> filter)
    {
        return Lists.newLinkedList(filter(listStorageDevices(), filter));
    }

    @EnterpriseEdition
    public StorageDevice findStorageDevice(final Predicate<StorageDevice> filter)
    {
        return Iterables.getFirst(filter(listStorageDevices(), filter), null);
    }

    @EnterpriseEdition
    public StorageDevice getStorageDevice(final Integer id)
    {
        StorageDeviceDto device =
            context.getApi().getInfrastructureClient().getStorageDevice(target, id);
        return wrap(context, StorageDevice.class, device);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Remote+Service+Resource#RemoteServiceResource-RetrievealistofRemoteServices">
     *      http://community.abiquo.com/display/ABI20/Remote+Service+Resource#RemoteServiceResource-RetrievealistofRemoteServices</a>
     */
    public List<RemoteService> listRemoteServices()
    {
        RemoteServicesDto remoteServices =
            context.getApi().getInfrastructureClient().listRemoteServices(target);
        return wrap(context, RemoteService.class, remoteServices.getCollection());
    }

    public List<RemoteService> listRemoteServices(final Predicate<RemoteService> filter)
    {
        return Lists.newLinkedList(filter(listRemoteServices(), filter));
    }

    public RemoteService findRemoteService(final Predicate<RemoteService> filter)
    {
        return Iterables.getFirst(filter(listRemoteServices(), filter), null);
    }

    private void createRemoteServices()
    {
        if (this.edition == AbiquoEdition.ENTERPRISE)
        {
            createRemoteService(RemoteServiceType.BPM_SERVICE);
            createRemoteService(RemoteServiceType.DHCP_SERVICE);
            createRemoteService(RemoteServiceType.STORAGE_SYSTEM_MONITOR);
        }

        createRemoteService(RemoteServiceType.APPLIANCE_MANAGER);
        createRemoteService(RemoteServiceType.VIRTUAL_SYSTEM_MONITOR);
        createRemoteService(RemoteServiceType.NODE_COLLECTOR);
        createRemoteService(RemoteServiceType.TARANTINO);
    }

    private void createRemoteService(final RemoteServiceType type)
    {
        RemoteService.builder(context, this).type(type).ip(this.ip).build().save();
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-Retrievethelistofallocationlimitsinadatacenterforalltheenterprisesusingit">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-Retrievethelistofallocationlimitsinadatacenterforalltheenterprisesusingit</a>
     */
    public List<Limits> listLimits()
    {
        DatacentersLimitsDto dto =
            context.getApi().getInfrastructureClient().listLimits(this.unwrap());
        return DomainWrapper.wrap(context, Limits.class, dto.getCollection());
    }

    public List<Limits> listLimits(final Predicate<Limits> filter)
    {
        return Lists.newLinkedList(filter(listLimits(), filter));
    }

    public Limits findLimits(final Predicate<Limits> filter)
    {
        return Iterables.getFirst(filter(listLimits(), filter), null);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Tier+Resource#TierResource-RetrievethelistofTiers">
     *      http://community.abiquo.com/display/ABI20/Tier+Resource#TierResource-RetrievethelistofTiers</a>
     */
    @EnterpriseEdition
    public List<Tier> listTiers()
    {
        TiersDto dto = context.getApi().getInfrastructureClient().listTiers(this.unwrap());
        return DomainWrapper.wrap(context, Tier.class, dto.getCollection());
    }

    @EnterpriseEdition
    public List<Tier> listTiers(final Predicate<Tier> filter)
    {
        return Lists.newLinkedList(filter(listTiers(), filter));
    }

    @EnterpriseEdition
    public Tier findTier(final Predicate<Tier> filter)
    {
        return Iterables.getFirst(filter(listTiers(), filter), null);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-GetPublic%2CExternalandUnmanagedNetworks">
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-GetPublic%2CExternalandUnmanagedNetworks</a>
     */
    public List<Network< ? >> listNetworks()
    {
        VLANNetworksDto networks = context.getApi().getInfrastructureClient().listNetworks(target);
        return Network.wrapNetworks(context, networks.getCollection());
    }

    public List<Network< ? >> listNetworks(final Predicate<Network< ? >> filter)
    {
        return Lists.newLinkedList(filter(listNetworks(), filter));
    }

    public Network< ? > findNetwork(final Predicate<Network< ? >> filter)
    {
        return Iterables.getFirst(filter(listNetworks(), filter), null);
    }

    public Network< ? > getNetwork(final Integer id)
    {
        VLANNetworkDto network = context.getApi().getInfrastructureClient().getNetwork(target, id);
        return Network.wrapNetwork(context, network);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-GetthelistofPublicNetworks">
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-GetthelistofPublicNetworks</a>
     */
    @EnterpriseEdition
    public List<PublicNetwork> listPublicNetworks()
    {
        NetworkOptions options = NetworkOptions.builder().type(NetworkType.PUBLIC).build();

        VLANNetworksDto networks =
            context.getApi().getInfrastructureClient().listNetworks(target, options);
        return wrap(context, PublicNetwork.class, networks.getCollection());
    }

    @EnterpriseEdition
    public List<PublicNetwork> listPublicNetworks(final Predicate<Network< ? >> filter)
    {
        return Lists.newLinkedList(filter(listPublicNetworks(), filter));
    }

    @EnterpriseEdition
    public PublicNetwork findPublicNetwork(final Predicate<Network< ? >> filter)
    {
        return Iterables.getFirst(filter(listPublicNetworks(), filter), null);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-GetthelistofExternalNetworks">
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-GetthelistofExternalNetworks</a>
     */
    @EnterpriseEdition
    public List<ExternalNetwork> listExternalNetworks()
    {
        NetworkOptions options = NetworkOptions.builder().type(NetworkType.EXTERNAL).build();

        VLANNetworksDto networks =
            context.getApi().getInfrastructureClient().listNetworks(target, options);
        return wrap(context, ExternalNetwork.class, networks.getCollection());
    }

    @EnterpriseEdition
    public List<ExternalNetwork> listExternalNetworks(final Predicate<Network< ? >> filter)
    {
        return Lists.newLinkedList(filter(listExternalNetworks(), filter));
    }

    @EnterpriseEdition
    public ExternalNetwork findExternalNetwork(final Predicate<Network< ? >> filter)
    {
        return Iterables.getFirst(filter(listExternalNetworks(), filter), null);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-GetthelistofUnmanagedNetworks">
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-GetthelistofUnmanagedNetworks</a>
     */
    @EnterpriseEdition
    public List<UnmanagedNetwork> listUnmanagedNetworks()
    {
        NetworkOptions options = NetworkOptions.builder().type(NetworkType.EXTERNAL).build();

        VLANNetworksDto networks =
            context.getApi().getInfrastructureClient().listNetworks(target, options);
        return wrap(context, UnmanagedNetwork.class, networks.getCollection());
    }

    @EnterpriseEdition
    public List<UnmanagedNetwork> listUnmanagedNetworks(final Predicate<Network< ? >> filter)
    {
        return Lists.newLinkedList(filter(listUnmanagedNetworks(), filter));
    }

    @EnterpriseEdition
    public UnmanagedNetwork findUnmanagedNetwork(final Predicate<Network< ? >> filter)
    {
        return Iterables.getFirst(filter(listUnmanagedNetworks(), filter), null);
    }

    // Actions

    /**
     * Retrieve the hypervisor type from remote machine.
     * 
     * @see API: <a href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrievethehypervisortypefromremotemachine"
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-
     *      Retrievethehypervisortypefromremotemachine/a>
     */
    public HypervisorType getHypervisorType(final String ip)
    {
        DatacenterOptions options = DatacenterOptions.builder().ip(ip).build();

        String type =
            context.getApi().getInfrastructureClient()
                .getHypervisorTypeFromMachine(target, options);

        return HypervisorType.valueOf(type);
    }

    /**
     * Retrieve the the list of hypervisor types in the datacenter.
     */
    public List<HypervisorType> listAvailableHypervisors()
    {
        HypervisorTypesDto types =
            context.getApi().getInfrastructureClient().getHypervisorTypes(target);

        return getHypervisorTypes(types);
    }

    @EnterpriseEdition
    public List<HypervisorType> listAvailableHypervisors(final Predicate<HypervisorType> filter)
    {
        return Lists.newLinkedList(filter(listAvailableHypervisors(), filter));
    }

    @EnterpriseEdition
    public HypervisorType findHypervisor(final Predicate<HypervisorType> filter)
    {
        return Iterables.getFirst(filter(listAvailableHypervisors(), filter), null);
    }

    private List<HypervisorType> getHypervisorTypes(final HypervisorTypesDto dtos)
    {
        List<HypervisorType> types = Lists.newArrayList();

        for (HypervisorTypeDto dto : dtos.getCollection())
        {
            types.add(HypervisorType.fromId(dto.getId()));
        }

        return types;
    }

    /**
     * Searches a remote machine and retrieves an Machine object with its information.
     * 
     * @param ip IP address of the remote hypervisor to connect.
     * @param hypervisorType Kind of hypervisor we want to connect. Valid values are {vbox, kvm,
     *            xen-3, vmx-04, hyperv-301, xenserver}.
     * @param user User to log in.
     * @param password Password to authenticate.
     * @return A physical machine if found or <code>null</code>.
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrieveremotemachineinformation">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrieveremotemachineinformation</a>
     */
    public Machine discoverSingleMachine(final String ip, final HypervisorType hypervisorType,
        final String user, final String password)
    {
        return discoverSingleMachine(ip, hypervisorType, user, password, hypervisorType.defaultPort);
    }

    /**
     * Searches a remote machine and retrieves an Machine object with its information.
     * 
     * @param ip IP address of the remote hypervisor to connect.
     * @param hypervisorType Kind of hypervisor we want to connect. Valid values are {vbox, kvm,
     *            xen-3, vmx-04, hyperv-301, xenserver}.
     * @param user User to log in.
     * @param password Password to authenticate.
     * @param port Port to connect.
     * @return A physical machine if found or <code>null</code>.
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrieveremotemachineinformation">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrieveremotemachineinformation</a>
     */
    public Machine discoverSingleMachine(final String ip, final HypervisorType hypervisorType,
        final String user, final String password, final int port)
    {
        MachineDto dto =
            context.getApi().getInfrastructureClient().discoverSingleMachine(target, ip,
                hypervisorType, user, password, MachineOptions.builder().port(port).build());

        // Credentials are not returned by the API
        dto.setUser(user);
        dto.setPassword(password);

        return wrap(context, Machine.class, dto);
    }

    /**
     * Searches multiple remote machines and retrieves an Machine list with its information.
     * 
     * @param ipFrom IP address of the remote first hypervisor to check.
     * @param ipTo IP address of the remote last hypervisor to check.
     * @param hypervisorType Kind of hypervisor we want to connect. Valid values are {vbox, kvm,
     *            xen-3, vmx-04, hyperv-301, xenserver}.
     * @param user User to log in.
     * @param password Password to authenticate.
     * @return The physical machine list.
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrievealistofremotemachineinformation">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrievealistofremotemachineinformation</a>
     */
    public List<Machine> discoverMultipleMachines(final String ipFrom, final String ipTo,
        final HypervisorType hypervisorType, final String user, final String password)
    {
        return discoverMultipleMachines(ipFrom, ipTo, hypervisorType, user, password,
            hypervisorType.defaultPort);
    }

    /**
     * Searches multiple remote machines and retrieves an Machine list with its information.
     * 
     * @param ipFrom IP address of the remote first hypervisor to check.
     * @param ipTo IP address of the remote last hypervisor to check.
     * @param hypervisorType Kind of hypervisor we want to connect. Valid values are {vbox, kvm,
     *            xen-3, vmx-04, hyperv-301, xenserver}.
     * @param user User to log in.
     * @param password Password to authenticate.
     * @param port Port to connect.
     * @return The physical machine list.
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrievealistofremotemachineinformation">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrievealistofremotemachineinformation</a>
     */
    public List<Machine> discoverMultipleMachines(final String ipFrom, final String ipTo,
        final HypervisorType hypervisorType, final String user, final String password,
        final int port)
    {
        MachinesDto dto =
            context.getApi().getInfrastructureClient().discoverMultipleMachines(target, ipFrom,
                ipTo, hypervisorType, user, password, MachineOptions.builder().port(port).build());

        // Credentials are not returned by the API
        for (MachineDto machine : dto.getCollection())
        {
            machine.setUser(user);
            machine.setPassword(password);
        }

        return wrap(context, Machine.class, dto.getCollection());
    }

    public List<VirtualMachineTemplate> listTemplatesInRepository(final Enterprise enterprise)
    {
        VirtualMachineTemplatesDto dto =
            context.getApi().getVirtualMachineTemplateClient().listVirtualMachineTemplates(
                enterprise.getId(), target.getId());
        return wrap(context, VirtualMachineTemplate.class, dto.getCollection());
    }

    public List<VirtualMachineTemplate> listTemplatesInRepository(final Enterprise enterprise,
        final Predicate<VirtualMachineTemplate> filter)
    {
        return Lists.newLinkedList(filter(listTemplatesInRepository(enterprise), filter));
    }

    public VirtualMachineTemplate findTemplateInRepository(final Enterprise enterprise,
        final Predicate<VirtualMachineTemplate> filter)
    {
        return Iterables.getFirst(filter(listTemplatesInRepository(enterprise), filter), null);
    }

    public VirtualMachineTemplate getTemplateInRepository(final Enterprise enterprise,
        final Integer id)
    {
        VirtualMachineTemplateDto template =
            context.getApi().getVirtualMachineTemplateClient().getVirtualMachineTemplate(
                enterprise.getId(), target.getId(), id);
        return wrap(context, VirtualMachineTemplate.class, template);
    }

    // Builder

    public static Builder builder(final AbiquoContext context)
    {
        return new Builder(context);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private String name;

        private String location;

        private String ip;

        private AbiquoEdition edition;

        public Builder(final AbiquoContext context)
        {
            super();
            this.context = context;
        }

        public Builder remoteServices(final String ip, final AbiquoEdition edition)
        {
            this.ip = ip;
            this.edition = edition;
            return this;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder location(final String location)
        {
            this.location = location;
            return this;
        }

        public Datacenter build()
        {
            DatacenterDto dto = new DatacenterDto();
            dto.setName(name);
            dto.setLocation(location);
            Datacenter datacenter = new Datacenter(context, dto);
            datacenter.edition = edition;
            datacenter.ip = ip;
            return datacenter;
        }

        public static Builder fromDatacenter(final Datacenter in)
        {
            return Datacenter.builder(in.context).name(in.getName()).location(in.getLocation());
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getLocation()
    {
        return target.getLocation();
    }

    public String getName()
    {
        return target.getName();
    }

    public void setLocation(final String location)
    {
        target.setLocation(location);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public String getUUID()
    {
        return target.getUuid();
    }

    @Override
    public String toString()
    {
        return "Datacenter [id=" + getId() + ", location=" + getLocation() + ", name=" + getName()
            + ", uuid=" + getUUID() + "]";
    }

}
