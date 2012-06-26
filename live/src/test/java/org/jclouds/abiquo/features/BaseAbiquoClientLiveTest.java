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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Properties;

import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.abiquo.AbiquoApiMetadata;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Base class for live and domain tests.
 * 
 * @author Ignasi Barrera
 */
public abstract class BaseAbiquoClientLiveTest
{
    /** The rest context. */
    protected static AbiquoContext context;

    /** The test environment. */
    protected static CloudTestEnvironment env;

    @BeforeSuite(groups = "live")
    protected static void setupClient() throws Exception
    {
        String identity =
            checkNotNull(System.getProperty("test.abiquo.identity"), "test.abiquo.identity");
        String credential =
            checkNotNull(System.getProperty("test.abiquo.credential"), "test.abiquo.credential");
        String endpoint =
            checkNotNull(System.getProperty("test.abiquo.endpoint"), "test.abiquo.endpoint");

        Properties props = new Properties();
        props.put(Constants.PROPERTY_MAX_RETRIES, "0");
        props.put(Constants.PROPERTY_MAX_REDIRECTS, "0");
        // Wait at most one minute in Machine discovery
        props.put("jclouds.timeouts.InfrastructureClient.discoverSingleMachine", "60000");
        props.put("jclouds.timeouts.InfrastructureClient.discoverMultipleMachines", "60000");
        props.put("jclouds.timeouts.InfrastructureClient.createMachine", "60000");
        props.put("jclouds.timeouts.InfrastructureClient.updateMachine", "60000");
        props.put("jclouds.timeouts.InfrastructureClient.checkMachineState", "60000");
        props.put("jclouds.timeouts.CloudClient.listVirtualMachines", "60000");

        context = ContextBuilder.newBuilder(new AbiquoApiMetadata()) //
            .endpoint(endpoint) //
            .credentials(identity, credential) //
            .modules(ImmutableSet.<Module> of(new SLF4JLoggingModule())) //
            .overrides(props) //
            .build(AbiquoContext.class);

        env = new CloudTestEnvironment(context);
        env.setup();
    }

    @BeforeSuite(groups = "ucs", dependsOnMethods = "setupClient")
    protected static void setupUcs() throws Exception
    {
        env.createUcsRack();
    }

    @AfterSuite(groups = "live")
    public static void teardownClient(final ITestContext testContext) throws Exception
    {
        env.tearDown();

        if (context != null)
        {
            // Wait a bit before closing context, to avoid executor shutdown while
            // there are still open threads
            Thread.sleep(1000L);
            context.close();
        }
    }

}
