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
import org.jclouds.abiquo.domain.factory.TransformerFactory;

import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Adds high level functionallity to {@link DatacenterDto}.
 * 
 * @author Ignasi Barrera
 */
public class Datacenter extends DatacenterDto implements DomainWrapper
{
    private static final long serialVersionUID = 1L;

    private AbiquoContext context;

    public Datacenter(final AbiquoContext context)
    {
        this.context = context;
    }

    public Datacenter(final AbiquoContext context, final String name, final String location)
    {
        super();
        this.context = context;
        setName(name);
        setLocation(location);
    }

    @Override
    public void delete()
    {
        context.getInfrastructureService().deleteDatacenter(this.getId());
    }

    @Override
    public void save()
    {
        // Create datacenter
        DatacenterDto dto =
            context.getInfrastructureService().createDatacenter(this.getName(), this.getLocation());

        // Update this class with incoming information
        TransformerFactory.getClientTransformer(DatacenterDto.class, Datacenter.class)
            .updateResource(dto, this);
    }

    @Override
    public void update()
    {
        // Update datacenter
        DatacenterDto dto = context.getInfrastructureService().updateDatacenter(this);

        // Update this class with incoming information
        TransformerFactory.getClientTransformer(DatacenterDto.class, Datacenter.class)
            .updateResource(dto, this);
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
            Datacenter datacenter = new Datacenter(context, name, location);
            datacenter.setId(id);
            return datacenter;
        }

        public static Builder fromDatacenter(final Datacenter in)
        {
            return Datacenter.builder(in.context).id(in.getId()).name(in.getName())
                .location(in.getLocation());
        }
    }
}
