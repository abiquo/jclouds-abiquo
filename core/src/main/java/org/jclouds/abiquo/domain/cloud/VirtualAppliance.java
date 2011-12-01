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

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.model.enumerator.VirtualMachineState;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;

/**
 * Adds high level functionality to {@link VirtualApplianceDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Virtual+Appliance+Resource">
 *      http://community.abiquo.com/display/ABI20/Virtual+Appliance+Resource</a>
 */
public class VirtualAppliance extends DomainWrapper<VirtualApplianceDto>
{
    /** The datacenter where the rack belongs. */
    // Package protected to allow navigation from children
    VirtualDatacenter virtualDatacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected VirtualAppliance(final AbiquoContext context, final VirtualApplianceDto target)
    {
        super(context, target);
    }

    // Domain operations

    public void delete()
    {
        context.getApi().getCloudClient().deleteVirtualAppliance(target);
        target = null;
    }

    public void save()
    {
        target =
            context.getApi().getCloudClient().createVirtualAppliance(virtualDatacenter.unwrap(),
                target);
    }

    public void update()
    {
        target = context.getApi().getCloudClient().updateVirtualAppliance(target);
    }

    // Parent access
    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-RetrieveaVirtualDatacenter">
     *      http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-RetrieveaVirtualDatacenter</a>
     */
    public VirtualDatacenter getVirtualDatacenter()
    {
        Integer virtualDatacenterId = target.getIdFromLink(ParentLinkName.VIRTUAL_DATACENTER);
        VirtualDatacenterDto dto =
            context.getApi().getCloudClient().getVirtualDatacenter(virtualDatacenterId);
        virtualDatacenter = wrap(context, VirtualDatacenter.class, dto);
        return virtualDatacenter;
    }

    // Children access

    // Builder

    // Delegate methods

    public int getError()
    {
        return target.getError();
    }

    public int getHighDisponibility()
    {
        return target.getHighDisponibility();
    }

    public Integer getId()
    {
        return target.getId();
    }

    public String getName()
    {
        return target.getName();
    }

    public int getPublicApp()
    {
        return target.getPublicApp();
    }

    public VirtualMachineState getState()
    {
        return target.getState();
    }

    public VirtualMachineState getSubState()
    {
        return target.getSubState();
    }

    public void setHighDisponibility(final int highDisponibility)
    {
        target.setHighDisponibility(highDisponibility);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPublicApp(final int publicApp)
    {
        target.setPublicApp(publicApp);
    }

    public void setState(final VirtualMachineState state)
    {
        target.setState(state);
    }

    public void setSubState(final VirtualMachineState subState)
    {
        target.setSubState(subState);
    }
}
