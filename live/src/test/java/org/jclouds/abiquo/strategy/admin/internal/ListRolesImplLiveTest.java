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

package org.jclouds.abiquo.strategy.admin.internal;

import static com.google.common.collect.Iterables.size;
import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.AdminResources;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.predicates.enterprise.RolePredicates;
import org.jclouds.abiquo.strategy.BaseAbiquoStrategyLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.enterprise.RoleDto;

/**
 * Live tests for the {@link ListRolesImpl} strategy.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class ListRolesImplLiveTest extends BaseAbiquoStrategyLiveTest
{
    private ListRolesImpl strategy;

    private AbiquoClient client;

    private RoleDto role;

    @Override
    protected void setupStrategy()
    {
        this.strategy = injector.getInstance(ListRolesImpl.class);
        this.client = injector.getInstance(AbiquoClient.class);
    }

    @Override
    protected void setup()
    {
        Random generator = new Random(System.currentTimeMillis());
        RoleDto role = AdminResources.rolePost();
        role.setName(PREFIX + role.getName() + generator.nextInt(100));
        this.role = client.getAdminClient().createRole(role);
    }

    @Override
    protected void tearDown()
    {
        client.getAdminClient().deleteRole(role);
    }

    public void testExecute()
    {
        Iterable<Role> roles = strategy.execute();
        assertNotNull(roles);
        assertTrue(size(roles) > 0);
    }

    public void testExecutePredicateWithoutResults()
    {
        Iterable<Role> roles = strategy.execute(RolePredicates.roleName("UNEXISTING"));
        assertNotNull(roles);
        assertEquals(size(roles), 0);
    }

    public void testExecutePredicateWithResults()
    {
        Iterable<Role> roles = strategy.execute(RolePredicates.roleName(role.getName()));
        assertNotNull(roles);
        assertEquals(size(roles), 1);
    }
}
