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

import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.jclouds.abiquo.features.BaseAbiquoAsyncClientTest;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.TypeLiteral;

/**
 * Tests asynchronous and synchronous API delegates.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class AbiquoDelegateClientTest extends BaseAbiquoAsyncClientTest<AbiquoAsyncClient>
{
    private AbiquoAsyncClient asyncClient;

    private AbiquoClient syncClient;

    @BeforeClass
    @Override
    protected void setupFactory() throws IOException
    {
        super.setupFactory();
        asyncClient = injector.getInstance(AbiquoAsyncClient.class);
        syncClient = injector.getInstance(AbiquoClient.class);
    }

    public void testSync() throws SecurityException, NoSuchMethodException, InterruptedException,
        ExecutionException
    {
        assertNotNull(syncClient.getAdminClient());
        assertNotNull(syncClient.getConfigClient());
        assertNotNull(syncClient.getInfrastructureClient());
        assertNotNull(syncClient.getEnterpriseClient());
    }

    public void testAsync() throws SecurityException, NoSuchMethodException, InterruptedException,
        ExecutionException
    {
        assertNotNull(asyncClient.getAdminClient());
        assertNotNull(asyncClient.getConfigClient());
        assertNotNull(asyncClient.getInfrastructureClient());
        assertNotNull(asyncClient.getEnterpriseClient());
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<AbiquoAsyncClient>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<AbiquoAsyncClient>>()
        {
        };
    }

    @Override
    protected void checkFilters(final HttpRequest request)
    {

    }
}
