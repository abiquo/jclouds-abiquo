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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.strategy.admin.internal.ListRolesImpl;
import org.jclouds.abiquo.strategy.config.internal.ListLicensesImpl;
import org.jclouds.abiquo.strategy.config.internal.ListPrivilegesImpl;
import org.jclouds.abiquo.strategy.enterprise.internal.ListEnterprisesImpl;
import org.jclouds.abiquo.strategy.infrastructure.internal.ListDatacentersImpl;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link BaseAdministrationService} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class BaseAdministrationServiceTest extends BaseInjectionTest
{

    public void testAllPropertiesInjected()
    {
        BaseAdministrationService service =
            (BaseAdministrationService) injector.getInstance(AdministrationService.class);

        assertNotNull(service.context);
        assertNotNull(service.listDatacenters);
        assertNotNull(service.listEnterprises);
        assertNotNull(service.listLicenses);
        assertNotNull(service.listPrivileges);
        assertNotNull(service.listRoles);

        assertTrue(service.context instanceof AbiquoContextImpl);
        assertTrue(service.listDatacenters instanceof ListDatacentersImpl);
        assertTrue(service.listEnterprises instanceof ListEnterprisesImpl);
        assertTrue(service.listLicenses instanceof ListLicensesImpl);
        assertTrue(service.listPrivileges instanceof ListPrivilegesImpl);
        assertTrue(service.listRoles instanceof ListRolesImpl);
    }
}
