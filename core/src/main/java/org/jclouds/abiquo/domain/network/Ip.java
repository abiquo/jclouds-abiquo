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

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.ValidationErrors;

import com.abiquo.model.enumerator.NetworkType;
import com.abiquo.server.core.infrastructure.network.IpPoolManagementDto;

/**
 * Adds generic high level functionality to {IpPoolManagementDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class Ip extends DomainWrapper<IpPoolManagementDto>
{
    /**
     * Constructor to be used only by the builder.
     */
    protected Ip(final AbiquoContext context, final IpPoolManagementDto target)
    {
        super(context, target);
    }

    // Domain operations

    public PublicIp toPublicIP()
    {
        checkNotNull(target.searchLink("publicnetwork"), ValidationErrors.MISSING_REQUIRED_LINK);

        return wrap(context, PublicIp.class, target);
    }

    public Ip toPrivateIP()
    {
        checkNotNull(target.searchLink("privatenetwork"), ValidationErrors.MISSING_REQUIRED_LINK);

        return wrap(context, Ip.class, target);
    }

    public Ip toExternalIP()
    {
        checkNotNull(target.searchLink("externalnetwork"), ValidationErrors.MISSING_REQUIRED_LINK);

        return wrap(context, Ip.class, target);
    }

    public Ip toUnmanagedIP()
    {
        checkNotNull(target.searchLink("unmanagednetwork"), ValidationErrors.MISSING_REQUIRED_LINK);

        return wrap(context, Ip.class, target);
    }

    /**
     * Retrevies the type of the network using the ip or <value>null</value> if the ip does not
     * belong to a network.
     */
    public NetworkType getNetworkType()
    {
        if (target.searchLink("privatenetwork") != null)
        {
            return NetworkType.INTERNAL;
        }
        if (target.searchLink("publicnetwork") != null)
        {
            return NetworkType.PUBLIC;
        }
        if (target.searchLink("externalnetwork") != null)
        {
            return NetworkType.PUBLIC;
        }
        if (target.searchLink("unmanagednetwork") != null)
        {
            return NetworkType.UNMANAGED;
        }

        return null;
    }

    // Delegate methods

    public boolean getAvailable()
    {
        return target.getAvailable();
    }

    public Integer getId()
    {
        return target.getId();
    }

    public String getIp()
    {
        return target.getIp();
    }

    public String getMac()
    {
        return target.getMac();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getNetworkName()
    {
        return target.getNetworkName();
    }

    public boolean getQuarantine()
    {
        return target.getQuarantine();
    }

    public void setAvailable(final boolean available)
    {
        target.setAvailable(available);
    }

    public void setQuarantine(final boolean quarantine)
    {
        target.setQuarantine(quarantine);
    }

    @Override
    public String toString()
    {
        return "Ip [id=" + getId() + ", available=" + getAvailable() + ", ip=" + getIp() + ", mac="
            + getMac() + ", name=" + getName() + ", networkName=" + getNetworkName()
            + ", quarantine=" + getQuarantine() + "]";
    }
}
