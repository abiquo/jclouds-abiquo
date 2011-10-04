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

package org.jclouds.abiquo.domain.factory;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.testng.Assert.assertNotNull;

import java.util.Properties;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Unit tests for the {@link DomainFactory} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class DomainFactoryTest
{
    /** The domain factory to test. */
    private DomainFactory factory;

    @BeforeGroups(groups = "unit")
    public void setupFactory()
    {
        String identity =
            checkNotNull(System.getProperty("test.abiquo.identity"), "test.abiquo.identity");
        String credential =
            checkNotNull(System.getProperty("test.abiquo.credential"), "test.abiquo.credential");
        String endpoint =
            checkNotNull(System.getProperty("test.abiquo.endpoint"), "test.abiquo.endpoint");

        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", endpoint);
        AbiquoContext context =
            new AbiquoContextFactory().createContext(identity, credential, props);

        factory = context.getDomainFactory();
        assertNotNull(factory.getContext());
    }

    public void testCreate()
    {
        Datacenter datacenter = factory.create(Datacenter.class);
        assertNotNull(datacenter);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateInvalidObject()
    {
        factory.create(DatacenterDto.class);
    }
}
