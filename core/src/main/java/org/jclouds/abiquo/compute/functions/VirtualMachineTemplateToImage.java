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
import org.jclouds.compute.domain.ImageBuilder;

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
        builder.uri(URI.create(template.getPath())); // TODO: Should be the public download URI
        builder.operatingSystem(null); // TODO: Operating system not implemented in Abiquo Templates
        // TODO: image credentials
        return builder.build();
    }

}
