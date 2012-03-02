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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.abiquo.binders.AppendOptionsToPath;
import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.binders.BindToXMLPayloadAndPath;
import org.jclouds.abiquo.domain.cloud.options.VirtualMachineTemplateOptions;
import org.jclouds.abiquo.http.filters.AbiquoAuthentication;
import org.jclouds.abiquo.http.filters.AppendApiVersionToMediaType;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.server.core.appslibrary.VirtualMachineTemplateDto;
import com.abiquo.server.core.appslibrary.VirtualMachineTemplatesDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo Abiquo Apps library API.
 * 
 * @see http://community.abiquo.com/display/ABI20/API+Reference
 * @see VirtualMachineTemplateClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@RequestFilters({AbiquoAuthentication.class, AppendApiVersionToMediaType.class})
@Consumes(MediaType.APPLICATION_XML)
@Path("/admin/enterprises")
public interface VirtualMachineTemplateAsyncClient
{
    /*********************** Virtual Machine Template ***********************/

    /**
     * @see VirtualMachineTemplateClient#listVirtualMachineTemplates(Integer, Integer)
     */
    @GET
    @Path("/{enterprise}/datacenterrepositories/{datacenterrepository}/virtualmachinetemplates")
    ListenableFuture<VirtualMachineTemplatesDto> listVirtualMachineTemplates(
        @PathParam("enterprise") Integer enterpriseId,
        @PathParam("datacenterrepository") Integer datacenterRepositoryId);

    /**
     * @see VirtualMachineTemplateClient#listVirtualMachineTemplates(Integer, Integer,
     *      VirtualMachineTemplateOptions)
     */
    @GET
    @Path("/{enterprise}/datacenterrepositories/{datacenterrepository}/virtualmachinetemplates")
    ListenableFuture<VirtualMachineTemplatesDto> listVirtualMachineTemplates(
        @PathParam("enterprise") Integer enterpriseId,
        @PathParam("datacenterrepository") Integer datacenterRepositoryId,
        @BinderParam(AppendOptionsToPath.class) VirtualMachineTemplateOptions options);

    /**
     * @see VirtualMachineTemplateClient#getVirtualMachineTemplate(Integer, Integer, Integer)
     */
    @GET
    @Path("/{enterprise}/datacenterrepositories/{datacenterrepository}/virtualmachinetemplates/{virtualmachinetemplate}")
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<VirtualMachineTemplateDto> getVirtualMachineTemplate(
        @PathParam("enterprise") Integer enterpriseId,
        @PathParam("datacenterrepository") Integer datacenterRepositoryId,
        @PathParam("virtualmachinetemplate") Integer virtualMachineTemplateId);

    /**
     * @see VirtualMachineTemplateClient#updateVirtualMachineTemplate(VirtualMachineTemplateDto)
     */
    @PUT
    ListenableFuture<VirtualMachineTemplateDto> updateVirtualMachineTemplate(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) VirtualMachineTemplateDto template);

    /**
     * @see VirtualMachineTemplateClient#deleteVirtualMachineTemplate(VirtualMachineTemplateDto)
     */
    @DELETE
    ListenableFuture<Void> deleteVirtualMachineTemplate(
        @EndpointLink("edit") @BinderParam(BindToPath.class) VirtualMachineTemplateDto template);
}
