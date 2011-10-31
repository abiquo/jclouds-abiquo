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

package org.jclouds.abiquo.strategy;

import java.io.IOException;
import java.util.Properties;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.abiquo.util.Config;
import org.jclouds.lifecycle.Closer;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.rest.RestContextFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Base class for strategy live tests.
 * 
 * @author Ignasi Barrera
 */
public abstract class BaseAbiquoStrategyLiveTest
{
    protected Injector injector;

    @BeforeTest(groups = "live")
    public void setupClient() throws IOException
    {
        String identity = Config.get("abiquo.api.user");
        String credential = Config.get("abiquo.api.pass");
        String endpoint = Config.get("abiquo.api.endpoint");

        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", endpoint);

        injector =
            new RestContextFactory().<AbiquoClient, AbiquoAsyncClient> createContextBuilder(
                AbiquoContextFactory.PROVIDER_NAME, identity, credential,
                ImmutableSet.<Module> of(new Log4JLoggingModule()), props).buildInjector();

        setupStrategy();
        setup();
    }

    protected abstract void setupStrategy();

    protected void setup()
    {
        // Override if necessary
    }

    protected void tearDown()
    {
        // Override if necessary
    }

    @AfterTest(groups = "live")
    public void teardownClient() throws IOException
    {
        tearDown();

        if (injector != null)
        {
            injector.getInstance(Closer.class).close();
        }
    }
}
