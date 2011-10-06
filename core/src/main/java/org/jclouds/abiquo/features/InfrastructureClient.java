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

import org.jclouds.concurrent.Timeout;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.DatacentersDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.abiquo.server.core.infrastructure.RacksDto;

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
     * @param datacenter The idatacenter to delete.
     */
    void deleteDatacenter(DatacenterDto datacenter);

    /**
     * List all not managed racks for a datacenter.
     * 
     * @param datacenter The datacenter.
     * @return The list of not managed racks for the datacenter.
     */
    RacksDto listRacks(DatacenterDto dataceter);

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
     * @return The datacenter or <code>null</code> if it does not exist.
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

    /**
     * List all remote services of the datacenter.
     * 
     * @param datacenter The datacenter.
     * @return The list of remote services for the datacenter.
     */
    RacksDto listRemoteServices(DatacenterDto dataceter);
}
