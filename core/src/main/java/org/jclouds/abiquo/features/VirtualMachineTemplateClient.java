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

import org.jclouds.abiquo.domain.cloud.options.VirtualMachineTemplateOptions;
import org.jclouds.concurrent.Timeout;

import com.abiquo.server.core.appslibrary.VirtualMachineTemplateDto;
import com.abiquo.server.core.appslibrary.VirtualMachineTemplatesDto;

/**
 * Provides synchronous access to Abiquo Apps library API.
 * 
 * @see http://community.abiquo.com/display/ABI20/API+Reference
 * @see VirtualMachineTemplateAsyncClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Timeout(duration = 60, timeUnit = TimeUnit.SECONDS)
public interface VirtualMachineTemplateClient
{
    /*********************** Virtual Machine Template ***********************/

    /**
     * List all virtual machine templates for an enterprise in a datacenter repository.
     * 
     * @param enterpriseId Id of the enterprise.
     * @param datacenterRepositoryId Id of the datacenter repository contaning the templates.
     * @return The list of virtual machine templates for the enterprise in the datacenter
     *         repository.
     */
    VirtualMachineTemplatesDto listVirtualMachineTemplates(Integer enterpriseId,
        Integer datacenterRepositoryId);

    /**
     * List all virtual machine templates for an enterprise in a datacenter repository.
     * 
     * @param enterpriseId Id of the enterprise.
     * @param datacenterRepositoryId Id of the datacenter repository contaning the templates.
     * @param options The options to query the virtual machine templates.
     * @return The filtered list of virtual machine templates for the enterprise in the datacenter
     *         repository.
     */
    VirtualMachineTemplatesDto listVirtualMachineTemplates(Integer enterpriseId,
        Integer datacenterRepositoryId, VirtualMachineTemplateOptions options);

    /**
     * Get the given virtual machine template.
     * 
     * @param enterpriseId Id of the enterprise.
     * @param datacenterRepositoryId Id of the datacenter repository contaning the templates.
     * @param enterpriseId The id of the virtual machine template.
     * @return The virtual machine template or <code>null</code> if it does not exist.
     */
    VirtualMachineTemplateDto getVirtualMachineTemplate(Integer entepriseId,
        Integer datacenterRepositoryId, Integer virtualMachineTemplateId);

    /**
     * Updates an existing virtual machine template.
     * 
     * @param template The new attributes for the template.
     * @return The updated template.
     */
    VirtualMachineTemplateDto updateVirtualMachineTemplate(VirtualMachineTemplateDto template);

    /**
     * Deletes an existing virtual machine template.
     * 
     * @param template The virtual machine template to delete.
     */
    void deleteVirtualMachineTemplate(VirtualMachineTemplateDto template);
}
