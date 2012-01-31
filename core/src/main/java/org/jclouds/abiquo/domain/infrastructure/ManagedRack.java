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
import com.abiquo.server.core.infrastructure.UcsRackDto;
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
public class ManagedRack extends DomainWrapper<UcsRackDto>
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
    protected ManagedRack(final AbiquoContext context, final UcsRackDto target)
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
     *      href="http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-CreateanewUcsRack">
     *      http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-CreateanewUcsRack</a>
     */
    public void save()
    {
        target =
            context.getApi().getInfrastructureClient().createManagedRack(datacenter.unwrap(),
                target);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-UpdateanexistingUcsRack">
     *      http://community.abiquo.com/display/ABI20/Rack+Resource#RackResource-UpdateanexistingUcsRack</a>
     */
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateManagedRack(target);
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

        private Integer port;

        private String ip;

        private String password;

        private String user;

        private String defaultTemplate;

        private Integer maxMachinesOn;

        private Datacenter datacenter;

        public Builder(final AbiquoContext context, final Datacenter datacenter)
        {
            super();
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Datacenter.class);
            this.datacenter = datacenter;
            this.context = context;
        }

        public Builder port(final Integer port)
        {
            this.port = port;
            return this;
        }

        public Builder ipAddress(final String ip)
        {
            this.ip = ip;
            return this;
        }

        public Builder password(final String password)
        {
            this.password = password;
            return this;
        }

        public Builder user(final String user)
        {
            this.user = user;
            return this;
        }

        public Builder defaultTemplate(final String defaultTemplate)
        {
            this.defaultTemplate = defaultTemplate;
            return this;
        }

        public Builder maxMachinesOn(final Integer maxMachinesOn)
        {
            this.maxMachinesOn = maxMachinesOn;
            return this;
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

        public ManagedRack build()
        {
            UcsRackDto dto = new UcsRackDto();
            dto.setId(id);
            dto.setName(name);
            dto.setShortDescription(shortDescription);
            dto.setHaEnabled(haEnabled);
            dto.setNrsq(nrsq);
            dto.setVlanIdMax(vlanIdMax);
            dto.setVlanIdMin(vlanIdMin);
            dto.setVlanPerVdcReserved(vlanPerVdcReserved);
            dto.setVlansIdAvoided(vlansIdAvoided);
            dto.setPort(port);
            dto.setIp(ip);
            dto.setPassword(password);
            dto.setUser(user);
            dto.setDefaultTemplate(defaultTemplate);
            dto.setMaxMachinesOn(maxMachinesOn);

            ManagedRack rack = new ManagedRack(context, dto);
            rack.datacenter = datacenter;
            return rack;
        }

        public static Builder fromRack(final ManagedRack in)
        {
            return ManagedRack.builder(in.context, in.datacenter).id(in.getId()).name(in.getName())
                .shortDescription(in.getShortDescription()).haEnabled(in.isHaEnabled()).nrsq(
                    in.getNrsq()).vlanIdMax(in.getVlanIdMax()).vlanIdMin(in.getVlanIdMin())
                .vlanPerVdcReserved(in.getVlanPerVdcReserved()).VlansIdAvoided(
                    in.getVlansIdAvoided()).port(in.getPort()).ipAddress(in.getIp()).password(
                    in.getPassword()).user(in.getUser()).defaultTemplate(in.getDefaultTemplate())
                .maxMachinesOn(in.getMaxMachinesOn());
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

    public String getIp()
    {
        return target.getIp();
    }

    public String getLongDescription()
    {
        return target.getLongDescription();
    }

    public Integer getMaxMachinesOn()
    {
        return target.getMaxMachinesOn();
    }

    public String getPassword()
    {
        return target.getPassword();
    }

    public Integer getPort()
    {
        return target.getPort();
    }

    public String getUser()
    {
        return target.getUser();
    }

    public void setDefaultTemplate(final String defaultTemplate)
    {
        target.setDefaultTemplate(defaultTemplate);
    }

    public String getDefaultTemplate()
    {
        return target.getDefaultTemplate();
    }

    public void setIp(final String ip)
    {
        target.setIp(ip);
    }

    public void setMaxMachinesOn(final Integer maxMachinesOn)
    {
        target.setMaxMachinesOn(maxMachinesOn);
    }

    public void setPassword(final String password)
    {
        target.setPassword(password);
    }

    public void setPort(final Integer port)
    {
        target.setPort(port);
    }

    public void setUser(final String user)
    {
        target.setUser(user);
    }

}
