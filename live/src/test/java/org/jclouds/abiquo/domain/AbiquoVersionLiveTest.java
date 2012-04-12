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

package org.jclouds.abiquo.domain;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.abiquo.util.Assert.assertHasError;
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.AbiquoApiMetadata;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.rest.internal.ContextBuilder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Live integration tests for the Abiquo versioning support.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class AbiquoVersionLiveTest
{
    private AbiquoContext context;

    @BeforeMethod
    public void setup()
    {
        String identity =
            checkNotNull(System.getProperty("test.abiquo.identity"), "test.abiquo.identity");
        String credential =
            checkNotNull(System.getProperty("test.abiquo.credential"), "test.abiquo.credential");
        String endpoint =
            checkNotNull(System.getProperty("test.abiquo.endpoint"), "test.abiquo.endpoint");

        context = ContextBuilder.newBuilder(new AbiquoApiMetadata()) //
            .endpoint(endpoint) //
            .credentials(identity, credential) //
            .apiVersion("0.0") //
            .modules(ImmutableSet.<Module> of(new SLF4JLoggingModule())) //
            .build();
    }

    @AfterMethod
    public void tearDown()
    {
        if (context != null)
        {
            context.close();
        }
    }

    public void testUnsupportedVersion()
    {
        try
        {
            context.getAdministrationService().getCurrentUserInfo();
            fail("Unsupported versions in mime types should not be allowed");
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.NOT_ACCEPTABLE, "406-NOT ACCEPTABLE");
        }
    }
}
