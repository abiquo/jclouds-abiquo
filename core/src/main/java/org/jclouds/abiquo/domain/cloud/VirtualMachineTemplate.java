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
import java.util.List;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.DomainWithTasksWrapper;
import org.jclouds.abiquo.domain.cloud.options.ConversionOptions;
import org.jclouds.abiquo.domain.config.Category;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.reference.rest.ParentLinkName;
import org.jclouds.rest.RestContext;

import com.abiquo.model.enumerator.ConversionState;
import com.abiquo.model.enumerator.DiskFormatType;
import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.transport.AcceptedRequestDto;
import com.abiquo.server.core.appslibrary.CategoryDto;
import com.abiquo.server.core.appslibrary.ConversionRequestDto;
import com.abiquo.server.core.appslibrary.ConversionsDto;
import com.abiquo.server.core.appslibrary.VirtualMachineTemplateDto;

/**
 * Adds high level functionality to {@link VirtualMachineTemplateDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see API: <a href="http://community.abiquo.com/display/ABI20/Virtual+Machine+Template+Resource">
 *      http://community.abiquo.com/display/ABI20/Virtual+Machine+Template+Resource</a>
 */
public class VirtualMachineTemplate extends DomainWithTasksWrapper<VirtualMachineTemplateDto>
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

    /**
     * List all the conversions for the virtual machine template.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Conversion+Resource#ConversionResource-ListConversions"
     *      > http://community.abiquo.com/display/ABI20/Conversion+Resource#ConversionResource-
     *      ListConversions</a>
     * @return all the conversions of the virtual machine template
     */
    public List<Conversion> listConversions()
    {
        ConversionsDto convs =
            context.getApi().getVirtualMachineTemplateClient().listConversions(target);
        return wrap(context, Conversion.class, convs.getCollection());
    }

    /**
     * List conversions for a virtual machine template.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Conversion+Resource#ConversionResource-ListConversions"
     *      > http://community.abiquo.com/display/ABI20/Conversion+Resource#ConversionResource-
     *      ListConversions</a>
     * @param hypervisor, Optionally filter conversions compatible with the provided hypervisor
     * @param state, Optionally filter conversions with the desired state
     * @return all the conversions of the virtual machine template applying the constrains
     */
    public List<Conversion> listConversions(final HypervisorType hypervisor,
        final ConversionState state)
    {
        ConversionsDto convs =
            context
                .getApi()
                .getVirtualMachineTemplateClient()
                .listConversions(
                    target,
                    ConversionOptions.builder().hypervisorType(hypervisor).conversionState(state)
                        .build());
        return wrap(context, Conversion.class, convs.getCollection());
    }

    /**
     * Starts a new conversion for a virtual machine template.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Conversion+Resource#ConversionResource-RequestConversion"
     *      > http://community.abiquo.com/display/ABI20/Conversion+Resource#ConversionResource-
     *      RequestConversion</a>
     * @param diskFormat, desired target format for the request template
     * @return The task reference to track its progress
     */
    public AsyncTask requestConversion(final DiskFormatType diskFormat)
    {
        ConversionRequestDto request = new ConversionRequestDto();
        request.setFormat(diskFormat);

        AcceptedRequestDto<String> taskRef =
            context.getApi().getVirtualMachineTemplateClient().requestConversion(target, request);

        return taskRef == null ? null : getTask(taskRef);
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
