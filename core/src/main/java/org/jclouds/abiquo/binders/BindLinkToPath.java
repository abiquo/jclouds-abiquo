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

import java.net.URI;

import javax.inject.Singleton;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.utils.ModifyRequest;
import org.jclouds.rest.Binder;
import org.jclouds.rest.internal.GeneratedHttpRequest;

import com.abiquo.model.rest.RESTLink;

/**
 * Binds the given link to the uri.
 * <p>
 * This method should be used in {@link GET} methods to get parent resources.
 * 
 * @author Francesc Montserrat
 */
@Singleton
public class BindLinkToPath implements Binder
{

    public <R extends HttpRequest> R bindToRequest(final R request, final Object input)
    {
        checkArgument(checkNotNull(request, "request") instanceof GeneratedHttpRequest< ? >,
            "this binder is only valid for GeneratedHttpRequests");
        GeneratedHttpRequest< ? > gRequest = (GeneratedHttpRequest< ? >) request;
        checkState(gRequest.getArgs() != null, "args should be initialized at this point");
        RESTLink link = checkValidInput(input);

        // Update the request URI with the configured link URI
        String newEndpoint = link.getHref();
        return bindToPath(request, newEndpoint);
    }

    /**
     * Bind the given link to the request URI.
     * 
     * @param request The request to modify.
     * @param endpoint The endpoint to use as the request URI.
     * @return The updated request.
     */
    static <R extends HttpRequest> R bindToPath(final R request, final String endpoint)
    {
        // Preserve current query and matrix parameters
        String newEndpoint = endpoint + getParameterString(request);

        // Replace the URI with the edit link in the DTO
        URI path = URI.create(newEndpoint);
        return ModifyRequest.endpoint(request, path);
    }

    static RESTLink checkValidInput(final Object input)
    {
        checkArgument(checkNotNull(input, "input") instanceof RESTLink,
            "this binder is only valid for RESTLink objects");

        return (RESTLink) input;
    }

    private static <R extends HttpRequest> String getParameterString(final R request)
    {
        String endpoint = request.getEndpoint().toString();

        int query = endpoint.indexOf('?');
        int matrix = endpoint.indexOf(';');

        if (query == -1 && matrix == -1)
        {
            // No parameters
            return "";
        }
        else if (query != -1 && matrix != -1)
        {
            // Both parameter types
            return endpoint.substring(query < matrix ? query : matrix);
        }
        else if (query != -1)
        {
            // Only request parameters
            return endpoint.substring(query);
        }
        else
        {
            // Only matrix parameters
            return endpoint.substring(matrix);
        }
    }

}
