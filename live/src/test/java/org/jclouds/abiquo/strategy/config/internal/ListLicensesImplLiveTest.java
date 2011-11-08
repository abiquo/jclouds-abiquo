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

package org.jclouds.abiquo.strategy.config.internal;

import static com.google.common.collect.Iterables.size;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.ConfigResources;
import org.jclouds.abiquo.domain.config.License;
import org.jclouds.abiquo.predicates.configuration.LicensePredicates;
import org.jclouds.abiquo.strategy.BaseAbiquoStrategyLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.config.LicenseDto;

/**
 * Live tests for the {@link ListLicenseImpl} strategy.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class ListLicensesImplLiveTest extends BaseAbiquoStrategyLiveTest
{
    private ListLicensesImpl strategy;

    private AbiquoClient client;

    private LicenseDto license;

    @Override
    protected void setupStrategy()
    {
        this.strategy = injector.getInstance(ListLicensesImpl.class);
        this.client = injector.getInstance(AbiquoClient.class);
    }

    @Override
    protected void setup()
    {
        LicenseDto license = ConfigResources.licensePost();
        this.license = client.getConfigClient().addLicense(license);
    }

    @Override
    protected void tearDown()
    {
        client.getConfigClient().removeLicense(license);
    }

    public void testExecute()
    {
        Iterable<License> licenses = strategy.execute();
        assertNotNull(licenses);
        assertTrue(size(licenses) > 0);
    }

    public void testExecutePredicateWithoutResults()
    {
        Iterable<License> licenses = strategy.execute(LicensePredicates.customerId("FAIL"));
        assertNotNull(licenses);
        assertEquals(size(licenses), 0);
    }

    public void testExecutePredicateWithResults()
    {
        Iterable<License> licenses =
            strategy.execute(LicensePredicates.customerId(license.getCustomerid()));
        assertNotNull(licenses);
        assertEquals(size(licenses), 1);
    }
}
