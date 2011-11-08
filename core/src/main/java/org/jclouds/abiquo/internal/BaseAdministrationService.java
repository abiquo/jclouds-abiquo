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

import org.jclouds.abiquo.domain.config.License;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.features.AdministrationService;
import org.jclouds.abiquo.strategy.GlobalResources;
import org.jclouds.abiquo.strategy.admin.ListRoles;
import org.jclouds.abiquo.strategy.config.ListLicenses;
import org.jclouds.abiquo.strategy.enterprise.ListEnterprises;
import org.jclouds.abiquo.strategy.infrastructure.ListDatacenters;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Provides high level Abiquo administration operations.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Singleton
public class BaseAdministrationService implements AdministrationService
{
    private final ListDatacenters listDatacenters;

    private final ListEnterprises listEnterprises;

    private final ListRoles listRoles;

    private final ListLicenses listLicenses;

    private final GlobalResources globalResources;

    /*********************** Datacenter ********************** */

    @Inject
    protected BaseAdministrationService(final ListDatacenters listDatacenters,
        final ListEnterprises listEnterprises, final ListRoles listRoles,
        final ListLicenses listLicenses, final GlobalResources globalResources)
    {
        this.listDatacenters = checkNotNull(listDatacenters, "listDatacenters");
        this.listEnterprises = checkNotNull(listEnterprises, "listEnterprises");
        this.listRoles = checkNotNull(listRoles, "listRoles");
        this.listLicenses = checkNotNull(listLicenses, "listLicenses");
        this.globalResources = checkNotNull(globalResources, "globalResources");
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
        return Iterables.getFirst(listDatacenters(filter), null);
    }

    /*********************** Enterprise ********************** */

    @Override
    public Iterable<Enterprise> listEnterprises()
    {
        return listEnterprises.execute();
    }

    @Override
    public Iterable<Enterprise> listEnterprises(final Predicate<Enterprise> filter)
    {
        return listEnterprises.execute(filter);
    }

    @Override
    public Enterprise findEnterprise(final Predicate<Enterprise> filter)
    {
        return Iterables.getFirst(listEnterprises(filter), null);
    }

    /*********************** Role ********************** */

    @Override
    public Iterable<Role> listRoles()
    {
        return listRoles.execute();
    }

    @Override
    public Iterable<Role> listRoles(final Predicate<Role> filter)
    {
        return listRoles.execute(filter);
    }

    @Override
    public Role findRole(final Predicate<Role> filter)
    {
        return Iterables.getFirst(listRoles(filter), null);
    }

    /*********************** User ********************** */

    @Override
    public User getCurrentUserInfo()
    {
        return globalResources.getLogin();
    }

    /*********************** License ********************** */

    @Override
    public Iterable<License> listLicenses()
    {
        return listLicenses.execute();
    }

    @Override
    public Iterable<License> listLicenses(final boolean active)
    {
        return listLicenses.execute(active);
    }

    @Override
    public Iterable<License> listLicenses(final Predicate<License> filter)
    {
        return listLicenses.execute(filter);
    }

    @Override
    public License findLicense(final Predicate<License> filter)
    {
        return Iterables.getFirst(listLicenses(filter), null);
    }
}
