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

import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Infrastructure domain utilities.
 * 
 * @author Ignasi Barrera
 */
public class Infrastructure
{
    public static DatacenterDto datacenterPost()
    {
        DatacenterDto datacenter = new DatacenterDto();
        datacenter.setName("DC");
        datacenter.setLocation("Honolulu");
        return datacenter;
    }

    public static DatacenterDto datacenterPut()
    {
        DatacenterDto datacenter = datacenterPost();
        datacenter.setId(1);
        return datacenter;
    }

    public static String datacenterPostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<datacenter>");
        buffer.append("<location>Honolulu</location>");
        buffer.append("<name>DC</name>");
        buffer.append("</datacenter>");
        return buffer.toString();
    }

    public static String datacenterPutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<datacenter>");
        buffer.append("<id>1</id>");
        buffer.append("<location>Honolulu</location>");
        buffer.append("<name>DC</name>");
        buffer.append("</datacenter>");
        return buffer.toString();
    }
}
