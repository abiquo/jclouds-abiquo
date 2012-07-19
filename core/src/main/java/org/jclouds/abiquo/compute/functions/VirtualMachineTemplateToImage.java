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

package org.jclouds.abiquo.compute.functions;

import java.net.URI;

import javax.inject.Singleton;

import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.Image.Status;
import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.domain.LoginCredentials;

import com.abiquo.model.enumerator.OSType;
import com.abiquo.model.rest.RESTLink;
import com.google.common.base.Function;

/**
 * Transforms a {@link VirtualMachineTemplate} into an {@link Image}.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class VirtualMachineTemplateToImage implements Function<VirtualMachineTemplate, Image>
{

    @Override
    public Image apply(final VirtualMachineTemplate template)
    {
        ImageBuilder builder = new ImageBuilder();
        builder.ids(template.getId().toString());
        builder.name(template.getName());
        builder.description(template.getDescription());

        // Only conversions have a status
        builder.status(Status.AVAILABLE);
        builder.backendStatus(Status.AVAILABLE.name()); // Abiquo images do not have a status

        RESTLink downloadLink = template.unwrap().searchLink("diskfile");
        builder.uri(downloadLink == null ? null : URI.create(downloadLink.getHref()));

        builder.operatingSystem(toOperatingSystem(template.getOsType(), template.getOsVersion()));
        builder.defaultCredentials(toLoginCredentials(template.getLoginUser(), template
            .getLoginPassword(), template.getOsType()));

        return builder.build();
    }

    private OperatingSystem toOperatingSystem(OSType ostype, final String osVersion)
    {
        org.jclouds.cim.OSType jcloudsOstype;

        try
        {
            jcloudsOstype = org.jclouds.cim.OSType.valueOf(ostype.name());
        }
        catch (Throwable e)
        {
            jcloudsOstype = org.jclouds.cim.OSType.UNRECOGNIZED;
        }

        return OperatingSystem.builder().family(jcloudsOstype.getFamily()).name(
            jcloudsOstype.name()).is64Bit(jcloudsOstype.is64Bit()).description(
            jcloudsOstype.getValue()).version(osVersion).build();
    }

    private LoginCredentials toLoginCredentials(final String loginUser, final String loginPassword,
        final OSType ostype)
    {
        if (loginUser == null || loginPassword == null)
        {
            return null;
        }
        else
        {
            boolean useSudo = (OSType.UBUNTU == ostype || OSType.UBUNTU_64 == ostype);
            return LoginCredentials.builder().identity(loginUser).password(loginPassword)
                .authenticateSudo(useSudo).build();
        }
    }
}
