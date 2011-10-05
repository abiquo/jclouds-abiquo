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

import com.abiquo.server.core.infrastructure.RackDto;

/**
 * Adds high level functionality to {@link RackDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class Rack extends DomainWrapper<RackDto>
{
    public RackDto target;

    /**
     * Constructor to be used only by the builder.
     */
    protected Rack(final AbiquoContext context, final RackDto target)
    {
        super(context, target);
    }

    @Override
    public void delete()
    {
        // TODO not implemented context.getApi().getInfrastructureClient().deleteRack(getId());
    }

    @Override
    public void save()
    {
        // Create rack
        // TODO not implemented target =
        // context.getApi().getInfrastructureClient().createRack(target);
    }

    @Override
    public void update()
    {
        // Update rack
        // TODO not implemented target =
        // context.getApi().getInfrastructureClient().updateRack(getId(), target);
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

        private String shortDescription;

        private Boolean haEnabled;

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

        public Builder shortDescription(final String shortDescription)
        {
            this.shortDescription = shortDescription;
            return this;
        }

        public Builder haEnabled(final Boolean haEnabled)
        {
            this.haEnabled = haEnabled;
            return this;
        }

        public Rack build()
        {
            RackDto dto = new RackDto();
            dto.setId(id);
            dto.setName(name);
            dto.setShortDescription(shortDescription);
            dto.setHaEnabled(haEnabled);
            return new Rack(context, dto);
        }

        public static Builder fromRack(final Rack in)
        {
            return Rack.builder(in.context).id(in.getId()).name(in.getName()).shortDescription(
                in.getShortDescription()).haEnabled(in.isHaEnabled());
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

    public String getShortDescription()
    {
        return target.getShortDescription();
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setShortDescription(final String description)
    {
        target.setShortDescription(description);
    }

    public void setHaEnabled(final boolean haEnabled)
    {
        target.setHaEnabled(haEnabled);
    }

    public boolean isHaEnabled()
    {
        return target.isHaEnabled();
    }
}
