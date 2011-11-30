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

import com.abiquo.server.core.infrastructure.network.VLANNetworkDto;

/**
 * Network domain utilities.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class NetworkResources
{
    public static VLANNetworkDto vlanPost()
    {
        VLANNetworkDto vlan = new VLANNetworkDto();
        vlan.setAddress("192.168.1.0");
        vlan.setDefaultNetwork(true);
        vlan.setName("DefaultNetwork");
        vlan.setGateway("192.168.1.1");
        vlan.setMask(24);

        return vlan;
    }

    public static String VLANNetworkPostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<network>");
        buffer.append("<address>192.168.1.0</address>");
        buffer.append("<defaultNetwork>true</defaultNetwork>");
        buffer.append("<gateway>192.168.1.1</gateway>");
        buffer.append("<mask>24</mask>");
        buffer.append("<name>DefaultNetwork</name>");
        buffer.append("</network>");
        return buffer.toString();
    }
}
