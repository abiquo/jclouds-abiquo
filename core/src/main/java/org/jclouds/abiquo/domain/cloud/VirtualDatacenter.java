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

package org.jclouds.abiquo.domain.cloud;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.filter;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWithLimitsWrapper;
import org.jclouds.abiquo.domain.builder.LimitsBuilder;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.Tier;
import org.jclouds.abiquo.domain.network.Network;
import org.jclouds.abiquo.reference.ValidationErrors;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualAppliancesDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.abiquo.server.core.infrastructure.storage.TiersDto;
import com.abiquo.server.core.infrastructure.storage.VolumeManagementDto;
import com.abiquo.server.core.infrastructure.storage.VolumesManagementDto;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Adds high level functionality to {@link VirtualDatacenterDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource">
 *      http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource</a>
 */
public class VirtualDatacenter extends DomainWithLimitsWrapper<VirtualDatacenterDto>
{
    /** The enterprise where the rack belongs. */
    // Package protected to allow navigation from children
    Enterprise enterprise;

    /** The dataceter where the virtual datacenter will be deployed. */
    // Package protected to allow navigation from children
    Datacenter datacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected VirtualDatacenter(final AbiquoContext context, final VirtualDatacenterDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-DeleteanexistingVirtualDatacenter">
     *      http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-DeleteanexistingVirtualDatacenter</a>
     */
    public void delete()
    {
        context.getApi().getCloudClient().deleteVirtualDatacenter(target);
        target = null;
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-CreateanewVirtualDatacenter">
     *      http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-CreateanewVirtualDatacenter</a>
     */
    public void save()
    {
        Integer datacenterId = datacenter.unwrap().getIdFromLink("edit");
        Integer enterpriseId = enterprise.unwrap().getIdFromLink("edit");

        target =
            context.getApi().getCloudClient()
                .createVirtualDatacenter(target, datacenterId, enterpriseId);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-UpdatesanexistingVirtualDatacenter">
     *      http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-UpdatesanexistingVirtualDatacenter</a>
     */
    public void update()
    {
        target = context.getApi().getCloudClient().updateVirtualDatacenter(target);
    }

    // Children access

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Virtual+Appliance+Resource#VirtualApplianceResource-RetrievethelistofVirtualAppliances">
     *      http://community.abiquo.com/display/ABI20/Virtual+Appliance+Resource#VirtualApplianceResource-RetrievethelistofVirtualAppliances</a>
     */
    public List<VirtualAppliance> listVirtualAppliances()
    {
        VirtualAppliancesDto vapps =
            context.getApi().getCloudClient().listVirtualAppliances(target);
        return wrap(context, VirtualAppliance.class, vapps.getCollection());
    }

    public List<VirtualAppliance> listVirtualAppliances(final Predicate<VirtualAppliance> filter)
    {
        return Lists.newLinkedList(filter(listVirtualAppliances(), filter));
    }

    public VirtualAppliance findVirtualAppliance(final Predicate<VirtualAppliance> filter)
    {
        return Iterables.getFirst(filter(listVirtualAppliances(), filter), null);
    }

    public VirtualAppliance getVirtualAppliance(final Integer id)
    {
        VirtualApplianceDto vapp =
            context.getApi().getCloudClient().getVirtualAppliance(target, id);
        return wrap(context, VirtualAppliance.class, vapp);
    }

    // TODO: Wiki link

    public List<Tier> listStorageTiers()
    {
        TiersDto tiers = context.getApi().getCloudClient().listStorageTiers(target);
        return wrap(context, Tier.class, tiers.getCollection());
    }

    public List<Tier> listStorageTiers(final Predicate<Tier> filter)
    {
        return Lists.newLinkedList(filter(listStorageTiers(), filter));
    }

    public Tier findTier(final Predicate<Tier> filter)
    {
        return Iterables.getFirst(filter(listStorageTiers(), filter), null);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Volume+Resource#VolumeResource-Retrievethelistofvolumes">
     *      http://community.abiquo.com/display/ABI20/Volume+Resource#VolumeResource-Retrievethelistofvolumes</a>
     */
    public List<Volume> listVolumes()
    {
        VolumesManagementDto volumes = context.getApi().getCloudClient().listVolumes(target);
        return wrap(context, Volume.class, volumes.getCollection());
    }

    public List<Volume> listVolumes(final Predicate<Volume> filter)
    {
        return Lists.newLinkedList(filter(listVolumes(), filter));
    }

    public Volume findVolume(final Predicate<Volume> filter)
    {
        return Iterables.getFirst(filter(listVolumes(), filter), null);
    }

    public Volume getVolume(final Integer id)
    {
        VolumeManagementDto volume = context.getApi().getCloudClient().getVolume(target, id);
        return wrap(context, Volume.class, volume);
    }

    // Builder

    public static Builder builder(final AbiquoContext context, final Datacenter datacenter,
        final Enterprise enterprise)
    {
        return new Builder(context, datacenter, enterprise);
    }

    public static class Builder extends LimitsBuilder<Builder>
    {
        private AbiquoContext context;

        private String name;

        private HypervisorType hypervisorType;

        private Enterprise enterprise;

        private Datacenter datacenter;

        private Network network;

        public Builder(final AbiquoContext context, final Datacenter datacenter,
            final Enterprise enterprise)
        {
            super();
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Datacenter.class);
            this.datacenter = datacenter;
            checkNotNull(enterprise, ValidationErrors.NULL_RESOURCE + Enterprise.class);
            this.enterprise = enterprise;
            this.context = context;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder hypervisorType(final HypervisorType hypervisorType)
        {
            this.hypervisorType = hypervisorType;
            return this;
        }

        public Builder datacenter(final Datacenter datacenter)
        {
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Datacenter.class);
            this.datacenter = datacenter;
            return this;
        }

        public Builder enterprise(final Enterprise enterprise)
        {
            checkNotNull(enterprise, ValidationErrors.NULL_RESOURCE + Enterprise.class);
            this.enterprise = enterprise;
            return this;
        }

        public Builder network(final Network network)
        {
            checkNotNull(network, ValidationErrors.NULL_RESOURCE + Network.class);
            this.network = network;
            return this;
        }

        public VirtualDatacenter build()
        {
            VirtualDatacenterDto dto = new VirtualDatacenterDto();
            dto.setName(name);
            dto.setRamLimitsInMb(ramSoftLimitInMb, ramHardLimitInMb);
            dto.setCpuCountLimits(cpuCountSoftLimit, cpuCountHardLimit);
            dto.setHdLimitsInMb(hdSoftLimitInMb, hdHardLimitInMb);
            dto.setStorageLimits(storageSoft, storageHard);
            dto.setVlansLimits(vlansSoft, vlansHard);
            dto.setPublicIPLimits(publicIpsSoft, publicIpsHard);
            dto.setName(name);
            dto.setHypervisorType(hypervisorType);
            dto.setVlan(network.unwrap());

            VirtualDatacenter virtualDatacenter = new VirtualDatacenter(context, dto);
            virtualDatacenter.datacenter = datacenter;
            virtualDatacenter.enterprise = enterprise;

            return virtualDatacenter;
        }

        public static Builder fromVirtualDatacenter(final VirtualDatacenter in)
        {
            return VirtualDatacenter.builder(in.context, in.datacenter, in.enterprise)
                .name(in.getName()).ramLimits(in.getRamSoftLimitInMb(), in.getRamHardLimitInMb())
                .cpuCountLimits(in.getCpuCountSoftLimit(), in.getCpuCountHardLimit())
                .hdLimitsInMb(in.getHdSoftLimitInMb(), in.getHdHardLimitInMb())
                .storageLimits(in.getStorageSoft(), in.getStorageHard())
                .vlansLimits(in.getVlansSoft(), in.getVlansHard())
                .publicIpsLimits(in.getPublicIpsSoft(), in.getPublicIpsHard())
                .network(in.getNetwork()).hypervisorType(in.getHypervisorType());
        }
    }

    // Delegate methods

    public HypervisorType getHypervisorType()
    {
        return target.getHypervisorType();
    }

    public Integer getId()
    {
        return target.getId();
    }

    public String getName()
    {
        return target.getName();
    }

    public void setHypervisorType(final HypervisorType hypervisorType)
    {
        target.setHypervisorType(hypervisorType);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public Network getNetwork()
    {
        return wrap(context, Network.class, target.getVlan());
    }

    public void setNetwork(final Network network)
    {
        target.setVlan(network.unwrap());
    }
}
