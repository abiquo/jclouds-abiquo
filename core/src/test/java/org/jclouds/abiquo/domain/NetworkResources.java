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

import static org.jclouds.abiquo.domain.DomainUtils.link;

import com.abiquo.model.rest.RESTLink;
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

    public static VLANNetworkDto privateNetworkPut()
    {
        VLANNetworkDto vlan = new VLANNetworkDto();
        vlan.setAddress("192.168.1.0");
        vlan.setDefaultNetwork(true);
        vlan.setName("DefaultNetwork");
        vlan.setGateway("192.168.1.1");
        vlan.setMask(24);
        vlan.addLink(new RESTLink("edit",
            "http://localhost/api/cloud/virtualdatacenters/1/privatenetworks/1"));
        vlan.addLink(new RESTLink("ips",
            "http://localhost/api/cloud/virtualdatacenters/1/privatenetworks/1/ips"));

        return vlan;
    }

    public static VLANNetworkDto publicNetworkPut()
    {
        VLANNetworkDto vlan = new VLANNetworkDto();
        vlan.setAddress("192.168.1.0");
        vlan.setDefaultNetwork(true);
        vlan.setName("PublicNetwork");
        vlan.setGateway("192.168.1.1");
        vlan.setMask(24);
        vlan.addLink(new RESTLink("edit", "http://localhost/api/admin/datacenters/1/network/1"));

        return vlan;
    }

    public static String vlanNetworkPostPayload()
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

    public static String privateNetworkPutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<network>");
        buffer.append(link("/cloud/virtualdatacenters/1/privatenetworks/1", "edit"));
        buffer.append(link("/cloud/virtualdatacenters/1/privatenetworks/1/ips", "ips"));
        buffer.append("<address>192.168.1.0</address>");
        buffer.append("<defaultNetwork>true</defaultNetwork>");
        buffer.append("<gateway>192.168.1.1</gateway>");
        buffer.append("<mask>24</mask>");
        buffer.append("<name>DefaultNetwork</name>");
        buffer.append("</network>");
        return buffer.toString();
    }

    public static String publicNetworkPutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<network>");
        buffer.append(link("/admin/datacenters/1/network/1", "edit"));
        buffer.append("<address>192.168.1.0</address>");
        buffer.append("<defaultNetwork>true</defaultNetwork>");
        buffer.append("<gateway>192.168.1.1</gateway>");
        buffer.append("<mask>24</mask>");
        buffer.append("<name>PublicNetwork</name>");
        buffer.append("</network>");
        return buffer.toString();
    }

    public static String linksDtoPayload(final RESTLink link)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<links>");
        buffer.append(link(link));
        buffer.append("</links>");
        return buffer.toString();
    }
}
