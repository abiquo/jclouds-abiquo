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

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.ClientErrors;
import org.jclouds.abiquo.reference.AbiquoKeywords.ApiParentLinkName;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;

/**
 * Adds high level functionality to {@link RemoteServiceDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI18/Remote+Service+Resource
 */
public class RemoteService extends DomainWrapper<RemoteServiceDto>
{
    /** The datacenter using the remote service. */
    private Datacenter datacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected RemoteService(final AbiquoContext context, final RemoteServiceDto target)
    {
        super(context, target);
    }

    @Override
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteRemoteService(target);
        target = null;
    }

    @Override
    public void save()
    {
        target =
            context.getApi().getInfrastructureClient().createRemoteService(datacenter.unwrap(),
                target);
    }

    @Override
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateRemoteService(target);
    }

    // Parent access

    public Datacenter getDatacenter()
    {
        Integer datacenterId = target.getIdFromLink(ApiParentLinkName.DATACENTER);
        DatacenterDto dto = context.getApi().getInfrastructureClient().getDatacenter(datacenterId);
        datacenter = wrap(context, Datacenter.class, dto);

        return datacenter;
    }

    public static Builder builder(final AbiquoContext context, final Datacenter datacenter)
    {
        return new Builder(context, datacenter);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private Integer id;

        private Datacenter datacenter;

        public Builder(final AbiquoContext context, final Datacenter datacenter)
        {
            super();
            checkNotNull(datacenter, ClientErrors.NULL_PARENT + Datacenter.class);
            this.datacenter = datacenter;
            this.context = context;
        }

        public Builder datacenter(final Datacenter datacenter)
        {
            checkNotNull(datacenter, ClientErrors.NULL_PARENT + Datacenter.class);
            this.datacenter = datacenter;
            return this;
        }

        public RemoteService build()
        {
            RemoteServiceDto dto = new RemoteServiceDto();
            dto.setId(id);

            RemoteService remoteservice = new RemoteService(context, dto);
            remoteservice.datacenter = datacenter;
            return remoteservice;
        }

        public static Builder fromRemoteService(final RemoteService in)
        {
            return null;
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }
}
