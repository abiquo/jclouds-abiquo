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

import java.util.Properties;

import org.jclouds.rest.RestContext;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

/**
 * Tests behavior of {@code AbiquoClient}
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live", testName = "abiquo.AbiquoClientLiveTest")
public class AbiquoClientLiveTest
{

    protected RestContext<AbiquoClient, AbiquoAsyncClient> context;

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

    @BeforeGroups(groups = {"live"})
    public void setupClient()
    {
        setupCredentials();
        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", endpoint);
        context = new AbiquoContextFactory().createContext(identity, credential, props);
    }

    @Test
    public void testList() throws Exception
    {
        String response = context.getApi().list();
        assertNotNull(response);
    }

    @Test
    public void testGet() throws Exception
    {
        String response = context.getApi().get(1l);
        assertNotNull(response);
    }

    /*
     * TODO: add tests for Abiquo interface methods
     */
}
