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

package org.jclouds.abiquo.binders.infrastructure;

import static com.google.common.base.Preconditions.checkArgument;

import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.rest.internal.GeneratedHttpRequest;

import com.abiquo.model.transport.SingleResourceTransportDto;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;

/**
 * Bind the check uri of the remote service to the request path.
 * 
 * @author Ignasi Barrera
 * @deprecated This binder should be removed when the following improvement gets fixed:
 *             http://jira.abiquo.com/browse/ABICLOUDPREMIUM-2607
 */
@Deprecated
public class BindRemoteServiceCheck extends BindToPath
{
    @Override
    protected String getNewEndpoint(final GeneratedHttpRequest< ? > gRequest,
        final SingleResourceTransportDto dto)
    {
        checkArgument(dto instanceof RemoteServiceDto,
            "this binder is only valid for RemoteServiceDto objects");

        RemoteServiceDto remoteService = (RemoteServiceDto) dto;

        if (!remoteService.getType().canBeChecked())
        {
            throw new IllegalArgumentException("The given remote service ["
                + remoteService.getType().name() + "] cannot be checked");
        }

        return remoteService.getUri() + "/check";
    }

}
