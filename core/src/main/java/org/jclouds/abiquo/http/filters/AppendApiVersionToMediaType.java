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

package org.jclouds.abiquo.http.filters;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.http.utils.ModifyRequest;
import org.jclouds.rest.annotations.ApiVersion;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.net.HttpHeaders;

/**
 * Appends the api version to the media types to ensure the input and output of api calls will be in
 * the desired format.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class AppendApiVersionToMediaType implements HttpRequestFilter
{
    /** The version to append to media types without version. */
    protected String apiVersion;

    @Inject
    public AppendApiVersionToMediaType(@ApiVersion final String apiVersion)
    {
        super();
        this.apiVersion = apiVersion;
    }

    @Override
    public HttpRequest filter(final HttpRequest request) throws HttpException
    {
        Collection<String> accept = request.getHeaders().get(HttpHeaders.ACCEPT);
        Collection<String> contentType = request.getHeaders().get(HttpHeaders.CONTENT_TYPE);

        Iterable<String> acceptWithVersion = appendVersion(accept);
        Iterable<String> contentTypeWithVersion = appendVersion(contentType);

        HttpRequest requestWithVersionInMediaTypes = request;

        if (!accept.isEmpty())
        {
            requestWithVersionInMediaTypes =
                ModifyRequest.replaceHeader(requestWithVersionInMediaTypes, HttpHeaders.ACCEPT,
                    acceptWithVersion);
        }
        if (!contentType.isEmpty())
        {
            requestWithVersionInMediaTypes =
                ModifyRequest.replaceHeader(requestWithVersionInMediaTypes,
                    HttpHeaders.CONTENT_TYPE, contentTypeWithVersion);
        }

        return requestWithVersionInMediaTypes;
    }

    @VisibleForTesting
    protected Iterable<String> appendVersion(final Collection<String> headers)
    {
        return Iterables.transform(headers, new Function<String, String>()
        {
            @Override
            public String apply(final String input)
            {
                MediaType mediaType = MediaType.valueOf(input);
                if (!mediaType.getParameters().containsKey("version"))
                {
                    return mediaType.toString() + ";version=" + apiVersion;
                }
                else
                {
                    return mediaType.toString();
                }
            }
        });
    }

}
