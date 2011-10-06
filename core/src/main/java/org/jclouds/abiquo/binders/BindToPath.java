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

import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import javax.inject.Singleton;
import javax.ws.rs.PUT;

import org.jclouds.abiquo.binders.exception.BindException;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;
import org.jclouds.rest.internal.GeneratedHttpRequest;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.SingleResourceTransportDto;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * Binds the given object to the payload and extracts the path parameters from the edit link.
 * <p>
 * This method should be used in {@link PUT} methods to automatically extract the path parameters
 * from the edit link of the updated object.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class BindToPath implements Binder
{

    @Override
    public <R extends HttpRequest> R bindToRequest(final R request, final Object input)
    {
        checkArgument(checkNotNull(request, "request") instanceof GeneratedHttpRequest< ? >,
            "this binder is only valid for GeneratedHttpRequests");
        GeneratedHttpRequest< ? > gRequest = (GeneratedHttpRequest< ? >) request;
        checkState(gRequest.getArgs() != null, "args should be initialized at this point");
        SingleResourceTransportDto dto = checkValidInput(input);

        // Update the request URI with the configured link URI
        RESTLink linkToUse = getLinkToUse(gRequest, dto);
        return bindLinkToPath(request, linkToUse);
    }

    /**
     * Get the link to be used to build the request URI.
     * 
     * @param request The current request.
     * @param payload The object containing the link.
     * @return The link to be used to build the request URI.
     */
    static RESTLink getLinkToUse(final GeneratedHttpRequest< ? > request,
        final SingleResourceTransportDto payload)
    {
        int argIndex = request.getArgs().indexOf(payload);
        Annotation[] annotations = request.getJavaMethod().getParameterAnnotations()[argIndex];

        EndpointLink linkName =
            (EndpointLink) Iterables.find(Arrays.asList(annotations),
                Predicates.instanceOf(EndpointLink.class), null);

        if (linkName == null)
        {
            throw new BindException(request,
                "Expected a EndpointLink annotation but not found in the parameter");
        }

        return checkNotNull(payload.searchLink(linkName.value()),
            "No link was found in object with rel: " + linkName);
    }

    /**
     * Bind the given link to the request URI.
     * 
     * @param request The request to modify.
     * @param link The link to use as the request URI.
     * @return The updated request.
     */
    @SuppressWarnings("unchecked")
    static <R extends HttpRequest> R bindLinkToPath(final R request, final RESTLink link)
    {
        try
        {
            // Replace the URI with the edit link in the DTO
            URI path = new URL(link.getHref()).toURI();
            return (R) request.toBuilder().endpoint(path).build();
        }
        catch (Exception ex)
        {
            throw new BindException(request, ex);
        }
    }

    static SingleResourceTransportDto checkValidInput(final Object input)
    {
        checkNotNull(input, "payload");

        if (!(input instanceof SingleResourceTransportDto))
        {
            throw new IllegalArgumentException("The BindToXMLPayloadAndPath binder can "
                + "only be used to bind SingleResourceTransportDto objects");
        }

        return (SingleResourceTransportDto) input;
    }
}
