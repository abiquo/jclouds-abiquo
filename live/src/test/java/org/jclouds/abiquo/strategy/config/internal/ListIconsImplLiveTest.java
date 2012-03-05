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

import org.jclouds.abiquo.domain.config.Icon;
import org.jclouds.abiquo.predicates.config.IconPredicates;
import org.jclouds.abiquo.strategy.BaseAbiquoStrategyLiveTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Predicates;

/**
 * Live tests for the {@link ListIconsImpl} strategy.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class ListIconsImplLiveTest extends BaseAbiquoStrategyLiveTest
{
    private ListIconsImpl strategy;

    @Override
    @BeforeClass(groups = "live")
    protected void setupStrategy()
    {
        this.strategy = context.getUtils().getInjector().getInstance(ListIconsImpl.class);
    }

    public void testExecute()
    {
        Iterable<Icon> icons = strategy.execute();
        assertNotNull(icons);
        assertTrue(size(icons) > 0);
    }

    public void testExecutePredicateWithoutResults()
    {
        Iterable<Icon> icons = strategy.execute(IconPredicates.name("windows 3.1"));
        assertNotNull(icons);
        assertEquals(size(icons), 0);
    }

    public void testExecutePredicateWithResults()
    {
        Iterable<Icon> icons = strategy.execute(IconPredicates.name("ubuntu"));
        assertNotNull(icons);
        assertEquals(size(icons), 1);
    }

    public void testExecuteNotPredicateWithResults()
    {
        Iterable<Icon> icons = strategy.execute(Predicates.not(IconPredicates.name("ubuntu")));

        Iterable<Icon> allIcons = strategy.execute();

        assertNotNull(icons);
        assertNotNull(allIcons);
        assertEquals(size(icons), size(allIcons) - 1);
    }
}
