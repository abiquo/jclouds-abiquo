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

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;

import com.abiquo.server.core.infrastructure.LogicServerDto;
import com.abiquo.server.core.infrastructure.LogicServerPolicyDto;

/**
 * Adds high level functionality to {@link LogicServerDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see API: <a href="http://community.abiquo.com/display/ABI20/Rack+Resource">
 *      http://community.abiquo.com/display/ABI20/Rack+Resource</a>
 */
public class LogicServer extends DomainWrapper<LogicServerDto>
{
    public String getName()
    {
        return target.getName();
    }

    // Delegate Methods

    public void setType(final String value)
    {
        target.setType(value);
    }

    public String getAssociated()
    {
        return target.getAssociated();
    }

    public String getType()
    {
        return target.getType();
    }

    public void setAssociated(final String value)
    {
        target.setAssociated(value);
    }

    public String getAssociatedTo()
    {
        return target.getAssociatedTo();
    }

    public void setAssociatedTo(final String value)
    {
        target.setAssociatedTo(value);
    }

    public String getDescription()
    {
        return target.getDescription();
    }

    public void setDescription(final String value)
    {
        target.setDescription(value);
    }

    public List<LogicServerPolicyDto> getCollection()
    {
        return target.getCollection();
    }

    /**
     * Constructor to be used only by the builder.
     */
    protected LogicServer(final AbiquoContext context, final LogicServerDto target)
    {
        super(context, target);
    }
}
