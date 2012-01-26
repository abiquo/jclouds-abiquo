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

import org.jclouds.abiquo.binders.AppendOptionsToPath;
import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.binders.BindToXMLPayloadAndPath;
import org.jclouds.abiquo.domain.config.options.IconOptions;
import org.jclouds.abiquo.domain.config.options.LicenseOptions;
import org.jclouds.abiquo.domain.config.options.PropertyOptions;
import org.jclouds.abiquo.http.filters.AbiquoAuthentication;
import org.jclouds.abiquo.reference.annotations.EnterpriseEdition;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.abiquo.server.core.appslibrary.IconDto;
import com.abiquo.server.core.appslibrary.IconsDto;
import com.abiquo.server.core.config.LicenseDto;
import com.abiquo.server.core.config.LicensesDto;
import com.abiquo.server.core.config.SystemPropertiesDto;
import com.abiquo.server.core.config.SystemPropertyDto;
import com.abiquo.server.core.enterprise.PrivilegeDto;
import com.abiquo.server.core.enterprise.PrivilegesDto;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Abiquo Config API.
 * 
 * @see http://community.abiquo.com/display/ABI18/API+Reference
 * @see AdminClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@RequestFilters(AbiquoAuthentication.class)
@Consumes(MediaType.APPLICATION_XML)
@Path("/config")
public interface ConfigAsyncClient
{
    /*********************** License ***********************/

    /**
     * @see ConfigClient#listLicenses()
     */
    @GET
    @Path("/licenses")
    @EnterpriseEdition
    ListenableFuture<LicensesDto> listLicenses();

    /**
     * @see ConfigClient#listLicenses(LicenseOptions)
     */
    @GET
    @Path("/licenses")
    @EnterpriseEdition
    ListenableFuture<LicensesDto> listLicenses(
        @BinderParam(AppendOptionsToPath.class) LicenseOptions options);

    /**
     * @see ConfigClient#addLicense(LicenseDto)
     */
    @POST
    @Path("/licenses")
    @EnterpriseEdition
    ListenableFuture<LicenseDto> addLicense(@BinderParam(BindToXMLPayload.class) LicenseDto license);

    /**
     * @see ConfigClient#removeLicense(LicenseDto)
     */
    @DELETE
    @EnterpriseEdition
    ListenableFuture<Void> removeLicense(
        @EndpointLink("edit") @BinderParam(BindToPath.class) LicenseDto license);

    /*********************** Privilege ***********************/

    /**
     * @see ConfigClient#listPrivileges()
     */
    @GET
    @Path("/privileges")
    ListenableFuture<PrivilegesDto> listPrivileges();

    /**
     * @see ConfigClient#getPrivilege(Integer)
     */
    @GET
    @Path("/privileges/{privilege}")
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<PrivilegeDto> getPrivilege(@PathParam("privilege") Integer privilegeId);

    /*********************** System Properties ***********************/

    /**
     * @see ConfigClient#listSystemProperties()
     */
    @GET
    @Path("/properties")
    ListenableFuture<SystemPropertiesDto> listSystemProperties();

    /**
     * @see ConfigClient#listSystemProperties(PropertyOptions)
     */
    @GET
    @Path("/properties")
    ListenableFuture<SystemPropertiesDto> listSystemProperties(
        @BinderParam(AppendOptionsToPath.class) PropertyOptions options);

    /**
     * @see ConfigClient#updateSystemProperty(VirtualDatacenterDto)
     */
    @PUT
    ListenableFuture<SystemPropertyDto> updateSystemProperty(
        @EndpointLink("edit") @BinderParam(BindToXMLPayloadAndPath.class) SystemPropertyDto property);

    /*********************** Icon ***********************/

    /**
     * @see ConfigClient#listIcons()
     */
    @GET
    @Path("/icons")
    ListenableFuture<IconsDto> listIcons();

    /**
     * @see ConfigClient#listLicenses(LicenseOptions)
     */
    @GET
    @Path("/icons")
    @ExceptionParser(ReturnNullOnNotFoundOr404.class)
    ListenableFuture<IconDto> getIcon(@BinderParam(AppendOptionsToPath.class) IconOptions options);
}
