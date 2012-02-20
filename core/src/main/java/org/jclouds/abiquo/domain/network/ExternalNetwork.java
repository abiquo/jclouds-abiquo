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
import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.infrastructure.network.IpsPoolManagementDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworkDto;

/**
 * Adds high level functionality to external {@link VLANNetworkDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see API: <a href="http://community.abiquo.com/display/ABI20/Public+Network+Resource">
 *      http://community.abiquo.com/display/ABI20/Public+Network+Resource</a>
 */
@EnterpriseEdition
public class ExternalNetwork extends Network
{
    /** The datacenter where the network belongs. */
    private Datacenter datacenter;

    /** The enterprise where the network belongs. */
    private Enterprise enterprise;

    /**
     * Constructor to be used only by the builder.
     */
    protected ExternalNetwork(final AbiquoContext context, final VLANNetworkDto target)
    {
        super(context, target);
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-DeleteanExternalNetwork"
     *      >
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource
     *      -DeleteanExternalNetwork</a>
     */
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteNetwork(target);
        target = null;
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-CreateanewExternalNetwork"
     *      >
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource
     *      -CreateanewExternalNetwork</a>
     */
    public void save()
    {
        this.addEnterpriseLink();
        target =
            context.getApi().getInfrastructureClient().createNetwork(datacenter.unwrap(), target);
    }

    /**
     * @see API: <a href=
     *      " http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource-UpdateanExternalNetwork"
     *      >
     *      http://community.abiquo.com/display/ABI20/Public+Network+Resource#PublicNetworkResource
     *      -UpdateanExternalNetwork</a>
     */
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateNetwork(target);
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Public+IPs+Resource#PublicIPsResource-ReturnthelistofIPsforaPublicNetwork"
     *      > http://community.abiquo.com/display/ABI20/Public+IPs+Resource#PublicIPsResource-
     *      ReturnthelistofIPsforaPublicNetwork</a>
     */
    @Override
    public List<Ip> listIps()
    {
        IpsPoolManagementDto nics =
            context.getApi().getInfrastructureClient().listNetworkIps(target);
        return wrap(context, Ip.class, nics.getCollection());
    }

    private void addEnterpriseLink()
    {
        checkNotNull(enterprise, ValidationErrors.NULL_RESOURCE + Enterprise.class);
        checkNotNull(enterprise.getId(), ValidationErrors.MISSING_REQUIRED_FIELD + " id in "
            + Enterprise.class);

        RESTLink link = enterprise.unwrap().searchLink("edit");

        checkNotNull(link, ValidationErrors.MISSING_REQUIRED_LINK);

        target.addLink(new RESTLink("enterprise", link.getHref()));
    }

    // Builder

    public static Builder builder(final AbiquoContext context, final Datacenter datacenter,
        final Enterprise enterprise)
    {
        return new Builder(context, datacenter, enterprise);
    }

    public static class Builder extends NetworkBuilder<Builder>
    {
        private Datacenter datacenter;

        private Enterprise enterprise;

        public Builder(final AbiquoContext context, final Datacenter datacenter,
            final Enterprise enterprise)
        {
            super(context);
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Datacenter.class);
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Enterprise.class);
            this.datacenter = datacenter;
            this.enterprise = enterprise;
            this.context = context;
        }

        public Builder datacenter(final Datacenter datacenter)
        {
            this.datacenter = datacenter;
            return this;
        }

        public Builder enterprise(final Enterprise enterprise)
        {
            this.enterprise = enterprise;
            return this;
        }

        public ExternalNetwork build()
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
            dto.setType(NetworkType.EXTERNAL);

            ExternalNetwork network = new ExternalNetwork(context, dto);
            network.datacenter = datacenter;
            network.enterprise = enterprise;

            return network;
        }

        public static Builder fromExternalNetwork(final ExternalNetwork in)
        {
            return ExternalNetwork.builder(in.context, in.datacenter, in.enterprise)
                .name(in.getName()).tag(in.getTag()).gateway(in.getGateway())
                .address(in.getAddress()).mask(in.getMask()).primaryDNS(in.getPrimaryDNS())
                .secondaryDNS(in.getSecondaryDNS()).sufixDNS(in.getSufixDNS())
                .defaultNetwork(in.getDefaultNetwork());
        }
    }

    @Override
    public String toString()
    {
        return "External " + super.toString();
    }
}
