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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.transform;

import java.net.URI;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.network.Nic;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;

import com.google.common.base.Function;

/**
 * Links a {@link VirtualMachine} object to a {@link NodeMetadata} one.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class VirtualMachineToNodeMetadata implements Function<VirtualMachine, NodeMetadata>
{
    private final VirtualMachineTemplateToImage virtualMachineTemplateToImage;

    private final VirtualMachineTemplateToHardware virtualMachineTemplateToHardware;

    private final VirtualMachineStateToNodeState virtualMachineStateToNodeState;

    @Inject
    public VirtualMachineToNodeMetadata(
        final VirtualMachineTemplateToImage virtualMachineTemplateToImage,
        final VirtualMachineTemplateToHardware virtualMachineTemplateToHardware,
        final VirtualMachineStateToNodeState virtualMachineStateToNodeState)
    {
        this.virtualMachineTemplateToImage =
            checkNotNull(virtualMachineTemplateToImage, "virtualMachineTemplateToImage");
        this.virtualMachineTemplateToHardware =
            checkNotNull(virtualMachineTemplateToHardware, "virtualMachineTemplateToHardware");
        this.virtualMachineStateToNodeState =
            checkNotNull(virtualMachineStateToNodeState, "virtualMachineStateToNodeState");
    }

    @Override
    public NodeMetadata apply(final VirtualMachine vm)
    {
        NodeMetadataBuilder builder = new NodeMetadataBuilder();
        builder.ids(vm.getId().toString());
        builder.uri(URI.create(vm.unwrap().getEditLink().getHref()));
        builder.name(vm.getName());
        builder.hostname(vm.getName()); // TODO: Abiquo does not set the hostname

        // TODO: builder.location()
        // TODO: builder.group()
        // TODO: builder.credentials()

        VirtualMachineTemplate template = vm.getTemplate();

        Image image = virtualMachineTemplateToImage.apply(template);
        builder.imageId(image.getId().toString());
        builder.operatingSystem(image.getOperatingSystem());

        builder.hardware(virtualMachineTemplateToHardware.apply(template));
        builder.loginPort(vm.getVncPort()); // TODO: Is VNC or SSH port ?

        // TODO: Add a method to NIC domain object to determine its type
        builder.privateAddresses(privateIps(vm.listAttachedNics()));

        builder.state(virtualMachineStateToNodeState.apply(vm.getState()));

        return builder.build();
    }

    private static Iterable<String> privateIps(final Iterable<Nic> nics)
    {
        return transform(nics, new Function<Nic, String>()
        {
            @Override
            public String apply(final Nic nic)
            {
                return nic.getIp();
            }
        });
    }
}
