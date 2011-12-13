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

package org.jclouds.abiquo.features.services;

import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.Volume;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.internal.BaseCloudService;

import com.google.common.base.Predicate;
import com.google.inject.ImplementedBy;

/**
 * Provides high level Abiquo cloud operations.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@ImplementedBy(BaseCloudService.class)
public interface CloudService
{
    /*********************** VirtualDatacenter ***********************/

    /**
     * Get the list of all virtual datacenters.
     */
    Iterable<VirtualDatacenter> listVirtualDatacenters();

    /**
     * Get the list of all virtual datacenters for a pair enterprise-datacenter.
     * 
     * @param enterprise The given enterprise.
     * @param datacenter The given datacenter.
     */
    Iterable<VirtualDatacenter> listVirtualDatacenters(Enterprise enterprise);

    /**
     * Get the list of virtual datacenters matching the given filter.
     */
    Iterable<VirtualDatacenter> listVirtualDatacenters(final Predicate<VirtualDatacenter> filter);

    /**
     * Get the first virtual datacenter that matches the given filter or <code>null</code> if none
     * is found.
     */
    VirtualDatacenter findVirtualDatacenter(final Predicate<VirtualDatacenter> filter);

    /**
     * Get the virtual datacenter with the given id.
     */
    VirtualDatacenter getVirtualDatacenter(Integer virtualDatacenterId);

    /**
     * Detach the given volumes from the given virtual machine.
     * 
     * @param virtualMachine The virtual machine.
     * @param volumes The volumes to detach.
     */
    void detachVolumes(VirtualMachine virtualMachine, Volume... volumes);
}
