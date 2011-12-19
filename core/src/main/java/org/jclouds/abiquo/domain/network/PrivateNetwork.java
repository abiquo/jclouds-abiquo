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

package org.jclouds.abiquo.domain.network;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;

import com.abiquo.server.core.infrastructure.network.VLANNetworkDto;

/**
 * Adds high level functionality to private {@link VLANNetworkDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Private+Network+Resource">
 *      http://community.abiquo.com/display/ABI20/Private+Network+Resource</a>
 */
public class PrivateNetwork extends Network
{
    /** The virtual datacenter where the network belongs. */
    // Package protected to allow navigation from children
    VirtualDatacenter virtualDatacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected PrivateNetwork(final AbiquoContext context, final VLANNetworkDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Private+Network+Resource#PrivateNetworkResource-DeleteaPrivateNetwork">
     *      http://community.abiquo.com/display/ABI20/Private+Network+Resource#PrivateNetworkResource-DeleteaPrivateNetwork</a>
     */
    public void delete()
    {
        context.getApi().getCloudClient().deletePrivateNetwork(target);
        target = null;
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Private+Network+Resource#PrivateNetworkResource-CreateaPrivateNetwork">
     *      http://community.abiquo.com/display/ABI20/Private+Network+Resource#PrivateNetworkResource-CreateaPrivateNetwork</a>
     */
    public void save()
    {
        target =
            context.getApi().getCloudClient().createPrivateNetwork(virtualDatacenter.unwrap(),
                target);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Private+Network+Resource#PrivateNetworkResource-UpdateaPrivateNetwork">
     *      http://community.abiquo.com/display/ABI20/Private+Network+Resource#PrivateNetworkResource-UpdateaPrivateNetwork</a>
     */
    public void update()
    {
        target = context.getApi().getCloudClient().updatePrivateNetwork(target);
    }

    // Builder

    public static Builder builder(final AbiquoContext context,
        final VirtualDatacenter virtualDatacenter)
    {
        return new Builder(context, virtualDatacenter);
    }

    public static class Builder extends NetworkBuilder<Builder>
    {
        VirtualDatacenter virtualDatacenter;

        public Builder(final AbiquoContext context, final VirtualDatacenter virtualDatacenter)
        {
            super(context);
            this.context = context;
            this.virtualDatacenter = virtualDatacenter;
        }

        public PrivateNetwork build()
        {
            VLANNetworkDto dto = new VLANNetworkDto();
            dto.setName(name);
            dto.setTag(tag);
            dto.setGateway(gateway);
            dto.setAddress(address);
            dto.setMask(mask);
            dto.setPrimaryDNS(primaryDNS);
            dto.setSecondaryDNS(secondaryDNS);
            dto.setSufixDNS(sufixDNS);
            dto.setDefaultNetwork(defaultNetwork);
            dto.setUnmanaged(unmanaged);
            dto.setType(type);

            PrivateNetwork network = new PrivateNetwork(context, dto);
            network.virtualDatacenter = virtualDatacenter;

            return network;
        }

        public static Builder fromPrivateNetwork(final PrivateNetwork in)
        {
            return PrivateNetwork.builder(in.context, in.virtualDatacenter).name(in.getName()).tag(
                in.getTag()).gateway(in.getGateway()).address(in.getAddress()).mask(in.getMask())
                .primaryDNS(in.getPrimaryDNS()).secondaryDNS(in.getSecondaryDNS()).sufixDNS(
                    in.getSufixDNS()).defaultNetwork(in.getDefaultNetwork()).unmanaged(
                    in.getUnmanaged()).type(in.getType());
        }
    }
}
