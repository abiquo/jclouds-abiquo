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

import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.enterprise.EnterpriseDto;

/**
 * Enterprise domain utilities.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class EnterpriseResources
{
    public static EnterpriseDto enterprisePost()
    {
        EnterpriseDto enterprise = new EnterpriseDto();
        enterprise.setName("Kalakaua");
        return enterprise;
    }

    public static EnterpriseDto enterprisePut()
    {
        EnterpriseDto enterprise = enterprisePost();
        enterprise.setId(1);
        enterprise.addLink(new RESTLink("edit", "http://localhost/api/admin/enterprises/1"));

        return enterprise;
    }

    public static String enterprisePostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<enterprise>");
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
        buffer.append("<isReservationRestricted>false</isReservationRestricted>");
        buffer.append("<name>Kalakaua</name>");
        buffer.append("<repositoryHard>0</repositoryHard>");
        buffer.append("<repositorySoft>0</repositorySoft>");
        buffer.append("</enterprise>");
        return buffer.toString();
    }

    public static String enterprisePutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<enterprise>");
        buffer.append(link("/admin/enterprises/1", "edit"));
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
        buffer.append("<id>1</id>");
        buffer.append("<isReservationRestricted>false</isReservationRestricted>");
        buffer.append("<name>Kalakaua</name>");
        buffer.append("<repositoryHard>0</repositoryHard>");
        buffer.append("<repositorySoft>0</repositorySoft>");
        buffer.append("</enterprise>");
        return buffer.toString();
    }

    private static String link(final String href, final String rel)
    {
        return "<link href=\"http://localhost/api" + href + "\" rel=\"" + rel + "\"/>";
    }
}
