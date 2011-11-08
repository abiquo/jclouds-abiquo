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

package org.jclouds.abiquo.features;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.binders.BindToXMLPayloadAndPath;
import org.jclouds.abiquo.reference.rest.AbiquoMediaType;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.server.core.enterprise.RoleDto;
import com.abiquo.server.core.enterprise.RolesDto;
import com.abiquo.server.core.enterprise.UserDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo Infrastructure API.
 * 
 * @see http://community.abiquo.com/display/ABI20/API+Reference
 * @see AdminClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@RequestFilters(BasicAuthentication.class)
@Consumes(MediaType.APPLICATION_XML)
@Path("/admin")
public interface AdminAsyncClient
{
    /*********************** Role ********************** */

    /**
     * @see AdminClient#listRoles()
     */
    @GET
    @Path("/roles")
    ListenableFuture<RolesDto> listRoles();

    /**
     * @see AdminClient#getRole(UserDto)
     */
    @GET
    @Consumes(AbiquoMediaType.APPLICATION_LINK_XML)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<RoleDto> getRole(
        @EndpointLink("role") @BinderParam(BindToPath.class) UserDto user);

    /**
     * @see AdminClient#getRole(Integer)
     */
    @GET
    @Path("/roles/{role}")
    @Consumes(AbiquoMediaType.APPLICATION_LINK_XML)
    @JAXBResponseParser
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<RoleDto> getRole(@PathParam("role") Integer roleId);

    /**
     * @see AdminClient#deleteRole(RoleDto)
     */
    @DELETE
    ListenableFuture<Void> deleteRole(
        @EndpointLink("edit") @BinderParam(BindToPath.class) RoleDto role);

    /**
     * @see AdminClient#updateRole(RoleDto)
     */
    @PUT
    @JAXBResponseParser
    @Produces(AbiquoMediaType.APPLICATION_LINK_XML)
    @Consumes(AbiquoMediaType.APPLICATION_LINK_XML)
    ListenableFuture<RoleDto> updateRole(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) RoleDto role);

    /**
     * @see AdminClient#createRole(RoleDto)
     */
    @POST
    @Path("/roles")
    @JAXBResponseParser
    @Produces(AbiquoMediaType.APPLICATION_LINK_XML)
    @Consumes(AbiquoMediaType.APPLICATION_LINK_XML)
    ListenableFuture<RoleDto> createRole(@BinderParam(BindToXMLPayload.class) RoleDto role);
}
