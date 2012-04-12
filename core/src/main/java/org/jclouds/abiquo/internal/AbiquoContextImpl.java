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

package org.jclouds.abiquo.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.abiquo.features.services.SearchService;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.Utils;
import org.jclouds.compute.internal.ComputeServiceContextImpl;
import org.jclouds.domain.Credentials;
import org.jclouds.rest.RestContext;
import org.jclouds.rest.internal.RestContextImpl;

/**
 * Abiquo {@link RestContextImpl} implementation to expose high level Abiquo functionalities.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class AbiquoContextImpl extends ComputeServiceContextImpl<AbiquoClient, AbiquoAsyncClient>
    implements AbiquoContext
{
    private final AdministrationService administrationService;

    private final CloudService cloudService;

    private final SearchService searchService;

    private final MonitoringService monitoringService;

    @Inject
    public AbiquoContextImpl(final ComputeService computeService,
        final Map<String, Credentials> credentialStore, final Utils utils,
        final RestContext<AbiquoClient, AbiquoAsyncClient> providerSpecificContext,
        final AdministrationService administrationService, final CloudService cloudService,
        final SearchService searchService, final MonitoringService monitoringService)
    {
        super(computeService, credentialStore, utils, providerSpecificContext);
        this.administrationService = checkNotNull(administrationService, "administrationService");
        this.cloudService = checkNotNull(cloudService, "cloudService");
        this.searchService = checkNotNull(searchService, "searchService");
        this.monitoringService = checkNotNull(monitoringService, "monitoringService");
    }

    @Override
    public AdministrationService getAdministrationService()
    {
        return administrationService;
    }

    @Override
    public CloudService getCloudService()
    {
        return cloudService;
    }

    @Override
    public SearchService getSearchService()
    {
        return searchService;
    }

    @Override
    public MonitoringService getMonitoringService()
    {
        return monitoringService;
    }

}
