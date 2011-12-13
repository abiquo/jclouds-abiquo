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

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.Volume;
import org.jclouds.abiquo.domain.cloud.options.VirtualDatacenterOptions;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.strategy.cloud.DetachVolumes;
import org.jclouds.abiquo.strategy.cloud.ListVirtualDatacenters;

import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Provides high level Abiquo cloud operations.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Singleton
public class BaseCloudService implements CloudService
{
    @VisibleForTesting
    protected AbiquoContext context;

    @VisibleForTesting
    protected final ListVirtualDatacenters listVirtualDatacenters;

    @VisibleForTesting
    protected final DetachVolumes detachVolumes;

    @Inject
    protected BaseCloudService(final AbiquoContext context,
        final ListVirtualDatacenters listVirtualDatacenters, final DetachVolumes detachVolumes)
    {
        this.context = checkNotNull(context, "context");
        this.listVirtualDatacenters =
            checkNotNull(listVirtualDatacenters, "listVirtualDatacenters");
        this.detachVolumes = checkNotNull(detachVolumes, "detachVolumes");
    }

    /*********************** Virtual Datacenter ********************** */

    @Override
    public Iterable<VirtualDatacenter> listVirtualDatacenters()
    {
        return listVirtualDatacenters.execute();
    }

    @Override
    public Iterable<VirtualDatacenter> listVirtualDatacenters(final Enterprise enterprise)
    {
        checkNotNull(enterprise, ValidationErrors.NULL_RESOURCE + Enterprise.class);
        checkNotNull(enterprise.getId(), ValidationErrors.MISSING_REQUIRED_FIELD + " id in "
            + Enterprise.class);

        VirtualDatacenterOptions options =
            VirtualDatacenterOptions.builder().enterpriseId(enterprise.getId()).build();

        return listVirtualDatacenters.execute(options);
    }

    @Override
    public Iterable<VirtualDatacenter> listVirtualDatacenters(
        final Predicate<VirtualDatacenter> filter)
    {
        return listVirtualDatacenters.execute(filter);
    }

    @Override
    public VirtualDatacenter getVirtualDatacenter(final Integer virtualDatacenterId)
    {
        VirtualDatacenterDto virtualDatacenter =
            context.getApi().getCloudClient().getVirtualDatacenter(virtualDatacenterId);
        return wrap(context, VirtualDatacenter.class, virtualDatacenter);
    }

    @Override
    public VirtualDatacenter findVirtualDatacenter(final Predicate<VirtualDatacenter> filter)
    {
        return Iterables.getFirst(listVirtualDatacenters(filter), null);
    }

    @Override
    public void detachVolumes(final VirtualMachine virtualMachine, final Volume... volumes)
    {
        detachVolumes.execute(virtualMachine, Arrays.asList(volumes));
    }

}
