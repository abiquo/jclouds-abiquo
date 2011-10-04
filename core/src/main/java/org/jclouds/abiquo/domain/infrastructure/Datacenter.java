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

import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Adds high level functionality to {@link DatacenterDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class Datacenter extends DomainWrapper<DatacenterDto>
{
    /**
     * Constructor to be used only by the builder.
     */
    protected Datacenter(AbiquoContext context, DatacenterDto target)
    {
        super(context, target);
    }

    @Override
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteDatacenter(getId());
    }

    @Override
    public void save()
    {
        // Create datacenter
        target = context.getApi().getInfrastructureClient().createDatacenter(target);
    }

    @Override
    public void update()
    {
        // Update datacenter
        target = context.getApi().getInfrastructureClient().updateDatacenter(getId(), target);
    }

    public static Builder builder(final AbiquoContext context)
    {
        return new Builder(context);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private Integer id;

        private String name;

        private String location;

        public Builder(final AbiquoContext context)
        {
            super();
            this.context = context;
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

        public Builder location(final String location)
        {
            this.location = location;
            return this;
        }

        public Datacenter build()
        {
            DatacenterDto dto = new DatacenterDto();
            dto.setId(id);
            dto.setName(name);
            dto.setLocation(location);
            return new Datacenter(context, dto);
        }

        public static Builder fromDatacenter(final Datacenter in)
        {
            return Datacenter.builder(in.context).id(in.getId()).name(in.getName())
                .location(in.getLocation());
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getLocation()
    {
        return target.getLocation();
    }

    public String getName()
    {
        return target.getName();
    }

    public void setLocation(String location)
    {
        target.setLocation(location);
    }

    public void setName(String name)
    {
        target.setName(name);
    }

}
