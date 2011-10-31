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

import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.enterprise.EnterprisesDto;

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
    // Enterprise

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

}
