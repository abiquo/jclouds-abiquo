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

package org.jclouds.abiquo.utils;

import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Utility class to build domain objects used in tests.
 * 
 * @author Ignasi Barrera
 */
public class DomainUtils
{
    public static final String XML_HEADER =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    public static String withHeader(final String xml)
    {
        return XML_HEADER + xml;
    }

    /**
     * Builds {@link DatacenterDto} domain obejcts for testing.
     */
    public static class Datacenter
    {
        public static DatacenterDto object()
        {
            DatacenterDto datacenter = new DatacenterDto();
            datacenter.setName("Datacenter");
            datacenter.setLocation("Honolulu");
            return datacenter;
        }

        public static String payload()
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<datacenter>");
            buffer.append("<location>Honolulu</location>");
            buffer.append("<name>Datacenter</name>");
            buffer.append("</datacenter>");
            return buffer.toString();
        }

    }
}
