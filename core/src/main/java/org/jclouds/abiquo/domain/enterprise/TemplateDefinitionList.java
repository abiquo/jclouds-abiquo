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

package org.jclouds.abiquo.domain.enterprise;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;

import com.abiquo.server.core.appslibrary.TemplateDefinitionListDto;

/**
 * Adds high level functionality to {@link TemplateDefinitionListDto}. A Template Definition List
 * provides a way to organize multiple Template Definitions. A single Template Definition can be
 * shared by many lists. Its compatible with ovfindex.xml format.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class TemplateDefinitionList extends DomainWrapper<TemplateDefinitionListDto>
{
    /** The enterprise where the list belongs. */
    private Enterprise enterprise;

    /**
     * Constructor to be used only by the builder.
     */
    protected TemplateDefinitionList(final AbiquoContext context,
        final TemplateDefinitionListDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * Delete the template definition list.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/TemplateDefinitionListResource#TemplateDefinitionListResource-Deleteatemplatedefinitionlist"
     *      > http://community.abiquo.com/display/ABI20/TemplateDefinitionListResource#
     *      TemplateDefinitionListResource-Deleteatemplatedefinitionlist</a>
     */
    public void delete()
    {
        context.getApi().getEnterpriseClient().deleteTemplateDefinitionList(target);
        target = null;
    }

    /**
     * Create a template definition list. All the contained Template Definitions will also be
     * created.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/TemplateDefinitionListResource#TemplateDefinitionListResource-Createatemplatedefinitionlist"
     *      > http://community.abiquo.com/display/ABI20/TemplateDefinitionListResource#
     *      TemplateDefinitionListResource-Createatemplatedefinitionlistr</a>
     */
    public void save()
    {
        target =
            context.getApi().getEnterpriseClient()
                .createTemplateDefinitionList(enterprise.unwrap(), target);
    }

    /**
     * Update a template definition list with the data from this template definition list.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/TemplateDefinitionListResource#TemplateDefinitionListResource-Modifyatemplatedefinitionlist"
     *      > http://community.abiquo.com/display/ABI20/TemplateDefinitionListResource#
     *      TemplateDefinitionListResource-Modifyatemplatedefinitionlist</a>
     */
    public void update()
    {
        target = context.getApi().getEnterpriseClient().updateTemplateDefinitionList(target);
    }

    // Builder

    public static Builder builder(final AbiquoContext context, final Enterprise enterprise)
    {
        return new Builder(context, enterprise);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private Enterprise enterprise;

        private String name;

        private String url;

        public Builder(final AbiquoContext context, final Enterprise enterprise)
        {
            super();
            this.context = context;
            this.enterprise = enterprise;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder url(final String url)
        {
            this.url = url;
            return this;
        }

        public TemplateDefinitionList build()
        {
            TemplateDefinitionListDto dto = new TemplateDefinitionListDto();
            dto.setName(name);
            dto.setUrl(url);

            TemplateDefinitionList templateList = new TemplateDefinitionList(context, dto);
            templateList.enterprise = enterprise;
            return templateList;

        }

        public static Builder fromTemplateDefinitionList(final TemplateDefinitionList in)
        {
            return TemplateDefinitionList.builder(in.context, in.enterprise).name(in.getName())
                .url(in.getUrl());
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

    public String getUrl()
    {
        return target.getUrl();
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setUrl(final String url)
    {
        target.setUrl(url);
    }

    @Override
    public String toString()
    {
        return "TemplateDefinitionList [getId()=" + getId() + ", getName()=" + getName()
            + ", getUrl()=" + getUrl() + "]";
    }
}
