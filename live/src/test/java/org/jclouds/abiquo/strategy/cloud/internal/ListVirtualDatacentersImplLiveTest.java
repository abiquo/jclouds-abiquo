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

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.options.VirtualDatacenterOptions;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.predicates.cloud.VirtualDatacenterPredicates;
import org.jclouds.abiquo.strategy.BaseAbiquoStrategyLiveTest;
import org.testng.annotations.Test;

/**
 * Live tests for the {@link ListDatacentersImpl} strategy.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class ListVirtualDatacentersImplLiveTest extends
    BaseAbiquoStrategyLiveTest<CloudTestEnvironment>
{
    private ListVirtualDatacentersImpl strategy;

    @Override
    protected CloudTestEnvironment environment(final AbiquoContext context)
    {
        return new CloudTestEnvironment(context);
    }

    @Override
    protected void setupStrategy()
    {
        this.strategy = injector.getInstance(ListVirtualDatacentersImpl.class);
    }

    public void testExecute()
    {
        Iterable<VirtualDatacenter> virtualDatacenters = strategy.execute();
        assertNotNull(virtualDatacenters);
        assertTrue(size(virtualDatacenters) > 0);
    }

    public void testExecutePredicateWithoutResults()
    {
        Iterable<VirtualDatacenter> datacenters =
            strategy.execute(VirtualDatacenterPredicates.name("UNEXISTING"));
        assertNotNull(datacenters);
        assertEquals(size(datacenters), 0);
    }

    public void testExecutePredicateWithResults()
    {
        Iterable<VirtualDatacenter> virtualDatacenters =
            strategy.execute(VirtualDatacenterPredicates.name(env.virtualDatacenter.getName()));
        assertNotNull(virtualDatacenters);
        assertEquals(size(virtualDatacenters), 1);
    }

    public void testExecutePredicateOptionsWithResults()
    {
        Iterable<VirtualDatacenter> virtualDatacenters =
            strategy.execute(VirtualDatacenterOptions.builder()
                .datacenterId(env.datacenter.getId()).enterpriseId(env.defaultEnterprise.getId())
                .build());
        assertNotNull(virtualDatacenters);
        assertEquals(size(virtualDatacenters), 1);
    }

    public void testExecutePredicateOptionsWithoutResults()
    {
        Iterable<VirtualDatacenter> virtualDatacenters =
            strategy.execute(VirtualDatacenterOptions.builder()
                .enterpriseId(env.enterprise.getId()).build());
        assertNotNull(virtualDatacenters);
        assertEquals(size(virtualDatacenters), 0);
    }
}
