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

package org.jclouds.abiquo.domain.task;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;

import com.abiquo.server.core.task.TaskDto;

/**
 * Adds generic high level functionality to {TaskDto}.
 * 
 * @author Francesc Montserrat
 */
public class AsyncTask extends DomainWrapper<TaskDto>
{
    /**
     * Constructor to be used only by the builder.
     */
    protected AsyncTask(final AbiquoContext context, final TaskDto target)
    {
        super(context, target);
    }

    // Delegate methods

    public List<AsyncJob> getJobs()
    {
        return wrap(context, AsyncJob.class, target.getJobs().getCollection());
    }

    public String getOwnerId()
    {
        return target.getOwnerId();
    }

    public String getTaskId()
    {
        return target.getTaskId();
    }

    public long getTimestamp()
    {
        return target.getTimestamp();
    }

    public String getUserId()
    {
        return target.getUserId();
    }

}
