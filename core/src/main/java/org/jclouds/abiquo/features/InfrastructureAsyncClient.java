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
import javax.ws.rs.core.MediaType;

import org.jclouds.abiquo.binders.BindToXMLPayload;
import org.jclouds.abiquo.functions.infrastructure.ParseDatacenter;
import org.jclouds.abiquo.functions.infrastructure.ParseDatacenters;
import org.jclouds.abiquo.functions.infrastructure.ParseRack;
import org.jclouds.abiquo.functions.infrastructure.ParseRacks;
import org.jclouds.abiquo.reference.AbiquoMediaType;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.DatacentersDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.abiquo.server.core.infrastructure.RacksDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo Infrastructure API.
 * 
 * @see http://community.abiquo.com/display/ABI18/API+Reference
 * @see InfrastructureClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@RequestFilters(BasicAuthentication.class)
@Consumes(MediaType.APPLICATION_XML)
@Path("/admin")
public interface InfrastructureAsyncClient
{
    /**
     * @see InfrastructureClient#listDatacenters()
     */
    @GET
    @Path("/datacenters")
    @ResponseParser(ParseDatacenters.class)
    ListenableFuture<DatacentersDto> listDatacenters();

    /**
     * @see InfrastructureClient#createDatacenter(DatacenterDto)
     */
    @POST
    @Path("/datacenters")
    @ResponseParser(ParseDatacenter.class)
    ListenableFuture<DatacenterDto> createDatacenter(
        @BinderParam(BindToXMLPayload.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#getDatacenter
     */
    @GET
    @Path("/datacenters/{datacenter}")
    @ResponseParser(ParseDatacenter.class)
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<DatacenterDto> getDatacenter(@PathParam("datacenter") Integer datacenterId);

    /**
     * @see InfrastructureClient#updateDatacenter(Integer, DatacenterDto)
     */
    @PUT
    @Path("/datacenters/{datacenter}")
    @ResponseParser(ParseDatacenter.class)
    ListenableFuture<DatacenterDto> updateDatacenter(@PathParam("datacenter") Integer datacenterId,
        @BinderParam(BindToXMLPayload.class) DatacenterDto datacenter);

    /**
     * @see InfrastructureClient#deleteDatacenter(Integer datacenterId)
     */
    @DELETE
    @Path("/datacenters/{datacenter}")
    ListenableFuture<Void> deleteDatacenter(@PathParam("datacenter") Integer datacenterId);

    /**
     * @see InfrastructureClient#listRacks(Integer datacenterId)
     */
    @GET
    @Path("/datacenters/{datacenter}/racks")
    @Consumes(AbiquoMediaType.APPLICATION_NOTMANAGEDRACKSDTO_XML)
    @ResponseParser(ParseRacks.class)
    ListenableFuture<RacksDto> listRacks(@PathParam("datacenter") Integer datacenterId);

    @POST
    @Path("/datacenters/{datacenter}/racks")
    @ResponseParser(ParseRack.class)
    ListenableFuture<RackDto> createRack(@PathParam("datacenter") Integer datacenterId,
        @BinderParam(BindToXMLPayload.class) RackDto rack);
}
