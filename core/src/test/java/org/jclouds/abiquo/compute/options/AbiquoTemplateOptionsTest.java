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
package org.jclouds.abiquo.compute.options;

import static org.jclouds.abiquo.domain.DomainWrapper.wrap;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.network.Ip;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.rest.RestContext;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.network.IpPoolManagementDto;

/**
 * Unit tests for the {@link AbiquoTemplateOptions} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class AbiquoTemplateOptionsTest
{
    @Test
    public void testAs()
    {
        TemplateOptions options = new AbiquoTemplateOptions();
        assertEquals(options.as(AbiquoTemplateOptions.class), options);
    }

    @Test
    public void testOverrideCores()
    {
        TemplateOptions options = new AbiquoTemplateOptions().overrideCores(5);
        assertEquals(options.as(AbiquoTemplateOptions.class).getOverrideCores(), Integer.valueOf(5));
    }

    @Test
    public void testOverrideRam()
    {
        TemplateOptions options = new AbiquoTemplateOptions().overrideRam(2048);
        assertEquals(options.as(AbiquoTemplateOptions.class).getOverrideRam(),
            Integer.valueOf(2048));
    }

    @Test
    public void testVncPassword()
    {
        TemplateOptions options = new AbiquoTemplateOptions().vncPassword("foo");
        assertEquals(options.as(AbiquoTemplateOptions.class).getVncPassword(), "foo");
    }

    @Test
    public void testVirtualDatacenter()
    {
        TemplateOptions options = new AbiquoTemplateOptions().virtualDatacenter("foo");
        assertEquals(options.as(AbiquoTemplateOptions.class).getVirtualDatacenter(), "foo");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIps()
    {
        RestContext<AbiquoClient, AbiquoAsyncClient> context =
            EasyMock.createMock(RestContext.class);

        IpPoolManagementDto dto1 = new IpPoolManagementDto();
        dto1.setIp("10.60.0.1");
        IpPoolManagementDto dto2 = new IpPoolManagementDto();
        dto2.setIp("10.60.0.2");

        Ip ip1 = wrap(context, Ip.class, dto1);
        Ip ip2 = wrap(context, Ip.class, dto2);

        TemplateOptions options = new AbiquoTemplateOptions().ips(ip1, ip2);

        Ip[] ips = options.as(AbiquoTemplateOptions.class).getIps();
        assertNotNull(ips);
        assertEquals(ips[0].getIp(), "10.60.0.1");
        assertEquals(ips[1].getIp(), "10.60.0.2");
    }
}
