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
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.options.EnterpriseOptions;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.LogicServer;
import org.jclouds.abiquo.domain.infrastructure.ManagedRack;
import org.jclouds.abiquo.domain.infrastructure.StorageDevice;
import org.jclouds.abiquo.domain.infrastructure.StoragePool;
import org.jclouds.abiquo.domain.infrastructure.options.StoragePoolOptions;
import org.jclouds.abiquo.domain.network.Ip;
import org.jclouds.abiquo.domain.network.PrivateNetwork;
import org.jclouds.abiquo.domain.network.options.IpOptions;
import org.jclouds.abiquo.domain.options.search.FilterOptions;
import org.jclouds.abiquo.features.services.SearchService;

import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.infrastructure.LogicServerDto;
import com.abiquo.server.core.infrastructure.network.IpPoolManagementDto;
import com.abiquo.server.core.infrastructure.storage.StoragePoolDto;
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

    /*********************** Enterprise ***********************/

    @Override
    public Iterable<Enterprise> searchEnterprises(final EnterpriseOptions options)
    {
        List<EnterpriseDto> enterprises =
            context.getApi().getEnterpriseClient().listEnterprises(options).getCollection();

        return wrap(context, Enterprise.class, enterprises);
    }

    @Override
    public Iterable<Enterprise> searchEnterprisesUsingDatacenter(final Datacenter datacenter,
        final EnterpriseOptions options)
    {
        List<EnterpriseDto> enterprises =
            context.getApi().getEnterpriseClient().listEnterprises(datacenter.unwrap(), options)
                .getCollection();

        return wrap(context, Enterprise.class, enterprises);
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

    /*********************** Storage Pool ***********************/

    @Override
    public List<StoragePool> searchStoragePools(final StorageDevice device,
        final StoragePoolOptions options)
    {
        List<StoragePoolDto> pools =
            context.getApi().getInfrastructureClient().listStoragePools(device.unwrap(), options)
                .getCollection();

        return wrap(context, StoragePool.class, pools);
    }

    /*********************** Private Network ***********************/

    @Override
    public Iterable<Ip> searchPrivateIps(final PrivateNetwork network, final IpOptions options)
    {
        List<IpPoolManagementDto> ips =
            context.getApi().getCloudClient().listPrivateNetworkIps(network.unwrap(), options)
                .getCollection();

        return wrap(context, Ip.class, ips);
    }

    @Override
    public Iterable<Ip> searchPublicIpsToPurchase(final VirtualDatacenter virtualDatacenter,
        final IpOptions options)
    {
        List<IpPoolManagementDto> ips =
            context.getApi().getCloudClient()
                .listAvailablePublicIps(virtualDatacenter.unwrap(), options).getCollection();

        return wrap(context, Ip.class, ips);
    }

    @Override
    public Iterable<Ip> searchPurchasedPublicIps(final VirtualDatacenter virtualDatacenter,
        final IpOptions options)
    {
        List<IpPoolManagementDto> ips =
            context.getApi().getCloudClient()
                .listPurchasedPublicIps(virtualDatacenter.unwrap(), options).getCollection();

        return wrap(context, Ip.class, ips);
    }

    @Override
    public Iterable<LogicServer> searchServiceProfiles(final ManagedRack rack,
        final FilterOptions options)
    {
        List<LogicServerDto> profiles =
            context.getApi().getInfrastructureClient().listServiceProfiles(rack.unwrap(), options)
                .getCollection();

        return wrap(context, LogicServer.class, profiles);
    }

    @Override
    public Iterable<LogicServer> searchServiceProfileTemplates(final ManagedRack rack,
        final FilterOptions options)
    {
        List<LogicServerDto> profiles =
            context.getApi().getInfrastructureClient()
                .listServiceProfileTemplates(rack.unwrap(), options).getCollection();

        return wrap(context, LogicServer.class, profiles);
    }

}
