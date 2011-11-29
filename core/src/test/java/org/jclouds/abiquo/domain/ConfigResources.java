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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.config.LicenseDto;
import com.google.common.io.Resources;

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
        license.setCode(readLicense("license/expired"));
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
        buffer.append("<code>" + readLicense("license/expired") + "</code>");
        buffer.append("<id>1</id>");
        buffer.append("</license>");
        return buffer.toString();
    }

    public static String LicensePostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<license>");
        buffer.append("<customerid>3bca6d1d-5fe2-42c5-82ea-a5276ea8c71c</customerid>");
        buffer.append("<code>" + readLicense("license/expired") + "</code>");
        buffer.append("</license>");
        return buffer.toString();
    }

    private static String readLicense(final String filename)
    {
        URL url = ConfigResources.class.getResource("/" + filename);
        try
        {
            return Resources.toString(url, Charset.defaultCharset());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read file " + filename);
        }
    }
}
