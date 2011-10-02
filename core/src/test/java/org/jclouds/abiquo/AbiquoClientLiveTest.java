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

package org.jclouds.abiquo;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.Properties;

import org.jclouds.abiquo.predicates.DatacenterPredicates;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Tests behavior of {@code AbiquoClient}
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class AbiquoClientLiveTest
{
    protected AbiquoContext context;

    protected AbiquoService abiquoService;

    protected String provider = "abiquo";

    protected String identity;

    protected String credential;

    protected String endpoint;

    protected String apiVersion;

    protected void setupCredentials()
    {
        identity = checkNotNull(System.getProperty("test.abiquo.identity"), "test.abiquo.identity");
        credential =
            checkNotNull(System.getProperty("test.abiquo.credential"), "test.abiquo.credential");
        endpoint = checkNotNull(System.getProperty("test.abiquo.endpoint"), "test.abiquo.endpoint");
        apiVersion =
            checkNotNull(System.getProperty("test.abiquo.apiversion"), "test.abiquo.apiversion");
    }

    @BeforeGroups(groups = "live")
    public void setupClient()
    {
        setupCredentials();
        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", endpoint);
        context = new AbiquoContextFactory().createContext(identity, credential, props);
        abiquoService = context.getAbiquoService();
    }

    public void testListDatacenters() throws Exception
    {
        Iterable<DatacenterDto> datacenters = abiquoService.listDatacenters();
        assertNotNull(datacenters);
    }

    public void testListDatacentersByName() throws Exception
    {
        Iterable<DatacenterDto> datacenters =
            abiquoService.listDatacenters(DatacenterPredicates.containsName("Datacenter"));
        assertNotNull(datacenters);
    }

    public void testGetDatacenter() throws Exception
    {
        DatacenterDto datacenter = abiquoService.getDatacenter(2);
        assertNotNull(datacenter);
    }

    public void testGetUnexistingDatacenter() throws Exception
    {
        DatacenterDto datacenter = abiquoService.getDatacenter(100);
        assertNull(datacenter);
    }

}
