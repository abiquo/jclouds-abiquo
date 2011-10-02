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

package org.jclouds.abiquo;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.functions.ReturnEmptySetOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnVoidOnNotFoundOr404;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo via their REST API.
 * <p/>
 * 
 * @see AbiquoClient
 * @author Ignasi Barrera
 */
@RequestFilters(BasicAuthentication.class)
public interface AbiquoAsyncClient
{
    public static final String API_VERSION = "2.0-SNAPSHOT";

    /*
     * TODO: define interface methods for Abiquo
     */

    /**
     * @see AbiquoClient#list()
     */
    @GET
    @Path("/items")
    @Consumes(MediaType.TEXT_PLAIN)
    @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
    ListenableFuture<String> list();

    /**
     * @see AbiquoClient#get(long)
     */
    @GET
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/items/{itemId}")
    ListenableFuture<String> get(@PathParam("itemId") long id);

    /**
     * @see AbiquoClient#delete
     */
    @DELETE
    @Path("/items/{itemId}")
    @ExceptionParser(ReturnVoidOnNotFoundOr404.class)
    ListenableFuture<Void> delete(@PathParam("itemId") long id);
}
