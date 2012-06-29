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

package org.jclouds.abiquo.domain.network;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jclouds.abiquo.domain.network.options.IpOptions;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.network.IpsPoolManagementDto;

/**
 * Live integration tests for the {@link ExternalNetwork} domain class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class ExternalNetworkLiveTest extends BaseAbiquoClientLiveTest
{
    public void testListIps()
    {
        IpsPoolManagementDto ipsDto =
            context.getApiContext().getApi().getInfrastructureClient()
                .listNetworkIps(env.publicNetwork.unwrap(), IpOptions.builder().limit(1).build());
        int totalIps = ipsDto.getTotalSize();

        List<Ip> ips = env.publicNetwork.listIps();

        assertEquals(ips.size(), totalIps);
    }
}
