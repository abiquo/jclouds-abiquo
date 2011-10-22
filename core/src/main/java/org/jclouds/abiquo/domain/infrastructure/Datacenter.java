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

import static com.google.common.collect.Iterables.transform;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.AbiquoEdition;

import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

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
        // Datacenter must be persisted first, so links get populated in the target object
        target = context.getApi().getInfrastructureClient().createDatacenter(target);

        // If remote services data is set, create remote services.
        if (ip != null && edition != null)
        {
            createRemoteServices();
        }
    }

    @Override
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateDatacenter(target);
    }

    public List<Rack> listRacks()
    {
        Iterable<Rack> racks = context.getInfrastructureService().listRacks(this);
        return Lists.newLinkedList(setDatacenterInAllRacks(racks));
    }

    public List<Rack> listRacks(final Predicate<Rack> filter)
    {
        Iterable<Rack> racks = context.getInfrastructureService().listRacks(this, filter);
        return Lists.newLinkedList(setDatacenterInAllRacks(racks));
    }

    public Rack findRack(final Predicate<Rack> filter)
    {
        Rack rack = context.getInfrastructureService().findRack(this, filter);
        rack.datacenter = this;
        return rack;
    }

    public List<RemoteService> listRemoteServices()
    {
        Iterable<RemoteService> remoteServices =
            context.getInfrastructureService().listRemoteServices(this);
        return Lists.newLinkedList(setDatacenterInAllRemoteServices(remoteServices));
    }

    public List<RemoteService> listRemoteServices(final Predicate<RemoteService> filter)
    {
        Iterable<RemoteService> remoteServices =
            context.getInfrastructureService().listRemoteServices(this, filter);
        return Lists.newLinkedList(setDatacenterInAllRemoteServices(remoteServices));
    }

    public RemoteService findRemoteService(final Predicate<RemoteService> filter)
    {
        RemoteService remoteService =
            context.getInfrastructureService().findRemoteService(this, filter);
        remoteService.datacenter = this;
        return remoteService;
    }

    private Iterable<Rack> setDatacenterInAllRacks(Iterable<Rack> racks)
    {
        return Lists.newLinkedList(transform(racks, new Function<Rack, Rack>()
        {
            @Override
            public Rack apply(Rack input)
            {
                input.datacenter = Datacenter.this;
                return input;
            }
        }));
    }

    private Iterable<RemoteService> setDatacenterInAllRemoteServices(Iterable<RemoteService> racks)
    {
        return Lists.newLinkedList(transform(racks, new Function<RemoteService, RemoteService>()
        {
            @Override
            public RemoteService apply(RemoteService input)
            {
                input.datacenter = Datacenter.this;
                return input;
            }
        }));
    }

    private void createRemoteServices()
    {
        if (this.edition == AbiquoEdition.ENTERPRISE)
        {
            createRemoteService(RemoteServiceType.BPM_SERVICE);
            createRemoteService(RemoteServiceType.DHCP_SERVICE);
            createRemoteService(RemoteServiceType.STORAGE_SYSTEM_MONITOR);
        }

        createRemoteService(RemoteServiceType.APPLIANCE_MANAGER);
        createRemoteService(RemoteServiceType.VIRTUAL_FACTORY);
        createRemoteService(RemoteServiceType.VIRTUAL_SYSTEM_MONITOR);
        createRemoteService(RemoteServiceType.NODE_COLLECTOR);
    }

    private void createRemoteService(final RemoteServiceType type)
    {
        RemoteService.builder(context, this).type(type).ip(this.ip).build().save();
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
