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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Properties;

import org.jclouds.abiquo.internal.AbiquoContextImpl;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.ComputeServiceContextFactory;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.providers.BaseProviderMetadataTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Unit tests for the {@link AbiquoProviderMetadata} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class AbiquoProviderTest extends BaseProviderMetadataTest
{

    public AbiquoProviderTest()
    {
        super(new AbiquoProviderMetadata(), new AbiquoApiMetadata());
    }

    public void testProviderConfiguration()
    {
        Properties props = new Properties();
        props.put("abiquo.endpoint", "http://localhost/api");
        ComputeServiceContext context =
            new ComputeServiceContextFactory().createContext("abiquo", "admin", "xabiquo",
                ImmutableSet.<Module> of(new SLF4JLoggingModule()), props);

        assertNotNull(context);
        assertNotNull(context.getComputeService());
        assertEquals(context.getClass(), AbiquoContextImpl.class);

        context.close();
    }

}
