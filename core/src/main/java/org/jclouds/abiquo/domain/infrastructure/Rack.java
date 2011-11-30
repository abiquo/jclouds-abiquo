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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.filter;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.server.core.infrastructure.MachinesDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Adds high level functionality to {@link RackDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Rack+Resource">
 *      http://community.abiquo.com/display/ABI20/Rack+Resource</a>
 */
public class Rack extends DomainWrapper<RackDto>
{
    /** The default minimum VLAN id. */
    private static final int DEFAULT_VLAN_ID_MIN = 2;

    /** The default maximum VLAN id. */
    private static final int DEFAULT_VLAN_ID_MAX = 4094;

    /** The default maximum VLAN per virtual datacenter. */
    private static final int DEFAULT_VLAN_PER_VDC = 1;

    /** The default nrsq factor. */
    private static final int DEFAULT_NRSQ = 10;

    /** The datacenter where the rack belongs. */
    // Package protected to allow navigation from children
    Datacenter datacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected Rack(final AbiquoContext context, final RackDto target)
    {
        super(context, target);
    }

    // Domain operations

    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteRack(target);
        target = null;
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-CreateanewRack">
     *      http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-CreateanewRack</a>
     */
    public void save()
    {
        target = context.getApi().getInfrastructureClient().createRack(datacenter.unwrap(), target);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-UpdateanexistingRack">
     *      http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-UpdateanexistingRack</a>
     */
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateRack(target);
    }

    // Parent access
    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-RetrieveaDatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-RetrieveaDatacenter</a>
     */
    public Datacenter getDatacenter()
    {
        Integer datacenterId = target.getIdFromLink(ParentLinkName.DATACENTER);
        return context.getAdministrationService().getDatacenter(datacenterId);
    }

    // Children access

    public List<Machine> listMachines()
    {
        MachinesDto machines = context.getApi().getInfrastructureClient().listMachines(target);
        return wrap(context, Machine.class, machines.getCollection());
    }

    public List<Machine> listMachines(final Predicate<Machine> filter)
    {
        return Lists.newLinkedList(filter(listMachines(), filter));
    }

    public Machine findMachine(final Predicate<Machine> filter)
    {
        return Iterables.getFirst(filter(listMachines(), filter), null);
    }

    // Builder

    public static Builder builder(final AbiquoContext context, final Datacenter datacenter)
    {
        return new Builder(context, datacenter);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private Integer id;

        private String name;

        private String shortDescription;

        private boolean haEnabled = false;

        private Integer nrsq = DEFAULT_NRSQ;

        private Integer vlanIdMax = DEFAULT_VLAN_ID_MAX;

        private Integer vlanIdMin = DEFAULT_VLAN_ID_MIN;

        private Integer vlanPerVdcReserved = DEFAULT_VLAN_PER_VDC;

        private String vlansIdAvoided;

        private Datacenter datacenter;

        public Builder(final AbiquoContext context, final Datacenter datacenter)
        {
            super();
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Datacenter.class);
            this.datacenter = datacenter;
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

        public Builder haEnabled(final boolean haEnabled)
        {
            this.haEnabled = haEnabled;
            return this;
        }

        public Builder nrsq(final int nrsq)
        {
            this.nrsq = nrsq;
            return this;
        }

        public Builder vlanIdMax(final int vlanIdMax)
        {
            this.vlanIdMax = vlanIdMax;
            return this;
        }

        public Builder vlanIdMin(final int vlanIdMin)
        {
            this.vlanIdMin = vlanIdMin;
            return this;
        }

        public Builder vlanPerVdcReserved(final int vlanPerVdcExpected)
        {
            this.vlanPerVdcReserved = vlanPerVdcExpected;
            return this;
        }

        public Builder VlansIdAvoided(final String vlansIdAvoided)
        {
            this.vlansIdAvoided = vlansIdAvoided;
            return this;
        }

        public Builder datacenter(final Datacenter datacenter)
        {
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Datacenter.class);
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
            dto.setVlanPerVdcReserved(vlanPerVdcReserved);
            dto.setVlansIdAvoided(vlansIdAvoided);
            Rack rack = new Rack(context, dto);
            rack.datacenter = datacenter;
            return rack;
        }

        public static Builder fromRack(final Rack in)
        {
            return Rack.builder(in.context, in.datacenter).id(in.getId()).name(in.getName())
                .shortDescription(in.getShortDescription()).haEnabled(in.isHaEnabled()).nrsq(
                    in.getNrsq()).vlanIdMax(in.getVlanIdMax()).vlanIdMin(in.getVlanIdMin())
                .vlanPerVdcReserved(in.getVlanPerVdcReserved()).VlansIdAvoided(
                    in.getVlansIdAvoided());
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

    public Integer getVlanPerVdcReserved()
    {
        return target.getVlanPerVdcReserved();
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

    public void setVlanPerVdcReserved(final Integer vlanPerVdcReserved)
    {
        target.setVlanPerVdcReserved(vlanPerVdcReserved);
    }

    public void setVlansIdAvoided(final String vlansIdAvoided)
    {
        target.setVlansIdAvoided(vlansIdAvoided);
    }
}
