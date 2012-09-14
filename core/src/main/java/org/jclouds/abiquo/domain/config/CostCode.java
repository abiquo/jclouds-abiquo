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

package org.jclouds.abiquo.domain.config;

import org.jclouds.abiquo.AbiquoApi;
import org.jclouds.abiquo.AbiquoAsyncApi;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.rest.RestContext;

import com.abiquo.server.core.pricing.CostCodeDto;

/**
 * Adds high level functionality to {@link CostCodeDto}.
 * 
 * @author Ignasi Barrera
 * @author Susana Acedo
 * @see API: <a href="http://community.abiquo.com/display/ABI20/CostCode+Resource">
 *      http://community.abiquo.com/display/ABI20/CostCode+Resource</a>
 */

public class CostCode extends DomainWrapper<CostCodeDto>
{

    /**
     * Constructor to be used only by the builder. This resource cannot be created.
     */
    private CostCode(final RestContext<AbiquoApi, AbiquoAsyncApi> context, final CostCodeDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/CostCode+Resource#CostCodeResource-Deleteacostcode"
     *      > http://community.abiquo.com/display/ABI20/CostCode+Resource#CostCodeResource-
     *      Deleteacostcode</a>
     */
    public void delete()
    {
        context.getApi().getPricingApi().deleteCostCode(target);
        target = null;
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/CostCode+Resource#CostCodeResource-Createacostcode"
     *      > http://community.abiquo.com/display/ABI20/CostCode+Resource#CostCodeResource-
     *      Createacostcode</a>
     */
    public void save()
    {
        target = context.getApi().getPricingApi().createCostCode(target);
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/CostCode+Resource#CostCodeResource-Updateanexistingcostcode"
     *      > http://community.abiquo.com/display/ABI20/CostCode+Resource#CostCodeResource-
     *      Updateanexistingcostcode</a>
     */
    public void update()
    {
        target = context.getApi().getPricingApi().updateCostCode(target);
    }

    // Builder

    public static Builder builder(final RestContext<AbiquoApi, AbiquoAsyncApi> context)
    {
        return new Builder(context);
    }

    public static class Builder
    {
        private RestContext<AbiquoApi, AbiquoAsyncApi> context;

        private String name;

        private String description;

        public Builder(final RestContext<AbiquoApi, AbiquoAsyncApi> context)
        {
            super();
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

        public CostCode build()
        {
            CostCodeDto dto = new CostCodeDto();
            dto.setName(name);
            dto.setDescription(description);
            CostCode costcode = new CostCode(context, dto);

            return costcode;
        }

        public static Builder fromCostCode(final CostCode in)
        {
            Builder builder =
                CostCode.builder(in.context).name(in.getName()).description(in.getDescription());
            return builder;
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

    public void setName(final String name)
    {
        target.setName(name);
    }

    public String getDescription()
    {
        return target.getDescription();
    }

    public void setDescription(final String description)
    {
        target.setDescription(description);
    }

    @Override
    public String toString()
    {
        return "CostCode [id=" + getId() + ", name=" + getName() + ", description="
            + getDescription() + "]";
    }

}
