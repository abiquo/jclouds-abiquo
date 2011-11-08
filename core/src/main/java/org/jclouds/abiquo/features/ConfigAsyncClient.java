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
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.jclouds.abiquo.binders.AppendOptionsToPath;
import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.domain.config.options.LicenseOptions;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.binders.BindToXMLPayload;

import com.abiquo.server.core.config.LicenseDto;
import com.abiquo.server.core.config.LicensesDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo Infrastructure API.
 * 
 * @see http://community.abiquo.com/display/ABI18/API+Reference
 * @see AdminClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@RequestFilters(BasicAuthentication.class)
@Consumes(MediaType.APPLICATION_XML)
@Path("/config")
public interface ConfigAsyncClient
{
    /*********************** License ********************** */

    /**
     * @see ConfigClient#listLicenses()
     */
    @GET
    @Path("/licenses")
    ListenableFuture<LicensesDto> listLicenses();

    /**
     * @see ConfigClient#listLicenses()
     */
    @GET
    @Path("/licenses")
    ListenableFuture<LicensesDto> listLicenses(
        @BinderParam(AppendOptionsToPath.class) LicenseOptions options);

    /**
     * @see ConfigClient#addLicense(LicenseDto)
     */
    @POST
    @Path("/licenses")
    ListenableFuture<LicenseDto> addLicense(@BinderParam(BindToXMLPayload.class) LicenseDto license);

    /**
     * @see ConfigClient#removeLicense(LicenseDto)
     */
    @DELETE
    ListenableFuture<Void> removeLicense(
        @EndpointLink("edit") @BinderParam(BindToPath.class) LicenseDto role);
}
