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

package org.jclouds.abiquo.strategy.config.internal;

import static com.google.common.collect.Iterables.size;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.config.Privilege;
import org.jclouds.abiquo.environment.ConfigTestEnvironment;
import org.jclouds.abiquo.predicates.configuration.PrivilegePredicates;
import org.jclouds.abiquo.strategy.BaseAbiquoStrategyLiveTest;
import org.testng.annotations.Test;

/**
 * Live tests for the {@link ListPrivilegesImpl} strategy.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class ListPrivilegesImplLiveTest extends BaseAbiquoStrategyLiveTest<ConfigTestEnvironment>
{
    private ListPrivilegesImpl strategy;

    @Override
    protected ConfigTestEnvironment environment(final AbiquoContext context)
    {
        return new ConfigTestEnvironment(context);
    }

    @Override
    protected void setupStrategy()
    {
        this.strategy = injector.getInstance(ListPrivilegesImpl.class);
    }

    public void testExecute()
    {
        Iterable<Privilege> privileges = strategy.execute();
        assertNotNull(privileges);
        assertTrue(size(privileges) > 0);
    }

    public void testExecutePredicateWithoutResults()
    {
        Iterable<Privilege> privileges =
            strategy.execute(PrivilegePredicates.name("Destroy the universe"));
        assertNotNull(privileges);
        assertEquals(size(privileges), 0);
    }

    public void testExecutePredicateWithResults()
    {
        Iterable<Privilege> privileges =
            strategy.execute(PrivilegePredicates.name("USERS_MANAGE_USERS"));
        assertNotNull(privileges);
        assertEquals(size(privileges), 1);
    }

    public void testExecuteNotPredicateWithResults()
    {
        Iterable<Privilege> privileges =
            strategy.execute(PrivilegePredicates.nameNot("USERS_MANAGE_USERS"));

        Iterable<Privilege> allPrivileges = strategy.execute();

        assertNotNull(privileges);
        assertNotNull(allPrivileges);
        assertEquals(size(privileges), size(allPrivileges) - 1);
    }
}
