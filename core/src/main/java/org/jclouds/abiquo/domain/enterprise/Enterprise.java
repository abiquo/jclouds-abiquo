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

package org.jclouds.abiquo.domain.enterprise;

import static com.google.common.collect.Iterables.filter;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWithLimitsWrapper;
import org.jclouds.abiquo.domain.builder.LimitsBuilder;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.Machine;
import org.jclouds.abiquo.domain.network.ExternalNetwork;
import org.jclouds.abiquo.domain.network.Network;
import org.jclouds.abiquo.reference.annotations.EnterpriseEdition;
import org.jclouds.abiquo.strategy.enterprise.ListVirtualMachineTemplates;

import com.abiquo.server.core.appslibrary.VirtualMachineTemplateDto;
import com.abiquo.server.core.appslibrary.VirtualMachineTemplatesDto;
import com.abiquo.server.core.cloud.VirtualDatacentersDto;
import com.abiquo.server.core.cloud.VirtualMachinesDto;
import com.abiquo.server.core.enterprise.DatacenterLimitsDto;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.enterprise.RolesDto;
import com.abiquo.server.core.enterprise.UserDto;
import com.abiquo.server.core.enterprise.UsersDto;
import com.abiquo.server.core.infrastructure.DatacentersDto;
import com.abiquo.server.core.infrastructure.MachinesDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworksDto;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Adds high level functionality to {@link EnterpriseDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Enterprise+Resource">
 *      http://community.abiquo.com/display/ABI20/Enterprise+Resource</a>
 */
public class Enterprise extends DomainWithLimitsWrapper<EnterpriseDto>
{
    /** The default value for the reservation restricted flag. */
    private static final boolean DEFAULT_RESERVATION_RESTRICTED = false;

    /**
     * Constructor to be used only by the builder.
     */
    protected Enterprise(final AbiquoContext context, final EnterpriseDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-DeleteanexistingEnterprise">
     *      http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-DeleteanexistingEnterprise</a>
     */
    public void delete()
    {
        context.getApi().getEnterpriseClient().deleteEnterprise(target);
        target = null;
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-CreateanewEnterprise">
     *      http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-CreateanewEnterprise</a>
     */
    public void save()
    {
        target = context.getApi().getEnterpriseClient().createEnterprise(target);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-UpdatesanexistingEnterprise">
     *      http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-UpdatesanexistingEnterprise</a>
     */
    public void update()
    {
        target = context.getApi().getEnterpriseClient().updateEnterprise(target);
    }

    // Children access

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-RetrievealistofVirtualDatacenters">
     *      http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-RetrievealistofVirtualDatacenters</a>
     */
    public List<VirtualDatacenter> listVirtualDatacenters()
    {
        VirtualDatacentersDto dto =
            context.getApi().getEnterpriseClient().listVirtualDatacenters(target);
        return wrap(context, VirtualDatacenter.class, dto.getCollection());
    }

    public List<VirtualDatacenter> listVirtualDatacenters(final Predicate<VirtualDatacenter> filter)
    {
        return Lists.newLinkedList(filter(listVirtualDatacenters(), filter));
    }

    public VirtualDatacenter findVirtualDatacenter(final Predicate<VirtualDatacenter> filter)
    {
        return Iterables.getFirst(filter(listVirtualDatacenters(), filter), null);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-Retrievethelistofallocationlimitsinaallthedatacentersforacertainenterprise">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-Retrievethelistofallocationlimitsinaallthedatacentersforacertainenterprise</a>
     */
    public List<Limits> listLimits()
    {
        DatacentersLimitsDto dto = context.getApi().getEnterpriseClient().listLimits(this.unwrap());
        return wrap(context, Limits.class, dto.getCollection());
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
     *      href="http://community.abiquo.com/display/ABI20/User+resource#Userresource-Retrievealistofusers">
     *      http://community.abiquo.com/display/ABI20/User+resource#Userresource-Retrievealistofusers</a>
     */
    public List<User> listUsers()
    {
        UsersDto dto = context.getApi().getEnterpriseClient().listUsers(this.unwrap());
        return wrap(context, User.class, dto.getCollection());
    }

    public List<User> listUsers(final Predicate<User> filter)
    {
        return Lists.newLinkedList(filter(listUsers(), filter));
    }

    public User findUser(final Predicate<User> filter)
    {
        return Iterables.getFirst(filter(listUsers(), filter), null);
    }

    public User getUser(final Integer id)
    {
        UserDto user = context.getApi().getEnterpriseClient().getUser(target, id);
        return wrap(context, User.class, user);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Roles+Resource#RolesResource-Retrievealistofroles">
     *      http://community.abiquo.com/display/ABI20/Roles+Resource#RolesResource-Retrievealistofroles</a>
     */
    public List<Role> listRoles()
    {
        RolesDto dto = context.getApi().getAdminClient().listRoles(target);
        return wrap(context, Role.class, dto.getCollection());
    }

    public List<Role> listRoles(final Predicate<Role> filter)
    {
        return Lists.newLinkedList(filter(listRoles(), filter));
    }

    public Role findRole(final Predicate<Role> filter)
    {
        return Iterables.getFirst(filter(listRoles(), filter), null);
    }

    public List<VirtualMachineTemplate> listTemplatesInRepository(final Datacenter datacenter)
    {
        VirtualMachineTemplatesDto dto =
            context.getApi().getVirtualMachineTemplateClient().listVirtualMachineTemplates(
                target.getId(), datacenter.getId());
        return wrap(context, VirtualMachineTemplate.class, dto.getCollection());
    }

    public List<VirtualMachineTemplate> listTemplatesInRepository(final Datacenter datacenter,
        final Predicate<VirtualMachineTemplate> filter)
    {
        return Lists.newLinkedList(filter(listTemplatesInRepository(datacenter), filter));
    }

    public VirtualMachineTemplate findTemplateInRepository(final Datacenter datacenter,
        final Predicate<VirtualMachineTemplate> filter)
    {
        return Iterables.getFirst(filter(listTemplatesInRepository(datacenter), filter), null);
    }

    public VirtualMachineTemplate getTemplateInRepository(final Datacenter datacenter,
        final Integer id)
    {
        VirtualMachineTemplateDto template =
            context.getApi().getVirtualMachineTemplateClient().getVirtualMachineTemplate(
                target.getId(), datacenter.getId(), id);
        return wrap(context, VirtualMachineTemplate.class, template);
    }

    public List<VirtualMachineTemplate> listTemplates()
    {
        ListVirtualMachineTemplates strategy =
            context.getUtils().getInjector().getInstance(ListVirtualMachineTemplates.class);
        return Lists.newLinkedList(strategy.execute(this));
    }

    public List<VirtualMachineTemplate> listTemplates(final Predicate<VirtualMachineTemplate> filter)
    {
        ListVirtualMachineTemplates strategy =
            context.getUtils().getInjector().getInstance(ListVirtualMachineTemplates.class);
        return Lists.newLinkedList(strategy.execute(this, filter));
    }

    public VirtualMachineTemplate findTemplate(final Predicate<VirtualMachineTemplate> filter)
    {
        ListVirtualMachineTemplates strategy =
            context.getUtils().getInjector().getInstance(ListVirtualMachineTemplates.class);
        return Iterables.getFirst(strategy.execute(this, filter), null);
    }

    public List<Datacenter> listAllowedDatacenters()
    {
        DatacentersDto datacenters =
            context.getApi().getEnterpriseClient().listAllowedDatacenters(target.getId());
        return wrap(context, Datacenter.class, datacenters.getCollection());
    }

    public List<Datacenter> listAllowedDatacenters(final Predicate<Datacenter> filter)
    {
        return Lists.newLinkedList(filter(listAllowedDatacenters(), filter));
    }

    public Datacenter findAllowedDatacenter(final Predicate<Datacenter> filter)
    {
        return Iterables.getFirst(filter(listAllowedDatacenters(), filter), null);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-Getthelistofexternalnetworks">
     *      http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-Getthelistofexternalnetworks</a>
     */
    @EnterpriseEdition
    public List<ExternalNetwork> listExternalNetworks()
    {
        VLANNetworksDto networks =
            context.getApi().getEnterpriseClient().listExternalNetworks(target);
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
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-RetrievethelistofvirtualmachinesbyanEnterprise">
     *      http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-RetrievethelistofvirtualmachinesbyanEnterprise</a>
     */
    public List<VirtualMachine> listVirtualMachines()
    {
        VirtualMachinesDto machines =
            context.getApi().getEnterpriseClient().listVirtualMachines(target);
        return wrap(context, VirtualMachine.class, machines.getCollection());
    }

    public List<VirtualMachine> listVirtualMachines(final Predicate<VirtualMachine> filter)
    {
        return Lists.newLinkedList(filter(listVirtualMachines(), filter));
    }

    public VirtualMachine findVirtualMachine(final Predicate<VirtualMachine> filter)
    {
        return Iterables.getFirst(filter(listVirtualMachines(), filter), null);
    }

    public List<Machine> listReservedMachines()
    {
        MachinesDto machines = context.getApi().getEnterpriseClient().listReservedMachines(target);
        return wrap(context, Machine.class, machines.getCollection());
    }

    public List<VirtualMachine> listReservedMachines(final Predicate<VirtualMachine> filter)
    {
        return Lists.newLinkedList(filter(listVirtualMachines(), filter));
    }

    public VirtualMachine findReservedMachine(final Predicate<VirtualMachine> filter)
    {
        return Iterables.getFirst(filter(listVirtualMachines(), filter), null);
    }

    // Actions

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Repository+Resource#DatacenterRepositoryResource-SynchronizetheDatacenterRepositorywiththerepository">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Repository+Resource#DatacenterRepositoryResource-SynchronizetheDatacenterRepositorywiththerepository</a>
     */
    public void refreshTemplateRepository(final Datacenter datacenter)
    {
        context.getApi().getEnterpriseClient().refreshTemplateRepository(target.getId(),
            datacenter.getId());
    }

    /**
     * Allows the given datacenter to be used by this enterprise. Creates a {@link DatacenterLimits}
     * object if not exists.
     * 
     * @param datacenter The datacenter.
     * @return Default datacenter limits of the enterprise for the given datacenter.
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-CreateanewLimitforanenterpriseinadatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-CreateanewLimitforanenterpriseinadatacenter</a>
     */
    public Limits allowDatacenter(final Datacenter datacenter)
    {
        DatacenterLimitsDto dto;

        try
        {
            // Create new limits
            Limits limits = Limits.builder(context).build();

            // Save new limits
            dto =
                context.getApi().getEnterpriseClient().createLimits(target, datacenter.unwrap(),
                    limits.unwrap());
        }
        catch (AbiquoException ex)
        {
            // Controlled error to allow duplicated authorizations
            if (ex.hasError("LIMIT-7"))
            {
                dto =
                    context.getApi().getEnterpriseClient().getLimits(target, datacenter.unwrap())
                        .getCollection().get(0);
            }
            else
            {
                throw ex;
            }
        }

        return wrap(context, Limits.class, dto);
    }

    /**
     * Prohibe the given datacenter to be used by this enterprise. Deletes a {@link Limits} object.
     * 
     * @param datacenter The datacenter.
     * @return Default datacenter limits of the enterprise for the given datacenter.
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-Deleteanexistinglimitforanenterpriseinadatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-Deleteanexistinglimitforanenterpriseinadatacenter</a>
     */
    public void prohibitDatacenter(final Datacenter datacenter)
    {
        // Get limits
        DatacentersLimitsDto dto =
            context.getApi().getEnterpriseClient().getLimits(this.unwrap(), datacenter.unwrap());

        // Delete limits (if any)
        if (dto != null && !dto.isEmpty())
        {
            // Should be only one limit
            context.getApi().getEnterpriseClient().deleteLimits(dto.getCollection().get(0));
        }
    }

    /**
     * Disables chef in the enterprise.
     * 
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-DisableChefinanexistingEnterprise">
     *      http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-DisableChefinanexistingEnterprise</a>
     */
    public void disableChef()
    {
        target.setChefClient(null);
        target.setChefClientCertificate(null);
        target.setChefURL(null);
        target.setChefValidator(null);
        target.setChefValidatorCertificate(null);
        update();
    }

    // Builder

    public static Builder builder(final AbiquoContext context)
    {
        return new Builder(context);
    }

    public static class Builder extends LimitsBuilder<Builder>
    {
        private AbiquoContext context;

        private String name;

        protected Long repositorySoft = Long.valueOf(DEFAULT_LIMITS);

        protected Long repositoryHard = Long.valueOf(DEFAULT_LIMITS);

        private Boolean isReservationRestricted = DEFAULT_RESERVATION_RESTRICTED;

        private String chefURL;

        private String chefClient;

        private String chefValidator;

        private String chefClientCertificate;

        private String chefValidatorCertificate;

        public Builder(final AbiquoContext context)
        {
            super();
            this.context = context;
        }

        public Builder isReservationRestricted(final boolean isReservationRestricted)
        {
            this.isReservationRestricted = isReservationRestricted;
            return this;
        }

        public Builder chefURL(final String chefURL)
        {
            this.chefURL = chefURL;
            return this;
        }

        public Builder chefClient(final String chefClient)
        {
            this.chefClient = chefClient;
            return this;
        }

        public Builder chefClientCertificate(final String chefClientCertificate)
        {
            this.chefClientCertificate = chefClientCertificate;
            return this;
        }

        public Builder chefValidator(final String chefValidator)
        {
            this.chefValidator = chefValidator;
            return this;
        }

        public Builder chefValidatorCertificate(final String chefValidatorCertificate)
        {
            this.chefValidatorCertificate = chefValidatorCertificate;
            return this;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder repositoryLimits(final long soft, final long hard)
        {
            this.repositorySoft = soft;
            this.repositoryHard = hard;
            return this;
        }

        public Enterprise build()
        {
            EnterpriseDto dto = new EnterpriseDto();
            dto.setName(name);
            dto.setRamLimitsInMb(ramSoftLimitInMb, ramHardLimitInMb);
            dto.setCpuCountLimits(cpuCountSoftLimit, cpuCountHardLimit);
            dto.setHdLimitsInMb(hdSoftLimitInMb, hdHardLimitInMb);
            dto.setStorageLimits(storageSoft, storageHard);
            dto.setVlansLimits(vlansSoft, vlansHard);
            dto.setPublicIPLimits(publicIpsSoft, publicIpsHard);
            dto.setRepositoryLimits(repositorySoft, repositoryHard);
            dto.setIsReservationRestricted(isReservationRestricted);
            dto.setChefClient(chefClient);
            dto.setChefClientCertificate(chefClientCertificate);
            dto.setChefURL(chefURL);
            dto.setChefValidator(chefValidator);
            dto.setChefValidatorCertificate(chefValidatorCertificate);

            return new Enterprise(context, dto);
        }

        public static Builder fromEnterprise(final Enterprise in)
        {
            return Enterprise.builder(in.context).name(in.getName()).ramLimits(
                in.getRamSoftLimitInMb(), in.getRamHardLimitInMb()).cpuCountLimits(
                in.getCpuCountSoftLimit(), in.getCpuCountHardLimit()).hdLimitsInMb(
                in.getHdSoftLimitInMb(), in.getHdHardLimitInMb()).storageLimits(
                in.getStorageSoft(), in.getStorageHard()).vlansLimits(in.getVlansSoft(),
                in.getVlansHard()).publicIpsLimits(in.getPublicIpsSoft(), in.getPublicIpsHard())
                .repositoryLimits(in.getRepositorySoft(), in.getRepositoryHard())
                .isReservationRestricted(in.getIsReservationRestricted()).chefClient(
                    in.getChefClient()).chefClientCertificate(in.getChefClientCertificate())
                .chefURL(in.getChefURL()).chefValidator(in.getChefValidator())
                .chefValidatorCertificate(in.getChefValidatorCertificate());
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public boolean getIsReservationRestricted()
    {
        return target.getIsReservationRestricted();
    }

    public String getName()
    {
        return target.getName();
    }

    public long getRepositoryHard()
    {
        return target.getRepositoryHard();
    }

    public long getRepositorySoft()
    {
        return target.getRepositorySoft();
    }

    public void setIsReservationRestricted(final boolean isReservationRestricted)
    {
        target.setIsReservationRestricted(isReservationRestricted);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setRepositoryHard(final long repositoryHard)
    {
        target.setRepositoryHard(repositoryHard);
    }

    public void setRepositoryLimits(final long soft, final long hard)
    {
        target.setRepositoryLimits(soft, hard);
    }

    public void setRepositorySoft(final long repositorySoft)
    {
        target.setRepositorySoft(repositorySoft);
    }

    public String getChefClient()
    {
        return target.getChefClient();
    }

    public String getChefClientCertificate()
    {
        return target.getChefClientCertificate();
    }

    public String getChefURL()
    {
        return target.getChefURL();
    }

    public String getChefValidator()
    {
        return target.getChefValidator();
    }

    public String getChefValidatorCertificate()
    {
        return target.getChefValidatorCertificate();
    }

    public void setChefClient(final String chefClient)
    {
        target.setChefClient(chefClient);
    }

    public void setChefClientCertificate(final String chefClientCertificate)
    {
        target.setChefClientCertificate(chefClientCertificate);
    }

    public void setChefURL(final String chefURL)
    {
        target.setChefURL(chefURL);
    }

    public void setChefValidator(final String chefValidator)
    {
        target.setChefValidator(chefValidator);
    }

    public void setChefValidatorCertificate(final String chefValidatorCertificate)
    {
        target.setChefValidatorCertificate(chefValidatorCertificate);
    }

    @Override
    public String toString()
    {
        return "Enterprise [id=" + getId() + ", isReservationRestricted="
            + getIsReservationRestricted() + ", name=" + getName() + "]";
    }
}
