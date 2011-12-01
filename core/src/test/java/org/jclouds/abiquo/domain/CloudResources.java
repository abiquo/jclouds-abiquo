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

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;

/**
 * Cloud domain utilities.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class CloudResources
{
    public static VirtualDatacenterDto virtualDatacenterPost()
    {
        VirtualDatacenterDto virtualDatacenter = new VirtualDatacenterDto();
        virtualDatacenter.setName("VDC");
        virtualDatacenter.setHypervisorType(HypervisorType.KVM);
        virtualDatacenter.setVlan(NetworkResources.vlanPost());
        return virtualDatacenter;
    }

    public static VirtualApplianceDto virtualAppliancePost()
    {
        VirtualApplianceDto virtualAppliance = new VirtualApplianceDto();
        virtualAppliance.setName("VA");
        return virtualAppliance;
    }

    public static VirtualDatacenterDto virtualDatacenterPut()
    {
        VirtualDatacenterDto virtualDatacenter = virtualDatacenterPost();
        virtualDatacenter.setId(1);
        virtualDatacenter.addLink(new RESTLink("datacenter",
            "http://localhost/api/admin/datacenters/1"));
        virtualDatacenter.addLink(new RESTLink("enterprise",
            "http://localhost/api/admin/enterprises/1"));
        virtualDatacenter.addLink(new RESTLink("edit",
            "http://localhost/api/cloud/virtualdatacenters/1"));
        virtualDatacenter.addLink(new RESTLink("virtualappliance",
            "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances"));
        return virtualDatacenter;
    }

    public static VirtualApplianceDto virtualAppliancePut()
    {
        VirtualApplianceDto virtualAppliance = virtualAppliancePost();
        virtualAppliance.setId(1);
        virtualAppliance.addLink(new RESTLink("virtualdatacenter",
            "http://localhost/api/cloud/virtualdatacenters/1"));
        virtualAppliance.addLink(new RESTLink("edit",
            "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1"));
        return virtualAppliance;
    }

    public static String virtualDatacenterPostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualDatacenter>");
        buffer.append("<cpuHard>0</cpuHard>");
        buffer.append("<cpuSoft>0</cpuSoft>");
        buffer.append("<hdHard>0</hdHard>");
        buffer.append("<hdSoft>0</hdSoft>");
        buffer.append("<publicIpsHard>0</publicIpsHard>");
        buffer.append("<publicIpsSoft>0</publicIpsSoft>");
        buffer.append("<ramHard>0</ramHard>");
        buffer.append("<ramSoft>0</ramSoft>");
        buffer.append("<storageHard>0</storageHard>");
        buffer.append("<storageSoft>0</storageSoft>");
        buffer.append("<vlansHard>0</vlansHard>");
        buffer.append("<vlansSoft>0</vlansSoft>");
        buffer.append("<hypervisorType>KVM</hypervisorType>");
        buffer.append("<name>VDC</name>");
        buffer.append(NetworkResources.VLANNetworkPostPayload());
        buffer.append("</virtualDatacenter>");
        return buffer.toString();
    }

    public static String virtualAppliancePostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualAppliance>");
        buffer.append("<error>0</error>");
        buffer.append("<highDisponibility>0</highDisponibility>");
        buffer.append("<name>VA</name>");
        buffer.append("<publicApp>0</publicApp>");
        buffer.append("</virtualAppliance>");
        return buffer.toString();
    }

    public static String virtualDatacenterPutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualDatacenter>");
        buffer.append(link("/admin/datacenters/1", "datacenter"));
        buffer.append(link("/admin/enterprises/1", "enterprise"));
        buffer.append(link("/cloud/virtualdatacenters/1", "edit"));
        buffer.append(link("/cloud/virtualdatacenters/1/virtualappliances", "virtualappliance"));
        buffer.append("<cpuHard>0</cpuHard>");
        buffer.append("<cpuSoft>0</cpuSoft>");
        buffer.append("<hdHard>0</hdHard>");
        buffer.append("<hdSoft>0</hdSoft>");
        buffer.append("<publicIpsHard>0</publicIpsHard>");
        buffer.append("<publicIpsSoft>0</publicIpsSoft>");
        buffer.append("<ramHard>0</ramHard>");
        buffer.append("<ramSoft>0</ramSoft>");
        buffer.append("<storageHard>0</storageHard>");
        buffer.append("<storageSoft>0</storageSoft>");
        buffer.append("<vlansHard>0</vlansHard>");
        buffer.append("<vlansSoft>0</vlansSoft>");
        buffer.append("<hypervisorType>KVM</hypervisorType>");
        buffer.append("<id>1</id>");
        buffer.append("<name>VDC</name>");
        buffer.append(NetworkResources.VLANNetworkPostPayload());
        buffer.append("</virtualDatacenter>");
        return buffer.toString();
    }

    public static String virtualAppliancePutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualAppliance>");
        buffer.append(link("/cloud/virtualdatacenters/1", "virtualdatacenter"));
        buffer.append(link("/cloud/virtualdatacenters/1/virtualappliances/1", "edit"));
        buffer.append("<error>0</error>");
        buffer.append("<highDisponibility>0</highDisponibility>");
        buffer.append("<id>1</id>");
        buffer.append("<name>VA</name>");
        buffer.append("<publicApp>0</publicApp>");
        buffer.append("</virtualAppliance>");
        return buffer.toString();
    }
}
