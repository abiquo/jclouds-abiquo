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

import java.util.Date;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;

import com.abiquo.server.core.appslibrary.VirtualMachineTemplateDto;

/**
 * Adds high level functionality to {@link VirtualMachineTemplateDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class VirtualMachineTemplate extends DomainWrapper<VirtualMachineTemplateDto>
{
    /**
     * Constructor to be used only by the builder.
     */
    protected VirtualMachineTemplate(final AbiquoContext context,
        final VirtualMachineTemplateDto target)
    {
        super(context, target);
    }

    // Domain operations

    public void delete()
    {
        context.getApi().getVirtualMachineTemplateClient().deleteVirtualMachineTemplate(target);
        target = null;
    }

    public void update()
    {
        target =
            context.getApi().getVirtualMachineTemplateClient().updateVirtualMachineTemplate(target);
    }

    // Parent access

    // Delegate methods
    public int getCpuRequired()
    {
        return target.getCpuRequired();
    }

    public Date getCreationDate()
    {
        return target.getCreationDate();
    }

    public String getCreationUser()
    {
        return target.getCreationUser();
    }

    public String getDescription()
    {
        return target.getDescription();
    }

    public long getDiskFileSize()
    {
        return target.getDiskFileSize();
    }

    public String getDiskFormatType()
    {
        return target.getDiskFormatType();
    }

    public long getHdRequired()
    {
        return target.getHdRequired();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getPath()
    {
        return target.getPath();
    }

    public int getRamRequired()
    {
        return target.getRamRequired();
    }

    public boolean isChefEnabled()
    {
        return target.isChefEnabled();
    }

    public void setChefEnabled(final boolean chefEnabled)
    {
        target.setChefEnabled(chefEnabled);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public Integer getId()
    {
        return target.getId();
    }
}
