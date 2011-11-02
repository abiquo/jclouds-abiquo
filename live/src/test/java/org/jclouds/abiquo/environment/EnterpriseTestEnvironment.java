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

package org.jclouds.abiquo.environment;

import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.UUID;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.features.EnterpriseClient;

/**
 * Test environment for enterprise live tests.
 * 
 * @author Ignasi Barrera
 */
public class EnterpriseTestEnvironment implements TestEnvironment
{
    /** The rest context. */
    private AbiquoContext context;

    // Environment data made public so tests can use them easily
    public EnterpriseClient client;

    public Enterprise enterprise;

    public EnterpriseTestEnvironment(final AbiquoContext context)
    {
        super();
        this.context = context;
        this.client = context.getApi().getEnterpriseClient();
    }

    @Override
    public void setup() throws Exception
    {
        createEnterprise();
    }

    @Override
    public void tearDown() throws Exception
    {
        deleteEnterprise();
    }

    // Setup

    private void createEnterprise()
    {
        enterprise = Enterprise.builder(context).name(randomName()).build();
        enterprise.save();
        assertNotNull(enterprise.getId());
    }

    // Tear down

    private void deleteEnterprise()
    {
        if (enterprise != null)
        {
            Integer idEnterprise = enterprise.getId();
            enterprise.delete();
            assertNull(client.getEnterprise(idEnterprise));
        }
    }

    // Utility methods

    private static String randomName()
    {
        return PREFIX + UUID.randomUUID().toString().substring(0, 12);
    }

}
