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

package org.jclouds.abiquo.http.utils;

import javax.ws.rs.core.UriBuilder;

import org.jclouds.http.HttpRequest;

import com.google.common.collect.Multimap;

/**
 * Utility methods to manipulate request objects.
 * 
 * @author Ignasi Barrera
 * @see org.jclouds.http.utils.ModifyRequest
 */
public class ModifyRequest
{
    public static <R extends HttpRequest> R addQueryParams(R request,
        Multimap<String, String> parameters, UriBuilder builder)
    {
        return addQueryParams(request, parameters, builder, request.getSkips());
    }

    @SuppressWarnings("unchecked")
    public static <R extends HttpRequest> R addQueryParams(R request,
        Multimap<String, String> parameters, UriBuilder builder, char... skips)
    {
        builder.uri(request.getEndpoint());
        Multimap<String, String> map =
            org.jclouds.http.utils.ModifyRequest.parseQueryToMap(request.getEndpoint().getQuery());
        map.putAll(parameters);
        builder.replaceQuery(org.jclouds.http.utils.ModifyRequest.makeQueryLine(map, null, skips));
        return (R) request.toBuilder().endpoint(builder.build()).build();
    }
}
