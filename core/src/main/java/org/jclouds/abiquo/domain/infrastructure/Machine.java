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
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.MachineState;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.RackDto;

/**
 * Adds high level functionality to {@link MachineDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI18/Machine+Resource
 */
public class Machine extends DomainWrapper<MachineDto>
{
    /** The rack where the machine belongs. */
    // Package protected to allow navigation from children
    Rack rack;

    /**
     * Constructor to be used only by the builder.
     */
    protected Machine(final AbiquoContext context, final MachineDto target)
    {
        super(context, target);
    }

    @Override
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteMachine(target);
        target = null;
    }

    @Override
    public void save()
    {
        target = context.getApi().getInfrastructureClient().createMachine(rack.unwrap(), target);
    }

    @Override
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateMachine(target);
    }

    // Parent access

    public Rack getRack()
    {
        Integer rackId = target.getIdFromLink(ParentLinkName.RACK);
        RackDto dto =
            context.getApi().getInfrastructureClient().getRack(rack.datacenter.unwrap(), rackId);
        rack = wrap(context, Rack.class, dto);

        return rack;
    }

    public String getDescription()
    {
        return target.getDescription();
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getIp()
    {
        return target.getIp();
    }

    public String getIpmiIp()
    {
        return target.getIpmiIp();
    }

    public String getIpmiPassword()
    {
        return target.getIpmiPassword();
    }

    public Integer getIpmiPort()
    {
        return target.getIpmiPort();
    }

    public String getIpmiUser()
    {
        return target.getIpmiUser();
    }

    public String getIpService()
    {
        return target.getIpService();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getPassword()
    {
        return target.getPassword();
    }

    public Integer getPort()
    {
        return target.getPort();
    }

    public MachineState getState()
    {
        return target.getState();
    }

    public HypervisorType getType()
    {
        return target.getType();
    }

    public String getUser()
    {
        return target.getUser();
    }

    public Integer getVirtualCpuCores()
    {
        return target.getVirtualCpuCores();
    }

    public Integer getVirtualCpusPerCore()
    {
        return target.getVirtualCpusPerCore();
    }

    public Integer getVirtualCpusUsed()
    {
        return target.getVirtualCpusUsed();
    }

    public Integer getVirtualRamInMb()
    {
        return target.getVirtualRamInMb();
    }

    public Integer getVirtualRamUsedInMb()
    {
        return target.getVirtualRamUsedInMb();
    }

    public String getVirtualSwitch()
    {
        return target.getVirtualSwitch();
    }

    public void setDescription(final String description)
    {
        target.setDescription(description);
    }

    public void setIp(final String ip)
    {
        target.setIp(ip);
    }

    public void setIpmiIp(final String ipmiIp)
    {
        target.setIpmiIp(ipmiIp);
    }

    public void setIpmiPassword(final String ipmiPassword)
    {
        target.setIpmiPassword(ipmiPassword);
    }

    public void setIpmiPort(final Integer ipmiPort)
    {
        target.setIpmiPort(ipmiPort);
    }

    public void setIpmiUser(final String ipmiUser)
    {
        target.setIpmiUser(ipmiUser);
    }

    public void setIpService(final String ipService)
    {
        target.setIpService(ipService);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPassword(final String password)
    {
        target.setPassword(password);
    }

    public void setPort(final Integer port)
    {
        target.setPort(port);
    }

    public void setState(final MachineState state)
    {
        target.setState(state);
    }

    public void setType(final HypervisorType type)
    {
        target.setType(type);
    }

    public void setUser(final String user)
    {
        target.setUser(user);
    }

    public void setVirtualCpuCores(final Integer virtualCpuCores)
    {
        target.setVirtualCpuCores(virtualCpuCores);
    }

    public void setVirtualCpusPerCore(final Integer virtualCpusPerCore)
    {
        target.setVirtualCpusPerCore(virtualCpusPerCore);
    }

    public void setVirtualCpusUsed(final Integer virtualCpusUsed)
    {
        target.setVirtualCpusUsed(virtualCpusUsed);
    }

    public void setVirtualRamInMb(final Integer virtualRamInMb)
    {
        target.setVirtualRamInMb(virtualRamInMb);
    }

    public void setVirtualRamUsedInMb(final Integer virtualRamUsedInMb)
    {
        target.setVirtualRamUsedInMb(virtualRamUsedInMb);
    }

    public void setVirtualSwitch(final String virtualSwitch)
    {
        target.setVirtualSwitch(virtualSwitch);
    }

    // public Datastores getDatastores()
    // {
    // return target.getDatastores();
    // }
    //
    // public void setDatastores(final Datastores datastores)
    // {
    // target.setDatastores(datastores);
    // }
}
