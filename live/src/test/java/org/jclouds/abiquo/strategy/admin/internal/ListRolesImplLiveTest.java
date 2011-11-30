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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.environment.EnterpriseTestEnvironment;
import org.jclouds.abiquo.predicates.enterprise.RolePredicates;
import org.jclouds.abiquo.strategy.BaseAbiquoStrategyLiveTest;
import org.testng.annotations.Test;

/**
 * Live tests for the {@link ListRolesImpl} strategy.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class ListRolesImplLiveTest extends BaseAbiquoStrategyLiveTest<EnterpriseTestEnvironment>
{
    private ListRolesImpl strategy;

    @Override
    protected EnterpriseTestEnvironment environment(final AbiquoContext context)
    {
        return new EnterpriseTestEnvironment(context);
    }

    @Override
    protected void setupStrategy()
    {
        this.strategy = injector.getInstance(ListRolesImpl.class);
    }

    public void testExecute()
    {
        Iterable<Role> roles = strategy.execute();
        assertNotNull(roles);
        assertTrue(size(roles) > 0);
    }

    public void testExecutePredicateWithoutResults()
    {
        Iterable<Role> roles = strategy.execute(RolePredicates.name("UNEXISTING"));
        assertNotNull(roles);
        assertEquals(size(roles), 0);
    }

    public void testExecutePredicateWithResults()
    {
        Iterable<Role> roles = strategy.execute(RolePredicates.name(env.role.getName()));
        assertNotNull(roles);
        assertEquals(size(roles), 1);
    }
}
