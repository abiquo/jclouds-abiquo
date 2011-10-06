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

package org.jclouds.abiquo.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.PUT;

import org.jclouds.abiquo.xml.XMLParser;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.internal.GeneratedHttpRequest;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.SingleResourceTransportDto;

/**
 * Binds teh given object to the payload and extracts the path parameters from the edit link.
 * <p>
 * This method should be used in {@link PUT} methods to automatically extract the path parameters
 * from the edit link of the updated object.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class BindToXMLPayloadAndPath extends BindToXMLPayload
{
    @Inject
    public BindToXMLPayloadAndPath(final XMLParser xmlParser)
    {
        super(xmlParser);
    }

    @Override
    public <R extends HttpRequest> R bindToRequest(final R request, final Object payload)
    {
        checkArgument(checkNotNull(request, "request") instanceof GeneratedHttpRequest< ? >,
            "this binder is only valid for GeneratedHttpRequests");
        GeneratedHttpRequest< ? > gRequest = (GeneratedHttpRequest< ? >) request;
        checkState(gRequest.getArgs() != null, "args should be initialized at this point");
        SingleResourceTransportDto dto = BindToPath.checkValidInput(payload);

        // Update the request URI with the configured link URI
        RESTLink linkToUse = BindToPath.getLinkToUse(gRequest, dto);
        R updatedRequest = BindToPath.bindLinkToPath(request, linkToUse);

        // Add the payload
        return super.bindToRequest(updatedRequest, payload);
    }
}
