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
import static org.jclouds.abiquo.domain.DomainWrapper.wrap;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.config.License;
import org.jclouds.abiquo.domain.config.Privilege;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.strategy.admin.ListRoles;
import org.jclouds.abiquo.strategy.config.ListLicenses;
import org.jclouds.abiquo.strategy.config.ListPrivileges;
import org.jclouds.abiquo.strategy.enterprise.ListEnterprises;
import org.jclouds.abiquo.strategy.infrastructure.ListDatacenters;

import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.enterprise.RoleDto;
import com.abiquo.server.core.enterprise.UserDto;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.google.common.annotations.VisibleForTesting;
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
    @VisibleForTesting
    protected AbiquoContext context;

    @VisibleForTesting
    protected final ListDatacenters listDatacenters;

    @VisibleForTesting
    protected final ListEnterprises listEnterprises;

    @VisibleForTesting
    protected final ListRoles listRoles;

    @VisibleForTesting
    protected final ListLicenses listLicenses;

    @VisibleForTesting
    protected final ListPrivileges listPrivileges;

    @Inject
    protected BaseAdministrationService(final AbiquoContext context,
        final ListDatacenters listDatacenters, final ListEnterprises listEnterprises,
        final ListRoles listRoles, final ListLicenses listLicenses,
        final ListPrivileges listPrivileges)
    {
        this.context = checkNotNull(context, "context");
        this.listDatacenters = checkNotNull(listDatacenters, "listDatacenters");
        this.listEnterprises = checkNotNull(listEnterprises, "listEnterprises");
        this.listRoles = checkNotNull(listRoles, "listRoles");
        this.listLicenses = checkNotNull(listLicenses, "listLicenses");
        this.listPrivileges = checkNotNull(listPrivileges, "listPrivileges");
    }

    /*********************** Datacenter ********************** */

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
    public Datacenter getDatacenter(final Integer datacenterId)
    {
        DatacenterDto datacenter =
            context.getApi().getInfrastructureClient().getDatacenter(datacenterId);
        return wrap(context, Datacenter.class, datacenter);
    }

    @Override
    public Datacenter findDatacenter(final Predicate<Datacenter> filter)
    {
        return Iterables.getFirst(listDatacenters(filter), null);
    }

    /*********************** Enterprise ***********************/

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

    @Override
    public Enterprise getEnterprise(final Integer enterpriseId)
    {
        EnterpriseDto enterprise =
            context.getApi().getEnterpriseClient().getEnterprise(enterpriseId);
        return wrap(context, Enterprise.class, enterprise);
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

    @Override
    public Role getRole(final Integer roleId)
    {
        RoleDto role = context.getApi().getAdminClient().getRole(roleId);
        return wrap(context, Role.class, role);
    }

    /*********************** Privilege ***********************/

    @Override
    public Privilege findPrivilege(final Predicate<Privilege> filter)
    {
        return Iterables.getFirst(listPrivileges(filter), null);
    }

    @Override
    public Iterable<Privilege> listPrivileges()
    {
        return listPrivileges.execute();
    }

    @Override
    public Iterable<Privilege> listPrivileges(final Predicate<Privilege> filter)
    {
        return listPrivileges.execute(filter);
    }

    /*********************** User ***********************/

    @Override
    public User getCurrentUserInfo()
    {
        UserDto result = context.getApi().getAdminClient().getCurrentUser();
        return wrap(context, User.class, result);
    }

    /*********************** License ***********************/

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
