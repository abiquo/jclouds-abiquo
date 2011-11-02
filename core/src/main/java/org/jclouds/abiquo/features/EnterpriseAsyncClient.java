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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.binders.BindToXMLPayloadAndPath;
import org.jclouds.abiquo.functions.infrastructure.DatacenterId;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.ParamParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.server.core.enterprise.DatacenterLimitsDto;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.enterprise.EnterprisesDto;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo Enterprise API.
 * 
 * @see http://community.abiquo.com/display/ABI18/API+Reference
 * @see EnterpriseClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@RequestFilters(BasicAuthentication.class)
@Consumes(MediaType.APPLICATION_XML)
@Path("/admin/enterprises")
public interface EnterpriseAsyncClient
{
    /*                                   ********************** Enterprise ********************** */

    /**
     * @see EnterpriseClient#listEnterprises()
     */
    @GET
    ListenableFuture<EnterprisesDto> listEnterprises();

    /**
     * @see EnterpriseClient#createEnterprise(EnterpriseDto)
     */
    @POST
    ListenableFuture<EnterpriseDto> createEnterprise(
        @BinderParam(BindToXMLPayload.class) EnterpriseDto enterprise);

    /**
     * @see EnterpriseClient#getEnterprise(Integer)
     */
    @GET
    @Path("{enterprise}")
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<EnterpriseDto> getEnterprise(@PathParam("enterprise") Integer enterpriseId);

    /**
     * @see EnterpriseClient#updateEnterprise(EnterpriseDto)
     */
    @PUT
    ListenableFuture<EnterpriseDto> updateEnterprise(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) EnterpriseDto enterprise);

    /**
     * @see EnterpriseClient#deleteEnterprise(EnterpriseDto)
     */
    @DELETE
    ListenableFuture<Void> deleteEnterprise(
        @EndpointLink("edit") @BinderParam(BindToPath.class) EnterpriseDto enterprise);

    /*                                   ********************** Enterprise Limits ********************** */

    /**
     * @see EnterpriseClient#createLimits(EnterpriseDto, DatacenterDto, DatacenterLimitsDto)
     */
    @POST
    ListenableFuture<DatacenterLimitsDto> createLimits(
        @EndpointLink("limits") @BinderParam(BindToPath.class) final EnterpriseDto enterprise,
        @QueryParam("datacenter") @ParamParser(DatacenterId.class) final DatacenterDto datacenter,
        @BinderParam(BindToXMLPayload.class) DatacenterLimitsDto limits);

    /**
     * @see EnterpriseClient#getLimits(EnterpriseDto, DatacenterDto)
     */
    @GET
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<DatacenterLimitsDto> getLimits(
        @EndpointLink("limits") @BinderParam(BindToPath.class) final EnterpriseDto enterprise,
        @QueryParam("datacenter") @ParamParser(DatacenterId.class) final DatacenterDto datacenter);

    /**
     * @see EnterpriseClient#updateLimits(DatacenterLimitsDto)
     */
    @PUT
    ListenableFuture<DatacenterLimitsDto> updateLimits(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) DatacenterLimitsDto limits);

    /**
     * @see EnterpriseClient#deleteLimits(DatacenterLimitsDto)
     */
    @DELETE
    ListenableFuture<Void> deleteLimits(
        @EndpointLink("edit") @BinderParam(BindToPath.class) DatacenterLimitsDto limits);

    /**
     * @see EnterpriseClient#listLimits(Enterprise)
     */
    @GET
    ListenableFuture<DatacentersLimitsDto> listLimits(
        @EndpointLink("limits") @BinderParam(BindToPath.class) EnterpriseDto enterprise);
}
