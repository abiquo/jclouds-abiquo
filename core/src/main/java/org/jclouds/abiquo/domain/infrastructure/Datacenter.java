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
import org.jclouds.abiquo.reference.AbiquoEdition;

import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.google.common.base.Predicate;

/**
 * Adds high level functionality to {@link DatacenterDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI18/Datacenter+Resource
 */
public class Datacenter extends DomainWrapper<DatacenterDto>
{
    /**
     * IP address of the datacenter (used to create all remote services with the same ip).
     */
    private String ip;

    /**
     * Indicates the Abiquo edition to create the available remote services.
     * 
     * @see http://wiki.abiquo.com/display/ABI18/Introduction+-+The+Abiquo+Platform
     */
    private AbiquoEdition edition;

    /**
     * Constructor to be used only by the builder.
     */
    protected Datacenter(final AbiquoContext context, final DatacenterDto target)
    {
        super(context, target);
    }

    @Override
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteDatacenter(target);
        target = null;
    }

    @Override
    public void save()
    {
        // If remote services data is set, create remote services.
        if (ip != null && edition != null)
        {
            createRemoteServices();
        }

        target = context.getApi().getInfrastructureClient().createDatacenter(target);
    }

    @Override
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateDatacenter(target);
    }

    public Iterable<Rack> listRacks()
    {
        return context.getInfrastructureService().listRacks(this);
    }

    public Iterable<Rack> listRacks(final Predicate<Rack> filter)
    {
        return context.getInfrastructureService().listRacks(this, filter);
    }

    public Rack findRack(final Predicate<Rack> filter)
    {
        return context.getInfrastructureService().findRack(this, filter);
    }

    public Iterable<RemoteService> listRemoteServices()
    {
        return context.getInfrastructureService().listRemoteServices(this);
    }

    public Iterable<RemoteService> listRemoteServices(final Predicate<RemoteService> filter)
    {
        return context.getInfrastructureService().listRemoteServices(this, filter);
    }

    public RemoteService findRemoteService(final Predicate<RemoteService> filter)
    {
        return context.getInfrastructureService().findRemoteService(this, filter);
    }

    private void createRemoteServices()
    {
        if (this.edition == AbiquoEdition.ENTERPRISE)
        {
            createRemoteService(RemoteServiceType.BPM_SERVICE);
            createRemoteService(RemoteServiceType.DHCP_SERVICE);
            createRemoteService(RemoteServiceType.STORAGE_SYSTEM_MONITOR);
            // TODO Community in Abiquo 2.0
            // createRemoteService(RemoteServiceType.NODE_COLLECTOR);
        }

        createRemoteService(RemoteServiceType.APPLIANCE_MANAGER);
        createRemoteService(RemoteServiceType.VIRTUAL_FACTORY);
        createRemoteService(RemoteServiceType.VIRTUAL_SYSTEM_MONITOR);
        createRemoteService(RemoteServiceType.BPM_SERVICE);
    }

    private void createRemoteService(final RemoteServiceType type)
    {
        RemoteService.builder(context, this).type(type)
            .uri(RemoteService.generateUri(this.ip, type)).build().save();
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

        private String ip;

        private AbiquoEdition edition;

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

        public Builder remoteServices(final String ip, final AbiquoEdition edition)
        {
            this.ip = ip;
            this.edition = edition;
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
            Datacenter datacenter = new Datacenter(context, dto);
            datacenter.edition = edition;
            datacenter.ip = ip;
            return datacenter;
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

    public void setLocation(final String location)
    {
        target.setLocation(location);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

}
