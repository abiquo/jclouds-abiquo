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
import org.jclouds.abiquo.internal.BaseInfrastructureService;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.google.common.base.Predicate;
import com.google.inject.ImplementedBy;

/**
 * Provides high level Abiquo operations.
 * 
 * @author Ignasi Barrera
 */
@ImplementedBy(BaseInfrastructureService.class)
public interface InfrastructureService
{

    /**
     * Get the list of all datacenters.
     */
    Iterable<Datacenter> listDatacenters();

    /**
     * Creates a new datacenter.
     */
    Datacenter createDatacenter(final String name, final String location);

    /**
     * Get the list of datacenters matching the given filter.
     */
    Iterable<Datacenter> listDatacenters(Predicate<DatacenterDto> filter);

    /**
     * Get the datacenter with the given id, or return <code>null</code> if not found.
     */
    Datacenter getDatacenter(Integer datacenterId);

    /**
     * Delete the given datacenter .
     * 
     * @param datacenterId The id of the datacenter to delete.
     */
    void deleteDatacenter(final Integer datacenterId);

    /**
     * Update the given datacenter with the new values.
     * 
     * @param dc New values for the Datacenter.
     * @return Datacenter with the new values;
     */
    Datacenter updateDatacenter(Datacenter dc);
}