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

import java.util.concurrent.TimeUnit;

import org.jclouds.concurrent.Timeout;

import com.abiquo.server.core.enterprise.DatacenterLimitsDto;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.enterprise.EnterprisesDto;
import com.abiquo.server.core.enterprise.UserDto;
import com.abiquo.server.core.enterprise.UsersDto;
import com.abiquo.server.core.infrastructure.DatacenterDto;

/**
 * Provides synchronous access to Abiquo Enterprise API.
 * 
 * @see http://community.abiquo.com/display/ABI18/API+Reference
 * @see EnterpriseAsyncClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Timeout(duration = 30, timeUnit = TimeUnit.SECONDS)
public interface EnterpriseClient
{
    /*           ********************** Enterprise ********************** */

    /**
     * List all enterprises.
     * 
     * @return The list of Enterprises.
     */
    EnterprisesDto listEnterprises();

    /**
     * Create a new enterprise.
     * 
     * @param enterprise The enterprise to be created.
     * @return The created enterprise.
     */
    EnterpriseDto createEnterprise(EnterpriseDto enterprise);

    /**
     * Get the given enterprise.
     * 
     * @param enterpriseId The id of the enterprise.
     * @return The enterprise or <code>null</code> if it does not exist.
     */
    EnterpriseDto getEnterprise(Integer enterpriseId);

    /**
     * Updates an existing enterprise.
     * 
     * @param enterprise The new attributes for the enterprise.
     * @return The updated enterprise.
     */
    EnterpriseDto updateEnterprise(EnterpriseDto enterprise);

    /**
     * Deletes an existing enterprise.
     * 
     * @param enterprise The enterprise to delete.
     */
    void deleteEnterprise(EnterpriseDto enterprise);

    /*           ********************** Enterprise Limits ********************** */

    /**
     * Allows the given enterprise to use the given datacenter with the given limits.
     * 
     * @param enterprise The enterprise.
     * @param datacenter The datacenter to allow to the given enterprise.
     * @param limits The usage limits for the enterprise in the given datacenter.
     * @return The usage limits for the enterprise in the given datacenter.
     */
    DatacenterLimitsDto createLimits(EnterpriseDto enterprise, DatacenterDto datacenter,
        DatacenterLimitsDto limits);

    /**
     * Retreives the limits for the given enterprise and datacenter.
     * 
     * @param enterprise The enterprise.
     * @param datacenter The datacenter.
     * @return The usage limits for the enterprise in the given datacenter.
     */
    DatacentersLimitsDto getLimits(EnterpriseDto enterprise, DatacenterDto datacenter);

    /**
     * Retreives limits for the given enterprise and any datacenter.
     * 
     * @param enterprise The enterprise.
     * @return The usage limits for the enterprise on any datacenter.
     */
    DatacentersLimitsDto listLimits(EnterpriseDto enterprise);

    /**
     * Updates an existing enterprise-datacenter limits.
     * 
     * @param limits The new set of limits.
     * @return The updated limits.
     */
    DatacenterLimitsDto updateLimits(DatacenterLimitsDto limits);

    /**
     * Deletes existing limits for a pair enterprise-datacenter.
     * 
     * @param limits The limits to delete.
     */
    void deleteLimits(DatacenterLimitsDto limits);

    /*           ********************** User ********************** */

    /**
     * Retreives users of the given enterprise.
     * 
     * @param enterprise The enterprise.
     * @return The users of the enterprise.
     */
    UsersDto listUsers(EnterpriseDto enterprise);

    /**
     * Create a new user in the given enterprise.
     * 
     * @param enterprise The enterprise.
     * @param user The user to be created.
     * @return The created user.
     */
    UserDto createUser(EnterpriseDto enterprise, UserDto limits);

    /**
     * Get the given user from the given enterprise.
     * 
     * @param enterprise The enterprise.
     * @param userId The id of the user.
     * @return The user or <code>null</code> if it does not exist.
     */
    UserDto getUser(final EnterpriseDto enterprise, final Integer idUser);

    /**
     * Updates an existing user.
     * 
     * @param enterprise The new attributes for the user.
     * @return The updated user.
     */
    UserDto updateUser(UserDto user);

    /**
     * Deletes existing user.
     * 
     * @param user The user to delete.
     */
    void deleteUser(UserDto user);
}
