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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.annotations.EnterpriseEdition;

import com.abiquo.model.enumerator.NetworkType;
import com.abiquo.server.core.infrastructure.network.IpsPoolManagementDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworkDto;

/**
 * Adds high level functionality to public {@link VLANNetworkDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see API: <a href="http://community.abiquo.com/display/ABI20/Public+Network+Resource">
 *      http://community.abiquo.com/display/ABI20/Public+Network+Resource</a>
 */
@EnterpriseEdition
public class PublicNetwork extends Network<PublicIPAddress>
{
    /** The datacenter where the network belongs. */
    // Package protected to allow navigation from children
    Datacenter datacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected PublicNetwork(final AbiquoContext context, final VLANNetworkDto target)
    {
        super(context, target);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-DeleteaPublicNetwork">
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-DeleteaPublicNetwork</a>
     */
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteNetwork(target);
        target = null;
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-CreateanewPublicNetwork">
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-CreateanewPublicNetwork</a>
     */
    public void save()
    {
        target =
            context.getApi().getInfrastructureClient().createNetwork(datacenter.unwrap(), target);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-UpdateaPublicNetwork">
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-UpdateaPublicNetwork</a>
     */
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateNetwork(target);
    }

    /**
     * @see API: <a
     *      href="http://community.abiquo.com/display/ABI20/Public+IPs+Resource#PublicIPsResource-ReturnthelistofIPsforaPublicNetwork">
     *      http://community.abiquo.com/display/ABI20/Public+IPs+Resource#PublicIPsResource-ReturnthelistofIPsforaPublicNetwork</a>
     */
    @Override
    public List<PublicIPAddress> listIps()
    {
        IpsPoolManagementDto nics =
            context.getApi().getInfrastructureClient().listNetworkIps(target);
        return wrap(context, PublicIPAddress.class, nics.getCollection());
    }

    // Builder

    public static Builder builder(final AbiquoContext context, final Datacenter datacenter)
    {
        return new Builder(context, datacenter);
    }

    public static class Builder extends NetworkBuilder<Builder>
    {
        private Datacenter datacenter;

        public Builder(final AbiquoContext context, final Datacenter datacenter)
        {
            super(context);
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Datacenter.class);
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Enterprise.class);
            this.datacenter = datacenter;
            this.context = context;
        }

        public Builder datacenter(final Datacenter datacenter)
        {
            this.datacenter = datacenter;
            return this;
        }

        public PublicNetwork build()
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
            dto.setUnmanaged(false);
            dto.setType(NetworkType.PUBLIC);

            PublicNetwork network = new PublicNetwork(context, dto);
            network.datacenter = datacenter;

            return network;
        }

        public static Builder fromPublicNetwork(final PublicNetwork in)
        {
            return PublicNetwork.builder(in.context, in.datacenter).name(in.getName())
                .tag(in.getTag()).gateway(in.getGateway()).address(in.getAddress())
                .mask(in.getMask()).primaryDNS(in.getPrimaryDNS())
                .secondaryDNS(in.getSecondaryDNS()).sufixDNS(in.getSufixDNS())
                .defaultNetwork(in.getDefaultNetwork());
        }
    }

    @Override
    public String toString()
    {
        return "Public " + super.toString();
    }
}
