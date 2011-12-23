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

import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.abiquo.features.services.SearchService;
import org.jclouds.abiquo.internal.AbiquoContextImpl;
import org.jclouds.rest.RestContext;

import com.google.inject.ImplementedBy;

/**
 * Abiquo {@link RestContext} implementation to expose high level Abiquo functionalities.
 * 
 * @author Ignasi Barrera
 */
@ImplementedBy(AbiquoContextImpl.class)
public interface AbiquoContext extends RestContext<AbiquoClient, AbiquoAsyncClient>
{
    /**
     * Returns the administration service.
     * <p>
     * This service provides an entry point to infrastructure administration tasks.
     */
    AdministrationService getAdministrationService();

    /**
     * Returns the cloud service.
     * <p>
     * This service provides an entry point to cloud management tasks.
     */
    CloudService getCloudService();

    /**
     * Returns the search service.
     * <p>
     * This service provides an entry point to listing and filtering tasks.
     */
    SearchService getSearchService();

    /**
     * Returns the monitoring service.
     * <p>
     * This service provides an entry point to asyncohonous task monitoring tasks.
     */
    MonitoringService getMonitoringService();
}
