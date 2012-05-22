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

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.domain.config.Category;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.reference.rest.ParentLinkName;
import org.jclouds.rest.RestContext;

import com.abiquo.model.enumerator.DiskFormatType;
import com.abiquo.server.core.appslibrary.CategoryDto;
import com.abiquo.server.core.appslibrary.VirtualMachineTemplateDto;

/**
 * Adds high level functionality to {@link VirtualMachineTemplateDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see API: <a href="http://community.abiquo.com/display/ABI20/Virtual+Machine+Template+Resource">
 *      http://community.abiquo.com/display/ABI20/Virtual+Machine+Template+Resource</a>
 */
public class VirtualMachineTemplate extends DomainWrapper<VirtualMachineTemplateDto>// DomainWithTasksWrapper<VirtualMachineTemplateDto>
{
    /**
     * Constructor to be used only by the builder.
     */
    protected VirtualMachineTemplate(final RestContext<AbiquoClient, AbiquoAsyncClient> context,
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

    // Children access

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Category+Resource#CategoryResource-Retrieveacategory"
     *      > http://community.abiquo.com/display/ABI20/Category+Resource#CategoryResource-
     *      Retrieveacategory</a>
     */
    public Category getCategory()
    {
        Integer categoryId = target.getIdFromLink(ParentLinkName.CATEGORY);
        CategoryDto category = context.getApi().getConfigClient().getCategory(categoryId);
        return wrap(context, Category.class, category);
    }

    // Parent access

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-RetrieveanEnterprise"
     *      > http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-
     *      RetrieveanEnterprise</a>
     */
    public Enterprise getEnterprise()
    {
        Integer enterpriseId = target.getIdFromLink(ParentLinkName.ENTERPRISE);
        return wrap(context, Enterprise.class, context.getApi().getEnterpriseClient()
            .getEnterprise(enterpriseId));
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-RetrieveaDatacenter"
     *      > http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-
     *      RetrieveaDatacenter</a>
     */
    public Datacenter getDatacenter()
    {
        Integer repositoryId = target.getIdFromLink(ParentLinkName.DATACENTER_REPOSITORY);
        return wrap(context, Datacenter.class, context.getApi().getInfrastructureClient()
            .getDatacenter(repositoryId));
    }

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

    public DiskFormatType getDiskFormatType()
    {
        return DiskFormatType.valueOf(target.getDiskFormatType());
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

    public String getIconUrl()
    {
        return target.getIconUrl();
    }

    @Override
    public String toString()
    {
        return "VirtualMachineTemplate [id=" + getId() + ", cpuRequired=" + getCpuRequired()
            + ", creationDate=" + getCreationDate() + ", creationUser=" + getCreationUser()
            + ", description=" + getDescription() + ", diskFileSize=" + getDiskFileSize()
            + ", diskFormatType=" + getDiskFormatType() + ", hdRequired=" + getHdRequired()
            + ", name=" + getName() + ", path=" + getPath() + ", ramRequired=" + getRamRequired()
            + ", chefEnabled=" + isChefEnabled() + "]";
    }

}
