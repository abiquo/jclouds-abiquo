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

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.AbiquoKeywords.ApiParentLinkName;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.RackDto;

/**
 * Adds high level functionality to {@link RackDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI18/Rack+Resource
 */
public class Rack extends DomainWrapper<RackDto>
{
    /** The datacenter where the rack belongs. */
    private Datacenter datacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected Rack(final AbiquoContext context, final RackDto target)
    {
        super(context, target);
    }

    @Override
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteRack(target);
        target = null;
    }

    @Override
    public void save()
    {
        target = context.getApi().getInfrastructureClient().createRack(datacenter.unwrap(), target);
    }

    @Override
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateRack(target);
    }

    // Parent access

    public Datacenter getDatacenter()
    {
        if (datacenter == null)
        {
            Integer datacenterId = target.getIdFromLink(ApiParentLinkName.DATACENTER);
            DatacenterDto dto =
                context.getApi().getInfrastructureClient().getDatacenter(datacenterId);
            datacenter = wrap(context, Datacenter.class, dto);
        }

        return datacenter;
    }

    public static Builder builder(final AbiquoContext context)
    {
        return new Builder(context);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private Integer id;

        private String name;

        private String shortDescription;

        private Boolean haEnabled;

        private Integer nrsq;

        private Integer vlanIdMax;

        private Integer vlanIdMin;

        private Integer vlanPerVdcExpected;

        private String vlansIdAvoided;

        private Datacenter datacenter;

        public Builder(final AbiquoContext context)
        {
            super();
            this.context = context;
        }

        public Builder id(final Integer id)
        {
            this.id = id;
            return this;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder shortDescription(final String shortDescription)
        {
            this.shortDescription = shortDescription;
            return this;
        }

        public Builder haEnabled(final Boolean haEnabled)
        {
            this.haEnabled = haEnabled;
            return this;
        }

        public Builder nrsq(final Integer nrsq)
        {
            this.nrsq = nrsq;
            return this;
        }

        public Builder vlanIdMax(final Integer vlanIdMax)
        {
            this.vlanIdMax = vlanIdMax;
            return this;
        }

        public Builder vlanIdMin(final Integer vlanIdMin)
        {
            this.vlanIdMin = vlanIdMin;
            return this;
        }

        public Builder vlanPerVdcExpected(final Integer vlanPerVdcExpected)
        {
            this.vlanPerVdcExpected = vlanPerVdcExpected;
            return this;
        }

        public Builder VlansIdAvoided(final String vlansIdAvoided)
        {
            this.vlansIdAvoided = vlansIdAvoided;
            return this;
        }

        public Builder datacenter(final Datacenter datacenter)
        {
            this.datacenter = datacenter;
            return this;
        }

        public Rack build()
        {
            RackDto dto = new RackDto();
            dto.setId(id);
            dto.setName(name);
            dto.setShortDescription(shortDescription);
            dto.setHaEnabled(haEnabled);
            dto.setNrsq(nrsq);
            dto.setVlanIdMax(vlanIdMax);
            dto.setVlanIdMin(vlanIdMin);
            dto.setVlanPerVdcExpected(vlanPerVdcExpected);
            dto.setVlansIdAvoided(vlansIdAvoided);
            Rack rack = new Rack(context, dto);
            rack.datacenter = datacenter;
            return rack;

            // TODO: Validate mandatory fields in build methods?
        }

        public static Builder fromRack(final Rack in)
        {
            return Rack.builder(in.context).id(in.getId()).name(in.getName())
                .shortDescription(in.getShortDescription()).haEnabled(in.isHaEnabled())
                .nrsq(in.getNrsq()).vlanIdMax(in.getVlanIdMax()).vlanIdMin(in.getVlanIdMin())
                .vlanPerVdcExpected(in.getVlanPerVdcExpected())
                .VlansIdAvoided(in.getVlansIdAvoided()).datacenter(in.datacenter);
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getShortDescription()
    {
        return target.getShortDescription();
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setShortDescription(final String description)
    {
        target.setShortDescription(description);
    }

    public void setHaEnabled(final boolean haEnabled)
    {
        target.setHaEnabled(haEnabled);
    }

    public boolean isHaEnabled()
    {
        return target.isHaEnabled();
    }

    public Integer getNrsq()
    {
        return target.getNrsq();
    }

    public Integer getVlanIdMax()
    {
        return target.getVlanIdMax();
    }

    public Integer getVlanIdMin()
    {
        return target.getVlanIdMin();
    }

    public Integer getVlanPerVdcExpected()
    {
        return target.getVlanPerVdcExpected();
    }

    public String getVlansIdAvoided()
    {
        return target.getVlansIdAvoided();
    }

    public void setNrsq(final Integer nrsq)
    {
        target.setNrsq(nrsq);
    }

    public void setVlanIdMax(final Integer vlanIdMax)
    {
        target.setVlanIdMax(vlanIdMax);
    }

    public void setVlanIdMin(final Integer vlanIdMin)
    {
        target.setVlanIdMin(vlanIdMin);
    }

    public void setVlanPerVdcExpected(final Integer vlanPerVdcExpected)
    {
        target.setVlanPerVdcExpected(vlanPerVdcExpected);
    }

    public void setVlansIdAvoided(final String vlansIdAvoided)
    {
        target.setVlansIdAvoided(vlansIdAvoided);
    }
}
