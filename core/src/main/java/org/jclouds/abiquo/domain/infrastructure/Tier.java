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

package org.jclouds.abiquo.domain.infrastructure;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.annotations.EnterpriseEdition;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.storage.TierDto;

/**
 * Adds high level functionality to {@link TierDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI20/Tier+Resource
 */
@EnterpriseEdition
public class Tier extends DomainWrapper<TierDto>
{
    /** The datacenter where the tier belongs. */
    // Package protected to allow navigation from children
    Datacenter datacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected Tier(final AbiquoContext context, final TierDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Tier+Resource#TierResource-UpdateaTier">
     *      http://community.abiquo.com/display/ABI20/Tier+Resource#TierResource-UpdateaTier</a>
     */
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateTier(target);
    }

    // Parent access

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-RetrieveaDatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-RetrieveaDatacenter</a>
     */
    public Datacenter getDatacenter()
    {
        Integer datacenterId = target.getIdFromLink(ParentLinkName.DATACENTER);
        DatacenterDto dto = context.getApi().getInfrastructureClient().getDatacenter(datacenterId);
        datacenter = wrap(context, Datacenter.class, dto);
        return datacenter;
    }

    // Delegate methods

    public String getDescription()
    {
        return target.getDescription();
    }

    public boolean getEnabled()
    {
        return target.getEnabled();
    }

    public Integer getId()
    {
        return target.getId();
    }

    public String getName()
    {
        return target.getName();
    }

    public void setDescription(final String description)
    {
        target.setDescription(description);
    }

    public void setEnabled(final boolean enabled)
    {
        target.setEnabled(enabled);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    @Override
    public String toString()
    {
        return "Tier [id=" + getId() + ", description=" + getDescription() + ", enabled="
            + getEnabled() + ", name=" + getName() + "]";
    }

}
