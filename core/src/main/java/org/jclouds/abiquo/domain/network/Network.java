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
import org.jclouds.abiquo.domain.DomainWrapper;

import com.abiquo.model.enumerator.NetworkType;
import com.abiquo.server.core.infrastructure.network.VLANNetworkDto;

/**
 * Adds high level functionality to {@link VLANNetworkDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Private+Network+Resource">
 *      http://community.abiquo.com/display/ABI20/Private+Network+Resource</a>
 */
public class Network extends DomainWrapper<VLANNetworkDto>
{
    VLANNetworkDto target;

    /**
     * Constructor to be used only by the builder.
     */
    protected Network(final AbiquoContext context, final VLANNetworkDto target)
    {
        super(context, target);
    }

    // Domain operations

    // Children access

    // Builder

    public static Builder builder(final AbiquoContext context)
    {
        return new Builder(context);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private String name;

        private Integer tag;

        private String gateway;

        private String address;

        private Integer mask;

        private String primaryDNS;

        private String secondaryDNS;

        private String sufixDNS;

        private Boolean defaultNetwork;

        private Boolean unmanaged;

        private NetworkType type;

        public Builder(final AbiquoContext context)
        {
            super();
            this.context = context;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder tag(final int tag)
        {
            this.tag = tag;
            return this;
        }

        public Builder gateway(final String gateway)
        {
            this.gateway = gateway;
            return this;
        }

        public Builder address(final String address)
        {
            this.address = address;
            return this;
        }

        public Builder mask(final int mask)
        {
            this.mask = mask;
            return this;
        }

        public Builder primaryDNS(final String primaryDNS)
        {
            this.primaryDNS = primaryDNS;
            return this;
        }

        public Builder secondaryDNS(final String secondaryDNS)
        {
            this.secondaryDNS = secondaryDNS;
            return this;
        }

        public Builder sufixDNS(final String sufixDNS)
        {
            this.sufixDNS = sufixDNS;
            return this;
        }

        public Builder defaultNetwork(final boolean defaultNetwork)
        {
            this.defaultNetwork = defaultNetwork;
            return this;
        }

        public Builder unmanaged(final boolean unmanaged)
        {
            this.unmanaged = unmanaged;
            return this;
        }

        public Builder type(final NetworkType type)
        {
            this.type = type;
            return this;
        }

        public Network build()
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

            return new Network(context, dto);
        }

        public static Builder fromNetwork(final Network in)
        {
            return Network.builder(in.context).name(in.getName()).tag(in.getTag()).gateway(
                in.getGateway()).address(in.getAddress()).mask(in.getMask()).primaryDNS(
                in.getPrimaryDNS()).secondaryDNS(in.getSecondaryDNS()).sufixDNS(in.getSufixDNS())
                .defaultNetwork(in.getDefaultNetwork()).unmanaged(in.getUnmanaged()).type(
                    in.getType());
        }
    }

    // Delegate methods

    public String getAddress()
    {
        return target.getAddress();
    }

    public Boolean getDefaultNetwork()
    {
        return target.getDefaultNetwork();
    }

    public String getGateway()
    {
        return target.getGateway();
    }

    public Integer getId()
    {
        return target.getId();
    }

    public Integer getMask()
    {
        return target.getMask();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getPrimaryDNS()
    {
        return target.getPrimaryDNS();
    }

    public String getSecondaryDNS()
    {
        return target.getSecondaryDNS();
    }

    public String getSufixDNS()
    {
        return target.getSufixDNS();
    }

    public Integer getTag()
    {
        return target.getTag();
    }

    public NetworkType getType()
    {
        return target.getType();
    }

    public Boolean getUnmanaged()
    {
        return target.getUnmanaged();
    }

    public void setAddress(final String address)
    {
        target.setAddress(address);
    }

    public void setDefaultNetwork(final Boolean defaultNetwork)
    {
        target.setDefaultNetwork(defaultNetwork);
    }

    public void setGateway(final String gateway)
    {
        target.setGateway(gateway);
    }

    public void setMask(final Integer mask)
    {
        target.setMask(mask);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPrimaryDNS(final String primaryDNS)
    {
        target.setPrimaryDNS(primaryDNS);
    }

    public void setSecondaryDNS(final String secondaryDNS)
    {
        target.setSecondaryDNS(secondaryDNS);
    }

    public void setSufixDNS(final String sufixDNS)
    {
        target.setSufixDNS(sufixDNS);
    }

    public void setTag(final Integer tag)
    {
        target.setTag(tag);
    }

    public void setType(final NetworkType type)
    {
        target.setType(type);
    }

    public void setUnmanaged(final Boolean unmanaged)
    {
        target.setUnmanaged(unmanaged);
    }
}
