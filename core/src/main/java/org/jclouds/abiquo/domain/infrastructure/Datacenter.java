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
import org.jclouds.abiquo.domain.enterprise.Limits;
import org.jclouds.abiquo.domain.infrastructure.options.MachineOptions;
import org.jclouds.abiquo.reference.AbiquoEdition;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.MachinesDto;
import com.abiquo.server.core.infrastructure.RacksDto;
import com.abiquo.server.core.infrastructure.RemoteServicesDto;
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
 * @see <a href="http://community.abiquo.com/display/ABI20/Datacenter+Resource">
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
     * @see <a href="http://community.abiquo.com/display/ABI20/Introduction+-+The+Abiquo+Platform">
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
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-DeleteanexistingDatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-DeleteanexistingDatacenter</a>
     */
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteDatacenter(target);
        target = null;
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-CreateanewDatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-CreateanewDatacenter</a>
     *@see <a
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
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-UpdateanexistingDatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-UpdateanexistingDatacenter</a>
     */
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateDatacenter(target);
    }

    // Children access

    /**
     * @see <a
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

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-RetrievethelistofStorageDevices">
     *      http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-RetrievethelistofStorageDevices</a>
     */
    public List<StorageDevice> listStorageDevices()
    {
        StorageDevicesDto devices =
            context.getApi().getInfrastructureClient().listStorageDevices(target);
        return wrap(context, StorageDevice.class, devices.getCollection());
    }

    public List<StorageDevice> listStorageDevices(final Predicate<StorageDevice> filter)
    {
        return Lists.newLinkedList(filter(listStorageDevices(), filter));
    }

    public StorageDevice findStorageDevice(final Predicate<StorageDevice> filter)
    {
        return Iterables.getFirst(filter(listStorageDevices(), filter), null);
    }

    /**
     * @see <a
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
        createRemoteService(RemoteServiceType.VIRTUAL_FACTORY);
        createRemoteService(RemoteServiceType.VIRTUAL_SYSTEM_MONITOR);
        createRemoteService(RemoteServiceType.NODE_COLLECTOR);
    }

    private void createRemoteService(final RemoteServiceType type)
    {
        RemoteService.builder(context, this).type(type).ip(this.ip).build().save();
    }

    /**
     * @see <a
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
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Tier+Resource#TierResource-RetrievethelistofTiers">
     *      http://community.abiquo.com/display/ABI20/Tier+Resource#TierResource-RetrievethelistofTiers</a>
     */
    public List<Tier> listTiers()
    {
        TiersDto dto = context.getApi().getInfrastructureClient().listTiers(this.unwrap());
        return DomainWrapper.wrap(context, Tier.class, dto.getCollection());
    }

    public List<Tier> listTiers(final Predicate<Tier> filter)
    {
        return Lists.newLinkedList(filter(listTiers(), filter));
    }

    public Tier findTier(final Predicate<Tier> filter)
    {
        return Iterables.getFirst(filter(listTiers(), filter), null);
    }

    // Actions

    /**
     * Searches a remote machine and retrieves an Machine object with its information.
     * 
     * @param ip IP address of the remote hypervisor to connect.
     * @param hypervisorType Kind of hypervisor we want to connect. Valid values are {vbox, kvm,
     *            xen-3, vmx-04, hyperv-301, xenserver}.
     * @param user User to log in.
     * @param password Password to authenticate.
     * @return A physical machine if found or <code>null</code>.
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrieveremotemachineinformation">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrieveremotemachineinformation</a>
     */
    public Machine discoverSingleMachine(final String ip, final HypervisorType hypervisorType,
        final String user, final String password)
    {
        MachineDto dto =
            context.getApi().getInfrastructureClient().discoverSingleMachine(target, ip,
                hypervisorType, user, password);

        return wrap(context, Machine.class, dto);
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
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrieveremotemachineinformation">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrieveremotemachineinformation</a>
     */
    public Machine discoverSingleMachine(final String ip, final HypervisorType hypervisorType,
        final String user, final String password, final int port)
    {
        MachineDto dto =
            context.getApi().getInfrastructureClient().discoverSingleMachine(target, ip,
                hypervisorType, user, password, MachineOptions.builder().port(port).build());

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
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrievealistofremotemachineinformation">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-Retrievealistofremotemachineinformation</a>
     */
    public List<Machine> discoverMultipleMachines(final String ipFrom, final String ipTo,
        final HypervisorType hypervisorType, final String user, final String password)
    {
        MachinesDto dto =
            context.getApi().getInfrastructureClient().discoverMultipleMachines(target, ipFrom,
                ipTo, hypervisorType, user, password);

        return wrap(context, Machine.class, dto.getCollection());
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
     * @see <a
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

        return wrap(context, Machine.class, dto.getCollection());
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

}
