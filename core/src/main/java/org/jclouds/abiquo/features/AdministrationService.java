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

import org.jclouds.abiquo.domain.config.License;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.internal.BaseAdministrationService;

import com.google.common.base.Predicate;
import com.google.inject.ImplementedBy;

/**
 * Provides high level Abiquo operations.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@ImplementedBy(BaseAdministrationService.class)
public interface AdministrationService
{
    /* ********************** Datacenter ********************** */

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

    /* ********************** Enterprise ********************** */

    /**
     * Get the list of all enterprises.
     */
    Iterable<Enterprise> listEnterprises();

    /**
     * Get the list of enterprises matching the given filter.
     */
    Iterable<Enterprise> listEnterprises(final Predicate<Enterprise> filter);

    /**
     * Get the first enterprises that matches the given filter or <code>null</code> if none is
     * found.
     */
    Enterprise findEnterprise(final Predicate<Enterprise> filter);

    /* ********************** Role ********************** */

    /**
     * Get the list of global roles.
     */
    Iterable<Role> listRoles();

    /**
     * Get the list of roles matching the given filter.
     */
    Iterable<Role> listRoles(final Predicate<Role> filter);

    /**
     * Get the first role that matches the given filter or <code>null</code> if none is found.
     */
    Role findRole(final Predicate<Role> filter);

    /* ********************** User ********************** */

    /**
     * Get the current user.
     */
    User getCurrentUserInfo();

    /* ********************** License ********************** */

    /**
     * Get the list of all licenses.
     */
    Iterable<License> listLicenses();

    /**
     * Get the list of all active/inactive licenses.
     * 
     * @param active Defines if searching for active (<code>true</code>) or inactive (
     *            <code>false</code>) licenses.
     */
    Iterable<License> listLicenses(boolean active);

    /**
     * Get the list of licenses matching the given filter.
     */
    Iterable<License> listLicenses(final Predicate<License> filter);

    /**
     * Get the first license that matches the given filter or <code>null</code> if none is found.
     */
    License findLicense(final Predicate<License> filter);
}
