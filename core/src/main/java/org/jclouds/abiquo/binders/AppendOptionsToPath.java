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

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.net.URL;
import java.util.Map.Entry;

import javax.inject.Singleton;

import org.jclouds.abiquo.binders.exception.BindException;
import org.jclouds.abiquo.domain.infrastructure.options.QueryOptions;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;

/**
 * Appends the parameter value to the end of the request URI.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class AppendOptionsToPath implements Binder
{
    @SuppressWarnings("unchecked")
    @Override
    public <R extends HttpRequest> R bindToRequest(final R request, final Object input)
    {
        checkNotNull(input, "input");
        QueryOptions options = (QueryOptions) input;

        try
        {
            // Append the parameter to the request URI
            String valueToAppend = getValues(request, options);
            URI path = new URL(request.getEndpoint().toString() + valueToAppend).toURI();
            return (R) request.toBuilder().endpoint(path).build();
        }
        catch (Exception ex)
        {
            throw new BindException(request, ex);
        }
    }

    /**
     * Get the query param list from a QueryOptions.
     */
    protected <R extends HttpRequest> String getValues(final R request, final QueryOptions input)
    {
        String s = "";

        for (Entry<String, String> pair : input.getMap().entries())
        {
            s = "&" + pair.getKey() + "=" + pair.getValue();
        }

        return s;
    }
}
