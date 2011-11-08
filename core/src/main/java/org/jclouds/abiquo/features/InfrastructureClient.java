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

import java.util.concurrent.TimeUnit;

import org.jclouds.abiquo.domain.infrastructure.options.MachineOptions;
import org.jclouds.concurrent.Timeout;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.DatacentersDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.MachinesDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.abiquo.server.core.infrastructure.RacksDto;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;
import com.abiquo.server.core.infrastructure.RemoteServicesDto;

/**
 * Provides synchronous access to Abiquo Infrastructure API.
 * 
 * @see http://community.abiquo.com/display/ABI18/API+Reference
 * @see InfrastructureAsyncClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Timeout(duration = 30, timeUnit = TimeUnit.SECONDS)
public interface InfrastructureClient
{
    /*          ********************** Datacenter ********************** */

    /**
     * List all datacenters.
     * 
     * @return The list of Datacenters.
     */
    DatacentersDto listDatacenters();

    /**
     * Create a new datacenter.
     * 
     * @param datacenter The datacenter to be created.
     * @return The created datacenter.
     */
    DatacenterDto createDatacenter(DatacenterDto datacenter);

    /**
     * Get the given datacenter.
     * 
     * @param datacenterId The id of the datacenter.
     * @return The datacenter or <code>null</code> if it does not exist.
     */
    DatacenterDto getDatacenter(Integer datacenterId);

    /**
     * Updates an existing datacenter.
     * 
     * @param datacenter The new attributes for the datacenter.
     * @return The updated datacenter.
     */
    DatacenterDto updateDatacenter(DatacenterDto datacenter);

    /**
     * Deletes an existing datacenter.
     * 
     * @param datacenter The datacenter to delete.
     */
    void deleteDatacenter(DatacenterDto datacenter);

    /**
     * Retrieve remote machine information.
     * 
     * @see http://community.abiquo.com/display/ABI18/Datacenter+Resource#DatacenterResource-
     *      Retrieveremotemachineinformation
     * @param datacenter The datacenter.
     * @param ip IP address of the remote hypervisor to connect.
     * @param hypervisorType Kind of hypervisor we want to connect. Valid values are {vbox, kvm,
     *            xen-3, vmx-04, hyperv-301, xenserver}.
     * @param user User to log in.
     * @param password Password to authenticate.
     * @return The physical machine.
     */
    MachineDto discoverSingleMachine(DatacenterDto datacenter, String ip,
        HypervisorType hypervisorType, String user, String password);

    /**
     * Retrieve remote machine information.
     * 
     * @see http://community.abiquo.com/display/ABI18/Datacenter+Resource#DatacenterResource-
     *      Retrieveremotemachineinformation
     * @param datacenter The datacenter.
     * @param ip IP address of the remote hypervisor to connect.
     * @param hypervisorType Kind of hypervisor we want to connect. Valid values are {vbox, kvm,
     *            xen-3, vmx-04, hyperv-301, xenserver}.
     * @param user User to log in.
     * @param password Password to authenticate.
     * @param options Optional query params.
     * @return The physical machine.
     */
    MachineDto discoverSingleMachine(DatacenterDto datacenter, String ip,
        HypervisorType hypervisorType, String user, String password, MachineOptions options);

    /**
     * Retrieve a list of remote machine information.
     * 
     * @see http://community.abiquo.com/display/ABI18/Datacenter+Resource#DatacenterResource-
     *      Retrievealistofremotemachineinformation
     * @param datacenter The datacenter.
     * @param ipFrom IP address of the remote first hypervisor to check.
     * @param ipTo IP address of the remote last hypervisor to check.
     * @param hypervisorType Kind of hypervisor we want to connect. Valid values are {vbox, kvm,
     *            xen-3, vmx-04, hyperv-301, xenserver}.
     * @param user User to log in.
     * @param password Password to authenticate.
     * @return The physical machine list or <code>null</code> if there are no machines in the ip
     *         range.
     */
    MachinesDto discoverMultipleMachines(final DatacenterDto datacenter, final String ipFrom,
        final String ipTo, final HypervisorType hypervisorType, final String user,
        final String password);

    /**
     * Retrieve a list of remote machine information.
     * 
     * @see http://community.abiquo.com/display/ABI18/Datacenter+Resource#DatacenterResource-
     *      Retrievealistofremotemachineinformation
     * @param datacenter The datacenter.
     * @param ipFrom IP address of the remote first hypervisor to check.
     * @param ipTo IP address of the remote last hypervisor to check.
     * @param hypervisorType Kind of hypervisor we want to connect. Valid values are {vbox, kvm,
     *            xen-3, vmx-04, hyperv-301, xenserver}.
     * @param user User to log in.
     * @param password Password to authenticate.
     * @param options Optional query params.
     * @return The physical machine list or <code>null</code> if there are no machines in the ip
     *         range.
     */
    MachinesDto discoverMultipleMachines(final DatacenterDto datacenter, final String ipFrom,
        final String ipTo, final HypervisorType hypervisorType, final String user,
        final String password, final MachineOptions options);

    /**
     * Retreives limits for the given datacenter and any enterprise.
     * 
     * @param datacenter The datacenter.
     * @return The usage limits for the datacenter on any enterprise.
     */
    DatacentersLimitsDto listLimits(DatacenterDto datacenter);

    /*          ********************** Rack ********************** */

    /**
     * List all not managed racks for a datacenter.
     * 
     * @param datacenter The datacenter.
     * @return The list of not managed racks for the datacenter.
     */
    RacksDto listRacks(DatacenterDto datacenter);

    /**
     * Create a new not managed rack in a datacenter.
     * 
     * @param datacenter The datacenter.
     * @param rack The rack to be created.
     * @return The created rack.
     */
    RackDto createRack(final DatacenterDto datacenter, final RackDto rack);

    /**
     * Get the given rack from the given datacenter.
     * 
     * @param datacenter The datacenter.
     * @param rackId The id of the rack.
     * @return The rack or <code>null</code> if it does not exist.
     */
    RackDto getRack(DatacenterDto datacenter, Integer rackId);

    /**
     * Updates an existing rack from the given datacenter.
     * 
     * @param rack The new attributes for the rack.
     * @return The updated rack.
     */
    RackDto updateRack(final RackDto rack);

    /**
     * Deletes an existing rack.
     * 
     * @param rack The rack to delete.
     */
    void deleteRack(final RackDto rack);

    /*          ********************** Remote Service ********************** */

    /**
     * List all remote services of the datacenter.
     * 
     * @param datacenter The datacenter.
     * @return The list of remote services for the datacenter.
     */
    RemoteServicesDto listRemoteServices(DatacenterDto dataceter);

    /**
     * Create a new remote service in a datacenter.
     * 
     * @param datacenter The datacenter.
     * @param remoteService The remote service to be created.
     * @return The created remote service.
     */
    RemoteServiceDto createRemoteService(final DatacenterDto datacenter,
        final RemoteServiceDto remoteService);

    /**
     * Get the given remote service from the given datacenter.
     * 
     * @param datacenter The datacenter.
     * @param remoteServiceType The type of the remote service.
     * @return The remote service or <code>null</code> if it does not exist.
     */
    RemoteServiceDto getRemoteService(DatacenterDto datacenter, RemoteServiceType remoteServiceType);

    /**
     * Updates an existing remote service from the given datacenter.
     * 
     * @param remoteService The new attributes for the remote service.
     * @return The updated remote service.
     */
    RemoteServiceDto updateRemoteService(RemoteServiceDto remoteService);

    /**
     * Deletes an existing remote service.
     * 
     * @param remoteService The remote service to delete.
     */
    void deleteRemoteService(RemoteServiceDto remoteService);

    /**
     * Check if the given remote service is available and properly configured.
     * 
     * @param remoteService The remote service to check.
     * @return A Boolean indicating if the remote service is available.
     */
    boolean isAvailable(RemoteServiceDto remoteService);

    /*          ********************** Machine ********************** */

    /**
     * Create a new physical machine in a rack.
     * 
     * @param rack The rack.
     * @param machine The physical machine to be created.
     * @return The created physical machine.
     */
    MachineDto createMachine(RackDto rack, MachineDto machine);

    /**
     * Get the given machine from the given rack.
     * 
     * @param rack The rack.
     * @param machineId The id of the machine.
     * @return The machine or <code>null</code> if it does not exist.
     */
    MachineDto getMachine(RackDto rack, Integer machineId);

    /**
     * Updates an existing physical machine.
     * 
     * @param machine The new attributes for the physical machine.
     * @return The updated machine.
     */
    MachineDto updateMachine(MachineDto machine);

    /**
     * Deletes an existing physical machine.
     * 
     * @param machine The physical machine to delete.
     */
    void deleteMachine(MachineDto machine);

    /**
     * List all machines racks for a rack.
     * 
     * @param rack The rack.
     * @return The list of physical machines for the rack.
     */
    MachinesDto listMachines(RackDto rack);
}
