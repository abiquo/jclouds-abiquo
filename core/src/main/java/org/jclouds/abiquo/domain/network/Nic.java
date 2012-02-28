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
import org.jclouds.abiquo.domain.task.AsyncTask;

import com.abiquo.model.transport.AcceptedRequestDto;
import com.abiquo.server.core.infrastructure.network.NicDto;

/**
 * Adds high level functionality to {@link NicDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI20/Attached+NICs+Resource
 */
public class Nic extends DomainWrapper<NicDto>
{
    /**
     * Constructor to be used only by the builder (if any).
     */
    protected Nic(final AbiquoContext context, final NicDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/Attached+NICs+Resource#AttachedNICsResource-DeleteaNICs"
     *      > http://community.abiquo.com/display/ABI20/Attached+NICs+Resource#AttachedNICsResource-
     *      DeleteaNICs</a>
     */
    public AsyncTask delete()
    {
        AcceptedRequestDto<String> taskRef = context.getApi().getCloudClient().deleteNic(target);
        target = null;
        return taskRef == null ? null : getTask(taskRef);
    }

    // Parent access

    // Delegate methods

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
}
