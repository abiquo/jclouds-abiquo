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

/**
 * Provides synchronous access to Abiquo Infrastructure API.
 * 
 * @see http://community.abiquo.com/display/ABI18/API+Reference
 * @see InfrastructureAsyncClient
 * @author Ignasi Barrera
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
     * Gte the given datacenter.
     * 
     * @param datacenterId The id of the datacenter.
     * @return The datacenter or <code>null</code> if it does not exist.
     */
    DatacenterDto getDatacenter(Integer datacenterId);

    /**
     * Updates an existing datacenter.
     * 
     * @param datacenterId The id of the datacenter to update.
     * @param datacenter The new attributes for the datacenter.
     * @return The updated datacenter.
     */
    DatacenterDto updateDatacenter(Integer datacenterId, DatacenterDto datacenter);

    /**
     * Deletes an existing datacenter.
     * 
     * @param datacenterId The id of the datacenter to delete.
     */
    void deleteDatacenter(Integer datacenterId);
}
