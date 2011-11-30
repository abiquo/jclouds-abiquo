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

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.domain.Credentials;
import org.jclouds.lifecycle.Closer;
import org.jclouds.location.Provider;
import org.jclouds.rest.Utils;
import org.jclouds.rest.annotations.ApiVersion;
import org.jclouds.rest.annotations.Identity;
import org.jclouds.rest.internal.RestContextImpl;

import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

/**
 * Abiquo {@link RestContextImpl} implementation to expose high level Abiquo functionalities.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class AbiquoContextImpl extends RestContextImpl<AbiquoClient, AbiquoAsyncClient> implements
    AbiquoContext
{
    private final AdministrationService administrationService;

    private final CloudService cloudService;

    @Inject
    protected AbiquoContextImpl(final Closer closer,
        final Map<String, Credentials> credentialStore, final Utils utils, final Injector injector,
        final TypeLiteral<AbiquoClient> syncApi, final TypeLiteral<AbiquoAsyncClient> asyncApi,
        @Provider final URI endpoint, @Provider final String provider,
        @Identity final String identity, @ApiVersion final String apiVersion,
        final CloudService cloudService, final AdministrationService administrationService)
    {
        super(closer, credentialStore, utils, injector, syncApi, asyncApi, endpoint, provider,
            identity, apiVersion, null);
        this.administrationService = administrationService;
        this.cloudService = cloudService;
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

}
