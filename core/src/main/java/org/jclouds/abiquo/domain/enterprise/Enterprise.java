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
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;

import com.abiquo.server.core.appslibrary.VirtualMachineTemplatesDto;
import com.abiquo.server.core.enterprise.DatacenterLimitsDto;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.enterprise.RolesDto;
import com.abiquo.server.core.enterprise.UserDto;
import com.abiquo.server.core.enterprise.UsersDto;
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
            context.getApi().getVirtualMachineTemplateClient()
                .listVirtualMachineTemplates(target.getId(), datacenter.getId());
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

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Repository+Resource#DatacenterRepositoryResource-SynchronizetheDatacenterRepositorywiththerepository">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Repository+Resource#DatacenterRepositoryResource-SynchronizetheDatacenterRepositorywiththerepository</a>
     */
    public void refreshTemplateRepository(final Datacenter datacenter)
    {
        context.getApi().getEnterpriseClient()
            .refreshTemplateRepository(target.getId(), datacenter.getId());
    }

    // Actions

    /**
     * Allows the given datacenter to be used by this enterprise. Creates a {@link DatacenterLimits}
     * object.
     * 
     * @param datacenter The datacenter.
     * @return Default datacenter limits of the enterprise for the given datacenter.
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-CreateanewLimitforanenterpriseinadatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Limits+Resource#DatacenterLimitsResource-CreateanewLimitforanenterpriseinadatacenter</a>
     */
    public Limits allowDatacenter(final Datacenter datacenter)
    {
        // Create new limits
        Limits limits = Limits.builder(context).build();

        // Save new limits
        DatacenterLimitsDto dto =
            context.getApi().getEnterpriseClient()
                .createLimits(this.unwrap(), datacenter.unwrap(), limits.unwrap());

        return wrap(context, Limits.class, dto);
    }

    /**
     * Prohibe the given datacenter to be used by this enterprise. Deletes a
     * {@link DatacenterLimits} object.
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

            return new Enterprise(context, dto);
        }

        public static Builder fromEnterprise(final Enterprise in)
        {
            return Enterprise.builder(in.context).name(in.getName())
                .ramLimits(in.getRamSoftLimitInMb(), in.getRamHardLimitInMb())
                .cpuCountLimits(in.getCpuCountSoftLimit(), in.getCpuCountHardLimit())
                .hdLimitsInMb(in.getHdSoftLimitInMb(), in.getHdHardLimitInMb())
                .storageLimits(in.getStorageSoft(), in.getStorageHard())
                .vlansLimits(in.getVlansSoft(), in.getVlansHard())
                .publicIpsLimits(in.getPublicIpsSoft(), in.getPublicIpsHard())
                .repositoryLimits(in.getRepositorySoft(), in.getRepositoryHard())
                .isReservationRestricted(in.getIsReservationRestricted());
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

}
