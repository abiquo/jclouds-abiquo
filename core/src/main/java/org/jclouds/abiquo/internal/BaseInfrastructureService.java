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

package org.jclouds.abiquo.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.Rack;
import org.jclouds.abiquo.domain.infrastructure.RemoteService;
import org.jclouds.abiquo.features.InfrastructureService;
import org.jclouds.abiquo.strategy.infrastructure.ListDatacenters;
import org.jclouds.abiquo.strategy.infrastructure.ListRacks;
import org.jclouds.abiquo.strategy.infrastructure.ListRemoteServices;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Provides high level Abiquo operations.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Singleton
public class BaseInfrastructureService implements InfrastructureService
{
    private final ListDatacenters listDatacenters;

    private final ListRacks listRacks;

    private final ListRemoteServices listRemoteServices;

    @Inject
    protected BaseInfrastructureService(final ListDatacenters listDatacenters,
        final ListRacks listRacks, final ListRemoteServices listRemoteServices)
    {
        this.listDatacenters = checkNotNull(listDatacenters, "listDatacenters");
        this.listRacks = checkNotNull(listRacks, "listRacks");
        this.listRemoteServices = checkNotNull(listRemoteServices, "listRemoteServices");
    }

    @Override
    public Iterable<Datacenter> listDatacenters()
    {
        return listDatacenters.execute();
    }

    @Override
    public Iterable<Datacenter> listDatacenters(final Predicate<Datacenter> filter)
    {
        return listDatacenters.execute(filter);
    }

    @Override
    public Datacenter findDatacenter(final Predicate<Datacenter> filter)
    {
        return Iterables.getFirst(listDatacenters(), null);
    }

    @Override
    public Iterable<Rack> listRacks(final Datacenter datacenter)
    {
        return listRacks.execute(datacenter);
    }

    @Override
    public Iterable<Rack> listRacks(final Datacenter datacenter, final Predicate<Rack> filter)
    {
        return listRacks.execute(datacenter, filter);
    }

    @Override
    public Rack findRack(final Datacenter datacenter, final Predicate<Rack> filter)
    {
        return Iterables.getFirst(listRacks(datacenter), null);
    }

    @Override
    public Iterable<RemoteService> listRemoteServices(final Datacenter datacenter)
    {
        return listRemoteServices.execute(datacenter);
    }

    @Override
    public Iterable<RemoteService> listRemoteServices(final Datacenter datacenter,
        final Predicate<RemoteService> filter)
    {
        return listRemoteServices.execute(datacenter, filter);
    }

    @Override
    public RemoteService findRemoteService(final Datacenter datacenter,
        final Predicate<RemoteService> filter)
    {
        return Iterables.getFirst(listRemoteServices(datacenter), null);
    }

}
