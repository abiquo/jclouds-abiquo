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

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;

import com.abiquo.server.core.enterprise.EnterpriseDto;

/**
 * Adds high level functionality to {@link EnterpriseDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI18/Enterprise+Resource
 */
public class Enterprise extends DomainWrapper<EnterpriseDto>
{
    /** The default limits for enterprises (unlimited). */
    private static final int DEFAULT_LIMITS = 0;

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

    public void delete()
    {
        context.getApi().getEnterpriseClient().deleteEnterprise(target);
        target = null;
    }

    public void save()
    {
        target = context.getApi().getEnterpriseClient().createEnterprise(target);
    }

    public void update()
    {
        target = context.getApi().getEnterpriseClient().updateEnterprise(target);
    }

    // Children access

    // Actions

    public void allowDatacenter(final Datacenter datacenter)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void prohibeDatacenter(final Datacenter datacenter)
    {
        throw new UnsupportedOperationException("Not implemented yet");
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

        private Integer ramSoftLimitInMb = DEFAULT_LIMITS;

        private Integer ramHardLimitInMb = DEFAULT_LIMITS;

        private Integer cpuCountSoftLimit = DEFAULT_LIMITS;

        private Integer cpuCountHardLimit = DEFAULT_LIMITS;

        private Long hdSoftLimitInMb = Long.valueOf(DEFAULT_LIMITS);

        private Long hdHardLimitInMb = Long.valueOf(DEFAULT_LIMITS);

        private Long storageSoft = Long.valueOf(DEFAULT_LIMITS);

        private Long storageHard = Long.valueOf(DEFAULT_LIMITS);

        private Long vlansSoft = Long.valueOf(DEFAULT_LIMITS);

        private Long vlansHard = Long.valueOf(DEFAULT_LIMITS);

        private Long publicIpsSoft = Long.valueOf(DEFAULT_LIMITS);

        private Long publicIpsHard = Long.valueOf(DEFAULT_LIMITS);

        private Long repositorySoft = Long.valueOf(DEFAULT_LIMITS);

        private Long repositoryHard = Long.valueOf(DEFAULT_LIMITS);

        private Boolean isReservationRestricted = DEFAULT_RESERVATION_RESTRICTED;

        public Builder(final AbiquoContext context)
        {
            super();
            this.context = context;
        }

        public Builder ramLimits(final int soft, final int hard)
        {
            this.ramSoftLimitInMb = soft;
            this.ramHardLimitInMb = hard;
            return this;
        }

        public Builder cpuCountLimits(final int soft, final int hard)
        {
            this.cpuCountSoftLimit = soft;
            this.cpuCountHardLimit = hard;
            return this;
        }

        public Builder hdLimitsInMb(final long soft, final long hard)
        {
            this.hdSoftLimitInMb = soft;
            this.hdHardLimitInMb = hard;
            return this;
        }

        public Builder storageLimits(final long soft, final long hard)
        {
            this.storageSoft = soft;
            this.storageHard = hard;
            return this;
        }

        public Builder vlansLimits(final long soft, final long hard)
        {
            this.vlansSoft = soft;
            this.vlansHard = hard;
            return this;
        }

        public Builder publicIpsLimits(final long soft, final long hard)
        {
            this.publicIpsSoft = soft;
            this.publicIpsHard = hard;
            return this;
        }

        public Builder repositoryLimits(final long soft, final long hard)
        {
            this.repositorySoft = soft;
            this.repositoryHard = hard;
            return this;
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

            Enterprise enterprise = new Enterprise(context, dto);

            return enterprise;
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
                .isReservationRestricted(in.getIsReservationRestricted());
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public int getCpuCountHardLimit()
    {
        return target.getCpuCountHardLimit();
    }

    public int getCpuCountSoftLimit()
    {
        return target.getCpuCountSoftLimit();
    }

    public long getHdHardLimitInMb()
    {
        return target.getHdHardLimitInMb();
    }

    public long getHdSoftLimitInMb()
    {
        return target.getHdSoftLimitInMb();
    }

    public boolean getIsReservationRestricted()
    {
        return target.getIsReservationRestricted();
    }

    public String getName()
    {
        return target.getName();
    }

    public long getPublicIpsHard()
    {
        return target.getPublicIpsHard();
    }

    public long getPublicIpsSoft()
    {
        return target.getPublicIpsSoft();
    }

    public int getRamHardLimitInMb()
    {
        return target.getRamHardLimitInMb();
    }

    public int getRamSoftLimitInMb()
    {
        return target.getRamSoftLimitInMb();
    }

    public long getRepositoryHard()
    {
        return target.getRepositoryHard();
    }

    public long getRepositorySoft()
    {
        return target.getRepositorySoft();
    }

    public long getStorageHard()
    {
        return target.getStorageHard();
    }

    public long getStorageSoft()
    {
        return target.getStorageSoft();
    }

    public long getVlansHard()
    {
        return target.getVlansHard();
    }

    public long getVlansSoft()
    {
        return target.getVlansSoft();
    }

    public void setCpuCountHardLimit(final int cpuCountHardLimit)
    {
        target.setCpuCountHardLimit(cpuCountHardLimit);
    }

    public void setCpuCountLimits(final int softLimit, final int hardLimit)
    {
        target.setCpuCountLimits(softLimit, hardLimit);
    }

    public void setCpuCountSoftLimit(final int cpuCountSoftLimit)
    {
        target.setCpuCountSoftLimit(cpuCountSoftLimit);
    }

    public void setHdHardLimitInMb(final long hdHardLimitInMb)
    {
        target.setHdHardLimitInMb(hdHardLimitInMb);
    }

    public void setHdLimitsInMb(final long softLimit, final long hardLimit)
    {
        target.setHdLimitsInMb(softLimit, hardLimit);
    }

    public void setHdSoftLimitInMb(final long hdSoftLimitInMb)
    {
        target.setHdSoftLimitInMb(hdSoftLimitInMb);
    }

    public void setIsReservationRestricted(final boolean isReservationRestricted)
    {
        target.setIsReservationRestricted(isReservationRestricted);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPublicIPLimits(final long softLimit, final long hardLimit)
    {
        target.setPublicIPLimits(softLimit, hardLimit);
    }

    public void setPublicIpsHard(final long publicIpsHard)
    {
        target.setPublicIpsHard(publicIpsHard);
    }

    public void setPublicIpsSoft(final long publicIpsSoft)
    {
        target.setPublicIpsSoft(publicIpsSoft);
    }

    public void setRamHardLimitInMb(final int ramHardLimitInMb)
    {
        target.setRamHardLimitInMb(ramHardLimitInMb);
    }

    public void setRamLimitsInMb(final int softLimit, final int hardLimit)
    {
        target.setRamLimitsInMb(softLimit, hardLimit);
    }

    public void setRamSoftLimitInMb(final int ramSoftLimitInMb)
    {
        target.setRamSoftLimitInMb(ramSoftLimitInMb);
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

    public void setStorageHard(final long storageHard)
    {
        target.setStorageHard(storageHard);
    }

    public void setStorageLimits(final long softLimit, final long hardLimit)
    {
        target.setStorageLimits(softLimit, hardLimit);
    }

    public void setStorageSoft(final long storageSoft)
    {
        target.setStorageSoft(storageSoft);
    }

    public void setVlansHard(final long vlansHard)
    {
        target.setVlansHard(vlansHard);
    }

    public void setVlansLimits(final long softLimit, final long hardLimit)
    {
        target.setVlansLimits(softLimit, hardLimit);
    }

    public void setVlansSoft(final long vlansSoft)
    {
        target.setVlansSoft(vlansSoft);
    }
}
