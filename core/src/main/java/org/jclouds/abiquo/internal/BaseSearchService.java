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
import static org.jclouds.abiquo.domain.DomainWrapper.wrap;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.Volume;
import org.jclouds.abiquo.domain.cloud.options.VolumeOptions;
import org.jclouds.abiquo.features.services.SearchService;

import com.abiquo.server.core.infrastructure.storage.VolumeManagementDto;
import com.google.common.annotations.VisibleForTesting;

/**
 * Provides high level Abiquo search, filter and pagination operations.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Singleton
public class BaseSearchService implements SearchService
{
    @VisibleForTesting
    protected AbiquoContext context;

    @Inject
    protected BaseSearchService(final AbiquoContext context)
    {
        this.context = checkNotNull(context, "context");
    }

    /*********************** Volume ********************** */

    @Override
    public Iterable<Volume> searchVolumes(final VirtualDatacenter virtualDatacenter,
        final VolumeOptions options)
    {
        List<VolumeManagementDto> volumes =
            context.getApi().getCloudClient().listVolumes(virtualDatacenter.unwrap(), options)
                .getCollection();

        return wrap(context, Volume.class, volumes);
    }
}
