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

package org.jclouds.abiquo.strategy.cloud.internal;

import static com.google.common.collect.Iterables.size;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.predicates.cloud.VirtualMachinePredicates;
import org.jclouds.abiquo.strategy.BaseAbiquoStrategyLiveTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Live tests for the {@link ListVirtualMachinesImpl} strategy.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class ListVirtualMachinesImplLiveTest extends
    BaseAbiquoStrategyLiveTest<CloudTestEnvironment>
{
    private ListVirtualMachinesImpl strategy;

    @Override
    @BeforeClass(groups = "live")
    protected void setupStrategy()
    {
        this.strategy = context.getUtils().getInjector().getInstance(ListVirtualMachinesImpl.class);
    }

    public void testExecute()
    {
        Iterable<VirtualMachine> vms = strategy.execute();
        assertNotNull(vms);
        assertTrue(size(vms) > 0);
    }

    public void testExecutePredicateWithoutResults()
    {
        Iterable<VirtualMachine> vms =
            strategy.execute(VirtualMachinePredicates.name("UNEXISTING"));
        assertNotNull(vms);
        assertEquals(size(vms), 0);
    }

    public void testExecutePredicateWithResults()
    {
        Iterable<VirtualMachine> vms =
            strategy.execute(VirtualMachinePredicates.name(env.virtualMachine.getName()));
        assertNotNull(vms);
        assertEquals(size(vms), 1);
    }
}
