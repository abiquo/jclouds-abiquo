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

import java.util.Arrays;
import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.AcceptedRequestDto;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.abiquo.server.core.cloud.VirtualMachineState;
import com.abiquo.server.core.cloud.VirtualMachineStateDto;
import com.abiquo.server.core.cloud.VirtualMachineTaskDto;
import com.abiquo.server.core.cloud.chef.RunlistElementsDto;
import com.abiquo.server.core.infrastructure.storage.VolumeManagementDto;
import com.abiquo.server.core.infrastructure.storage.VolumesManagementDto;
import com.abiquo.server.core.task.TaskDto;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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

    VirtualMachineTemplate template;

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
        checkNotNull(template, ValidationErrors.NULL_RESOURCE + VirtualMachineTemplate.class);
        checkNotNull(template.getId(), ValidationErrors.MISSING_REQUIRED_FIELD + " id in "
            + VirtualMachineTemplate.class);

        this.updateLink(target, ParentLinkName.VIRTUAL_MACHINE_TEMPLATE, template.unwrap(), "edit");

        target =
            context.getApi().getCloudClient().createVirtualMachine(virtualAppliance.unwrap(),
                target);
    }

    public AcceptedRequestDto<String> update()
    {
        return context.getApi().getCloudClient().updateVirtualMachine(target);
    }

    public void changeState(final VirtualMachineState state)
    {
        VirtualMachineStateDto stateDto = new VirtualMachineStateDto();
        stateDto.setPower(state);

        AcceptedRequestDto<VirtualMachineStateDto> result =
            context.getApi().getCloudClient().changeVirtualMachineState(target, stateDto);
        VirtualMachineState newState = result.getEntity().getPower();
        target.setState(newState);
    }

    public VirtualMachineState getState()
    {
        VirtualMachineStateDto stateDto =
            context.getApi().getCloudClient().getVirtualMachineState(target);
        return stateDto.getPower();
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

    public List<Volume> listAttachedVolumes()
    {
        VolumesManagementDto volumes =
            context.getApi().getCloudClient().listAttachedVolumes(target);
        return wrap(context, Volume.class, volumes.getCollection());
    }

    public List<Volume> listAttachedVolumes(final Predicate<Volume> filter)
    {
        return Lists.newLinkedList(filter(listAttachedVolumes(), filter));
    }

    public Volume findAttachedVolume(final Predicate<Volume> filter)
    {
        return Iterables.getFirst(filter(listAttachedVolumes(), filter), null);
    }

    public Volume getAttachedVolume(final Predicate<Volume> filter)
    {
        return Iterables.getFirst(filter(listAttachedVolumes(), filter), null);
    }

    // Actions

    public AsyncTask deploy()
    {
        return deploy(false);
    }

    public AsyncTask deploy(final boolean forceEnterpriseSoftLimits)
    {
        // call deploy
        RESTLink deployLink = target.searchLink("deploy");
        VirtualMachineTaskDto deploy = new VirtualMachineTaskDto();
        deploy.setForceEnterpriseSoftLimits(false);

        // get async task
        AcceptedRequestDto<String> response =
            context.getApi().getCloudClient().deployVirtualMachine(deployLink, deploy);

        return this.getTask(response);
    }

    public AsyncTask undeploy()
    {
        RESTLink undeployLink = target.searchLink("undeploy");
        AcceptedRequestDto<String> response =
            context.getApi().getCloudClient().undeployVirtualMachine(undeployLink);

        return this.getTask(response);
    }

    public AcceptedRequestDto< ? > attachVolumes(final Volume... volumes)
    {
        List<Volume> expected = listAttachedVolumes();
        expected.addAll(Arrays.asList(volumes));

        Volume[] vols = new Volume[expected.size()];
        return replaceVolumes(expected.toArray(vols));
    }

    public AcceptedRequestDto< ? > dettachAllVolumes()
    {
        return context.getApi().getCloudClient().detachAllVolumes(target);
    }

    public AcceptedRequestDto< ? > detachVolumes(final Volume... volumes)
    {
        List<Volume> expected = listAttachedVolumes();
        Iterables.removeIf(expected, idIn(volumes));

        Volume[] vols = new Volume[expected.size()];
        return replaceVolumes(expected.toArray(vols));
    }

    public AcceptedRequestDto< ? > replaceVolumes(final Volume... volumes)
    {
        return context.getApi().getCloudClient().replaceVolumes(target, toVolumeDto(volumes));
    }

    private AsyncTask getTask(final AcceptedRequestDto<String> acceptedRequest)
    {
        RESTLink taskLink = acceptedRequest.getLinks().get(0);
        checkNotNull(taskLink, ValidationErrors.MISSING_REQUIRED_LINK + AsyncTask.class);

        TaskDto task = context.getApi().getTaskClient().getTask(taskLink);

        return wrap(context, AsyncTask.class, task);
    }

    // Builder

    public static Builder builder(final AbiquoContext context,
        final VirtualAppliance virtualAppliance, final VirtualMachineTemplate template)
    {
        return new Builder(context, virtualAppliance, template);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private VirtualAppliance virtualAppliance;

        private VirtualMachineTemplate template;

        private String name;

        private String description;

        private Integer ram;

        private Integer cpu;

        private Integer vdrpPort;

        private String vdrpIP;

        private Integer idState;

        private Integer highDisponibility;

        private Integer idType;

        private String password;

        private String uuid;

        public Builder(final AbiquoContext context, final VirtualAppliance virtualAppliance,
            final VirtualMachineTemplate template)
        {
            super();
            checkNotNull(virtualAppliance, ValidationErrors.NULL_RESOURCE + VirtualAppliance.class);
            this.virtualAppliance = virtualAppliance;
            this.template = template;
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
            dto.setHdInBytes(template.getHdRequired());
            dto.setVdrpIP(vdrpIP);

            if (vdrpPort != null)
            {
                dto.setVdrpPort(vdrpPort);
            }

            if (idState != null)
            {
                dto.setIdState(idState);
            }

            if (highDisponibility != null)
            {
                dto.setHighDisponibility(highDisponibility);
            }

            if (idType != null)
            {
                dto.setIdType(idType);
            }

            dto.setPassword(password);
            dto.setUuid(uuid);

            VirtualMachine virtualMachine = new VirtualMachine(context, dto);
            virtualMachine.virtualAppliance = virtualAppliance;
            virtualMachine.template = template;

            return virtualMachine;
        }

        public static Builder fromVirtualMachine(final VirtualMachine in)
        {
            return VirtualMachine.builder(in.context, in.virtualAppliance, in.template).name(
                in.getName()).description(in.getDescription()).ram(in.getRam()).cpu(in.getCpu())
                .vdrpIP(in.getVdrpIP()).vdrpPort(in.getVdrpPort()).idState(in.getIdState())
                .highDisponibility(in.getHighDisponibility()).idType(in.getIdType()).password(
                    in.getPassword());
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

    // Read-only field. This value is computed from the size of the Template
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

    private static VolumeManagementDto[] toVolumeDto(final Volume... volumes)
    {
        checkNotNull(volumes, "must provide at least one volume");

        VolumeManagementDto[] dtos = new VolumeManagementDto[volumes.length];
        for (int i = 0; i < volumes.length; i++)
        {
            dtos[i] = volumes[i].unwrap();
        }

        return dtos;
    }

    private static Predicate<Volume> idIn(final Volume... volumes)
    {
        return new Predicate<Volume>()
        {
            List<Integer> ids = ids(Arrays.asList(volumes));

            @Override
            public boolean apply(final Volume input)
            {
                return ids.contains(input.getId());
            }
        };
    }

    private static List<Integer> ids(final List<Volume> volumes)
    {
        return Lists.transform(volumes, new Function<Volume, Integer>()
        {
            @Override
            public Integer apply(final Volume input)
            {
                return input.getId();
            }
        });
    }

    @Override
    public String toString()
    {
        return "VirtualMachine [id=" + getId() + ", state=" + getState() + ", cpu=" + getCpu()
            + ", description=" + getDescription() + ", hdInBytes=" + getHdInBytes() + ", ha="
            + getHighDisponibility() + ", idType=" + getIdType() + ", name=" + getName()
            + ", password=" + getPassword() + ", ram=" + getRam() + ", uuid=" + getUuid()
            + ", vrdpIp=" + getVdrpIP() + ", vdrpPort=" + getVdrpPort() + "]";
    }

}
