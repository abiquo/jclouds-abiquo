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

package org.jclouds.abiquo.domain.config;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;

import com.abiquo.server.core.appslibrary.IconDto;

/**
 * Adds high level functionality to {@link IconDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Icon+Resource">
 *      http://community.abiquo.com/display/ABI20/Icon+Resource</a>
 */

public class Icon extends DomainWrapper<IconDto>
{
    /**
     * Constructor to be used only by the builder. This resource cannot be created.
     */
    private Icon(final AbiquoContext context, final IconDto target)
    {
        super(context, target);
    }

    // Domain operations

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getPath()
    {
        return target.getPath();
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPath(final String path)
    {
        target.setPath(path);
    }
}
