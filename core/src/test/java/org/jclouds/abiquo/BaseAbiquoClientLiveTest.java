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

import java.util.Properties;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;

/**
 * Tests behavior of {@code AbiquoClient}
 * 
 * @author Ignasi Barrera
 */
public abstract class BaseAbiquoClientLiveTest
{
    protected AbiquoContext context;

    protected AbiquoClient client;

    protected String provider = AbiquoContextFactory.PROVIDER_NAME;

    protected String identity;

    protected String credential;

    protected String endpoint;

    protected String apiVersion;

    protected abstract void setupEntities() throws Exception;

    protected abstract void teardownEntities() throws Exception;

    @BeforeGroups(groups = "live")
    protected void setupClient() throws Exception
    {
        identity = checkNotNull(System.getProperty("test.abiquo.identity"), "test.abiquo.identity");
        credential =
            checkNotNull(System.getProperty("test.abiquo.credential"), "test.abiquo.credential");
        endpoint = checkNotNull(System.getProperty("test.abiquo.endpoint"), "test.abiquo.endpoint");
        apiVersion =
            checkNotNull(System.getProperty("test.abiquo.apiversion"), "test.abiquo.apiversion");

        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", endpoint);
        context = new AbiquoContextFactory().createContext(identity, credential, props);
        client = context.getApi();

        setupEntities();
    }

    @AfterGroups(groups = "live")
    public void teardownClient() throws Exception
    {
        teardownEntities();

        if (context != null)
        {
            context.close();
        }
    }

}
