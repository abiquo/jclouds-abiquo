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

import com.abiquo.server.core.infrastructure.network.VMNetworkConfigurationDto;

/**
 * Adds high level functionality to {@link VMNetworkConfigurationDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a
 *      href="http://community.abiquo.com/display/ABI20/Virtual+Machine+Network+Configuration+Resource">
 *      http://community.abiquo.com/display/ABI20/Virtual+Machine+Network+Configuration+Resource</a>
 */
public class NetworkConfiguration extends DomainWrapper<VMNetworkConfigurationDto>
{
    VMNetworkConfigurationDto target;

    /**
     * Constructor to be used only by the builder.
     */
    protected NetworkConfiguration(final AbiquoContext context,
        final VMNetworkConfigurationDto target)
    {
        super(context, target);
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getGateway()
    {
        return target.getGateway();
    }

    public String getPrimaryDNS()
    {
        return target.getPrimaryDNS();
    }

    public String getSecondaryDNS()
    {
        return target.getSecondaryDNS();
    }

    public String getSuffixDNS()
    {
        return target.getSuffixDNS();
    }

    public Boolean getUsed()
    {
        return target.getUsed();
    }

    @Override
    public String toString()
    {
        return "NetworkConfiguration [getId()=" + getId() + ", getGateway()=" + getGateway()
            + ", getPrimaryDNS()=" + getPrimaryDNS() + ", getSecondaryDNS()=" + getSecondaryDNS()
            + ", getSuffixDNS()=" + getSuffixDNS() + ", getUsed()=" + getUsed() + "]";
    }

}
