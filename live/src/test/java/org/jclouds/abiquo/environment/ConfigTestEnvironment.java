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

import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.config.License;
import org.jclouds.abiquo.features.AdministrationService;

import com.google.common.io.Resources;

/**
 * Test environment for enterprise live tests.
 * 
 * @author Ignasi Barrera
 */
public class ConfigTestEnvironment implements TestEnvironment
{
    /** The rest context. */
    private AbiquoContext context;

    public License license;

    // Environment data made public so tests can use them easily
    public AdministrationService administrationService;

    public ConfigTestEnvironment(final AbiquoContext context)
    {
        super();
        this.administrationService = context.getAdministrationService();
        this.context = context;
    }

    @Override
    public void setup() throws Exception
    {
        createLicense();
    }

    @Override
    public void tearDown() throws Exception
    {
        deleteLicense();
    }

    // Setup
    private void createLicense() throws IOException
    {
        license = License.builder(context, readLicense()).build();

        license.add();
        assertNotNull(license.getId());
    }

    // Tear down
    private void deleteLicense()
    {
        license.remove();
    }

    // Utility methods

    public static String readLicense() throws IOException
    {
        URL url = ConfigTestEnvironment.class.getResource("/license/expired");

        return Resources.toString(url, Charset.defaultCharset());
    }
}
