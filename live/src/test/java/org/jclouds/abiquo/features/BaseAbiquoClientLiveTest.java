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

package org.jclouds.abiquo.features;

import java.util.Properties;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.abiquo.environment.TestEnvironment;
import org.jclouds.abiquo.util.Config;
import org.jclouds.logging.config.NullLoggingModule;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Base class for live and domain tests.
 * 
 * @author Ignasi Barrera
 */
public abstract class BaseAbiquoClientLiveTest<E extends TestEnvironment>
{
    /** The rest context. */
    protected AbiquoContext context;

    /** The test environment. */
    protected E env;

    @BeforeClass(groups = "live")
    protected void setupClient() throws Exception
    {
        String identity = Config.get("abiquo.api.user");
        String credential = Config.get("abiquo.api.pass");
        String endpoint = Config.get("abiquo.api.endpoint");

        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", endpoint);

        context =
            new AbiquoContextFactory().createContext(identity, credential,
                ImmutableSet.<Module> of(new NullLoggingModule()), props);
        env = environment(context);

        env.setup();
    }

    /**
     * Get the test environment for the current tests.
     */
    protected abstract E environment(AbiquoContext context);

    @AfterClass(groups = "live")
    public void teardownClient() throws Exception
    {
        env.tearDown();

        if (context != null)
        {
            context.close();
        }
    }

}
