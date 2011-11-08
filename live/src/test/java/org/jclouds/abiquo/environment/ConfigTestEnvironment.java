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

import java.util.UUID;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.config.License;
import org.jclouds.abiquo.features.AdministrationService;

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
    private void createLicense()
    {
        license = License.builder(context).code(readLicense()).build();

        license.add();
        assertNotNull(license.getId());
    }

    // Tear down
    private void deleteLicense()
    {
        license.remove();
    }

    // Utility methods

    private static String randomName()
    {
        return PREFIX + UUID.randomUUID().toString().substring(0, 12);
    }

    public static String readLicense()
    {
        return "B9cG06GaLHhUlpD9AWxKVkZPd4qPB0OAbm2Blr4374Y6rtPhcukg4MMLNK0uWn5fnsoBSqVX8o0hwQ1I6D3zUbFBSibMaK5xIZQfZmReHf04HPPBg0ZyaPRTBoKy6dCLnWpQIKe8vLemAudZ0w4spdzYMH2jw2TImN+2vd4QDU1qmUItYMsV5Sz+e8YVEGbUVkjRjQCmIUJskVxC+sW47dokgl5Qo8hN+4I6vKgEnXFdOSRFW2cyGgpHVH4Js4hwLG+PS2LXPS4UwvISJXRF6tO7Rgg9iaObcBD/byH5jGmggtSECUtXqI70nesIbMXRHQ1aGHARqbHH3+0Znjcu5g==";
    }
}
