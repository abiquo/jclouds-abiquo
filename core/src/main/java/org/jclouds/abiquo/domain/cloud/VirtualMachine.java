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

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.AcceptedRequestDto;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.abiquo.server.core.cloud.VirtualMachineState;
import com.abiquo.server.core.cloud.VirtualMachineStateDto;
import com.abiquo.server.core.cloud.chef.RunlistElementsDto;

/**
 * Adds high level functionality to {@link VirtualMachineDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Virtual+Machine+Resource">
 *      http://community.abiquo.com/display/ABI20/Virtual+Machine+Resource</a>
 */
public class VirtualMachine extends DomainWrapper<VirtualMachineDto>
{
    /** The virtual appliance where the virtual machine belongs. */
    // Package protected to allow navigation from children
    VirtualAppliance virtualAppliance;

    /**
     * Constructor to be used only by the builder.
     */
    protected VirtualMachine(final AbiquoContext context, final VirtualMachineDto target)
    {
        super(context, target);
    }

    // Domain operations

    public void delete()
    {
        context.getApi().getCloudClient().deleteVirtualMachine(target);
        target = null;
    }

    public void save()
    {
        target =
            context.getApi().getCloudClient().createVirtualMachine(virtualAppliance.unwrap(),
                target);
    }

    public void update()
    {
        target = context.getApi().getCloudClient().updateVirtualMachine(target);
    }

    public AcceptedRequestDto changeState(final VirtualMachineState state)
    {
        VirtualMachineStateDto stateDto = new VirtualMachineStateDto();
        stateDto.setPower(state.toString());

        return context.getApi().getCloudClient().changeVirtualMachineState(target, stateDto);
    }

    // Parent access
    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Virtual+Appliance+Resource#VirtualApplianceResource-RetrieveaVirtualAppliance">
     *      http://community.abiquo.com/display/ABI20/Virtual+Appliance+Resource#VirtualApplianceResource-RetrieveaVirtualAppliance</a>
     */
    public VirtualAppliance getVirtualAppliance()
    {
        RESTLink link = target.searchLink(ParentLinkName.VIRTUAL_APPLIANCE);
        VirtualApplianceDto dto = context.getApi().getCloudClient().getVirtualAppliance(link);
        virtualAppliance = wrap(context, VirtualAppliance.class, dto);
        return virtualAppliance;
    }

    // Children access

    // Actions

    // Builder

    public static Builder builder(final AbiquoContext context,
        final VirtualAppliance virtualAppliance)
    {
        return new Builder(context, virtualAppliance);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private VirtualAppliance virtualAppliance;

        private String name;

        private String description;

        private Integer ram;

        private Integer cpu;

        private Long hdInBytes;

        private Integer vdrpPort;

        private String vdrpIP;

        private Integer idState;

        private VirtualMachineState state;

        private Integer highDisponibility;

        private Integer idType;

        private String password;

        private String uuid;

        public Builder(final AbiquoContext context, final VirtualAppliance virtualAppliance)
        {
            super();
            checkNotNull(virtualAppliance, ValidationErrors.NULL_RESOURCE + VirtualAppliance.class);
            this.virtualAppliance = virtualAppliance;
            this.context = context;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder description(final String description)
        {
            this.description = description;
            return this;
        }

        public Builder ram(final int ram)
        {
            this.ram = ram;
            return this;
        }

        public Builder cpu(final int cpu)
        {
            this.cpu = cpu;
            return this;
        }

        public Builder hdInBytes(final long hdInBytes)
        {
            this.hdInBytes = hdInBytes;
            return this;
        }

        public Builder vdrpPort(final int vdrpPort)
        {
            this.vdrpPort = vdrpPort;
            return this;
        }

        public Builder vdrpIP(final String vdrpIP)
        {
            this.vdrpIP = vdrpIP;
            return this;
        }

        public Builder idState(final int idState)
        {
            this.idState = idState;
            return this;
        }

        public Builder state(final VirtualMachineState state)
        {
            this.state = state;
            return this;
        }

        public Builder highDisponibility(final int highDisponibility)
        {
            this.highDisponibility = highDisponibility;
            return this;
        }

        public Builder idType(final int idType)
        {
            this.idType = idType;
            return this;
        }

        public Builder password(final String password)
        {
            this.password = password;
            return this;
        }

        public Builder uuid(final String uuid)
        {
            this.uuid = uuid;
            return this;
        }

        public Builder virtualAppliance(final VirtualAppliance virtualAppliance)
        {
            checkNotNull(virtualAppliance, ValidationErrors.NULL_RESOURCE + VirtualAppliance.class);
            this.virtualAppliance = virtualAppliance;
            return this;
        }

        public VirtualMachine build()
        {
            VirtualMachineDto dto = new VirtualMachineDto();
            dto.setName(name);
            dto.setDescription(description);
            dto.setRam(ram);
            dto.setCpu(cpu);
            dto.setHdInBytes(hdInBytes);
            dto.setVdrpIP(vdrpIP);
            dto.setVdrpPort(vdrpPort);
            dto.setIdState(idState);
            dto.setState(state);
            dto.setHighDisponibility(highDisponibility);
            dto.setIdType(idType);
            dto.setPassword(password);
            dto.setUuid(uuid);

            VirtualMachine virtualMachine = new VirtualMachine(context, dto);
            virtualMachine.virtualAppliance = virtualAppliance;

            return virtualMachine;
        }

        public static Builder fromVirtualMachine(final VirtualMachine in)
        {
            return VirtualMachine.builder(in.context, in.virtualAppliance).name(in.getName())
                .description(in.getDescription()).ram(in.getRam()).cpu(in.getCpu()).hdInBytes(
                    in.getHdInBytes()).vdrpIP(in.getVdrpIP()).vdrpPort(in.getVdrpPort()).idState(
                    in.getIdState()).highDisponibility(in.getHighDisponibility()).idType(
                    in.getIdType()).password(in.getPassword()).uuid(in.getUuid());
        }
    }

    // Delegate methods

    public int getCpu()
    {
        return target.getCpu();
    }

    public String getDescription()
    {
        return target.getDescription();
    }

    public long getHdInBytes()
    {
        return target.getHdInBytes();
    }

    public int getHighDisponibility()
    {
        return target.getHighDisponibility();
    }

    public Integer getId()
    {
        return target.getId();
    }

    public int getIdState()
    {
        return target.getIdState();
    }

    public int getIdType()
    {
        return target.getIdType();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getPassword()
    {
        return target.getPassword();
    }

    public int getRam()
    {
        return target.getRam();
    }

    public RunlistElementsDto getRunlist()
    {
        return target.getRunlist();
    }

    public VirtualMachineState getState()
    {
        return target.getState();
    }

    public String getUuid()
    {
        return target.getUuid();
    }

    public String getVdrpIP()
    {
        return target.getVdrpIP();
    }

    public int getVdrpPort()
    {
        return target.getVdrpPort();
    }

    public void setCpu(final int cpu)
    {
        target.setCpu(cpu);
    }

    public void setDescription(final String description)
    {
        target.setDescription(description);
    }

    public void setHdInBytes(final long hdInBytes)
    {
        target.setHdInBytes(hdInBytes);
    }

    public void setHighDisponibility(final int highDisponibility)
    {
        target.setHighDisponibility(highDisponibility);
    }

    public void setIdState(final int idState)
    {
        target.setIdState(idState);
    }

    public void setIdType(final int idType)
    {
        target.setIdType(idType);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPassword(final String password)
    {
        target.setPassword(password);
    }

    public void setRam(final int ram)
    {
        target.setRam(ram);
    }

    public void setUuid(final String uuid)
    {
        target.setUuid(uuid);
    }

    public void setVdrpIP(final String vdrpIP)
    {
        target.setVdrpIP(vdrpIP);
    }

    public void setVdrpPort(final int vdrpPort)
    {
        target.setVdrpPort(vdrpPort);
    }
}
