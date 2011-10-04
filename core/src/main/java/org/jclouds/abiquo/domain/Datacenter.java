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

package org.jclouds.abiquo.domain;

import org.jclouds.abiquo.AbiquoContext;
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
}
