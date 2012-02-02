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
import org.jclouds.abiquo.reference.annotations.EnterpriseEdition;

import com.abiquo.server.core.infrastructure.network.IpPoolManagementDto;

/**
 * Adds generic high level functionality to {IpPoolManagementDto} for public networks.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Public+IPs+Resource">
 *      http://community.abiquo.com/display/ABI20/Public+IPs+Resource</a>
 */
@EnterpriseEdition
public class PublicIPAddress extends IPAddress
{
    /**
     * Constructor to be used only by the builder.
     */
    protected PublicIPAddress(final AbiquoContext context, final IpPoolManagementDto target)
    {
        super(context, target);
    }

    @Override
    public String toString()
    {
        return "Public " + super.toString();
    }
}
