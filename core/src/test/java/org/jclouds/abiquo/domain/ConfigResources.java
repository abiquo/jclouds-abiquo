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
import com.abiquo.server.core.config.LicenseDto;

/**
 * Enterprise domain utilities.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class ConfigResources
{
    public static LicenseDto licensePost()
    {
        LicenseDto license = new LicenseDto();
        license.setCode(readLicense());
        license.setCustomerid("3bca6d1d-5fe2-42c5-82ea-a5276ea8c71c");
        return license;
    }

    public static LicenseDto licensePut()
    {
        LicenseDto license = licensePost();
        license.setId(1);
        license.addLink(new RESTLink("edit", "http://localhost/api/config/licenses/1"));

        return license;
    }

    public static String licensePutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<license>");
        buffer.append(link("/admin/enterprises/config/licenses/1", "edit"));
        buffer.append("<customerid>3bca6d1d-5fe2-42c5-82ea-a5276ea8c71c</customerid>");
        buffer.append("<code>" + readLicense() + "</code>");
        buffer.append("<id>1</id>");
        buffer.append("</license>");
        return buffer.toString();
    }

    public static String LicensePostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<license>");
        buffer.append("<customerid>3bca6d1d-5fe2-42c5-82ea-a5276ea8c71c</customerid>");
        buffer.append("<code>" + readLicense() + "</code>");
        buffer.append("</license>");
        return buffer.toString();
    }

    private static String link(final String href, final String rel)
    {
        return "<link href=\"http://localhost/api" + href + "\" rel=\"" + rel + "\"/>";
    }

    private static String readLicense()
    {
        return "B9cG06GaLHhUlpD9AWxKVkZPd4qPB0OAbm2Blr4374Y6rtPhcukg4MMLNK0uWn5fnsoBSqVX8o0hwQ1I6D3zUbFBSibMaK5xIZQfZmReHf04HPPBg0ZyaPRTBoKy6dCLnWpQIKe8vLemAudZ0w4spdzYMH2jw2TImN+2vd4QDU1qmUItYMsV5Sz+e8YVEGbUVkjRjQCmIUJskVxC+sW47dokgl5Qo8hN+4I6vKgEnXFdOSRFW2cyGgpHVH4Js4hwLG+PS2LXPS4UwvISJXRF6tO7Rgg9iaObcBD/byH5jGmggtSECUtXqI70nesIbMXRHQ1aGHARqbHH3+0Znjcu5g==";
    }
}
