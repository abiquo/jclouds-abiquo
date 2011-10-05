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

package org.jclouds.abiquo.strategy.infrastructure;

import static com.google.common.collect.Iterables.size;
import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.Infrastructure;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.predicates.infrastructure.DatacenterPredicates;
import org.jclouds.abiquo.strategy.BaseAbiquoStrategyLiveTest;
import org.jclouds.abiquo.strategy.infrastructure.internal.ListDatacentersImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Live tests for the {@link ListDatacentersImpl} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class ListDatacentersImplLiveTest extends BaseAbiquoStrategyLiveTest
{
    private ListDatacentersImpl strategy;

    private AbiquoClient client;

    private DatacenterDto datacenter;

    @BeforeTest(groups = "live", dependsOnMethods = "setupClient")
    @Override
    protected void setupStrategy()
    {
        this.strategy = injector.getInstance(ListDatacentersImpl.class);
        this.client = injector.getInstance(AbiquoClient.class);
    }

    @Override
    protected void setupEntities()
    {
        Random generator = new Random(System.currentTimeMillis());
        DatacenterDto datacenter = Infrastructure.datacenterPost();
        datacenter.setName(PREFIX + datacenter.getName() + generator.nextInt(100));
        this.datacenter = client.getInfrastructureClient().createDatacenter(datacenter);
    }

    @Override
    protected void teardownEntities()
    {
        client.getInfrastructureClient().deleteDatacenter(datacenter.getId());
    }

    @Test
    public void testExecute()
    {
        Iterable<Datacenter> datacenters = strategy.execute();
        assertNotNull(datacenters);
        assertTrue(size(datacenters) > 0);
    }

    @Test
    public void testExecutePredicateWithoutResults()
    {
        Iterable<Datacenter> datacenters =
            strategy.execute(DatacenterPredicates.datacenterName("blabla"));
        assertNotNull(datacenters);
        assertEquals(size(datacenters), 0);
    }

    @Test
    public void testExecutePredicateWithResults()
    {
        Iterable<Datacenter> datacenters =
            strategy.execute(DatacenterPredicates.datacenterName(datacenter.getName()));
        assertNotNull(datacenters);
        assertEquals(size(datacenters), 1);
    }

}
