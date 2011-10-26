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

package org.jclouds.abiquo.features;

import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.Machine;
import org.jclouds.abiquo.domain.infrastructure.Rack;
import org.jclouds.abiquo.domain.infrastructure.RemoteService;
import org.jclouds.abiquo.internal.BaseInfrastructureService;

import com.google.common.base.Predicate;
import com.google.inject.ImplementedBy;

/**
 * Provides high level Abiquo operations.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@ImplementedBy(BaseInfrastructureService.class)
public interface InfrastructureService
{
    // Datacenter

    /**
     * Get the list of all datacenters.
     */
    Iterable<Datacenter> listDatacenters();

    /**
     * Get the list of datacenters matching the given filter.
     */
    Iterable<Datacenter> listDatacenters(final Predicate<Datacenter> filter);

    /**
     * Get the first datacenter that matches the given filter or <code>null</code> if none is found.
     */
    Datacenter findDatacenter(final Predicate<Datacenter> filter);

    // Rack

    /**
     * Get the list of the racks in the given datacenter.
     */
    Iterable<Rack> listRacks(final Datacenter datacenter);

    /**
     * Get the list of the racks in the given datacenter matching the given filter.
     */
    Iterable<Rack> listRacks(final Datacenter datacenter, final Predicate<Rack> filter);

    /**
     * Get the first rack in the given datacenter that matches the given filter or <code>null</code>
     * if none is found.
     */
    Rack findRack(final Datacenter datacenter, final Predicate<Rack> filter);

    // Remote service

    /**
     * Get the list of the remote services in the given datacenter.
     */
    Iterable<RemoteService> listRemoteServices(final Datacenter datacenter);

    /**
     * Get the list of the remote services in the given datacenter matching the given filters.
     */
    Iterable<RemoteService> listRemoteServices(final Datacenter datacenter,
        final Predicate<RemoteService> filter);

    /**
     * Get the first remote service in the given datacenter that matches the given filter or
     * <code>null</code> if none is found.
     */
    RemoteService findRemoteService(final Datacenter datacenter,
        final Predicate<RemoteService> filter);

    // Machine

    /**
     * Get the list of the machines in the given rack.
     */
    Iterable<Machine> listMachines(final Rack rack);

    /**
     * Get the list of the machines in the given rack matching the given filters.
     */
    Iterable<Machine> listMachines(final Rack rack, final Predicate<Machine> filter);

    /**
     * Get the first machine in the given rack that matches the given filter or <code>null</code> if
     * none is found.
     */
    Machine findMachine(final Rack rack, final Predicate<Machine> filter);
}
