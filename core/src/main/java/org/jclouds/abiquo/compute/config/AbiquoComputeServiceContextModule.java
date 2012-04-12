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
package org.jclouds.abiquo.compute.config;

import static org.jclouds.Constants.PROPERTY_SESSION_INTERVAL;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.compute.functions.DatacenterToLocation;
import org.jclouds.abiquo.compute.functions.VirtualMachineTemplateToHardware;
import org.jclouds.abiquo.compute.functions.VirtualMachineTemplateToImage;
import org.jclouds.abiquo.compute.functions.VirtualMachineToNodeMetadata;
import org.jclouds.abiquo.compute.options.AbiquoTemplateOptions;
import org.jclouds.abiquo.compute.strategy.AbiquoComputeServiceAdapter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.suppliers.GetCurrentEnterprise;
import org.jclouds.abiquo.suppliers.GetCurrentUser;
import org.jclouds.collect.Memoized;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.config.ComputeServiceAdapterContextModule;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.Location;
import org.jclouds.location.suppliers.ImplicitLocationSupplier;
import org.jclouds.location.suppliers.implicit.OnlyLocationOrFirstZone;
import org.jclouds.rest.AuthorizationException;
import org.jclouds.rest.suppliers.MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

/**
 * Abiquo Compute service configuration module.
 * 
 * @author Ignasi Barrera
 */
public class AbiquoComputeServiceContextModule
    extends
    ComputeServiceAdapterContextModule<AbiquoClient, AbiquoAsyncClient, VirtualMachine, VirtualMachineTemplate, VirtualMachineTemplate, Datacenter>
{
    public AbiquoComputeServiceContextModule()
    {
        super(AbiquoClient.class, AbiquoAsyncClient.class);
    }

    @Override
    protected void configure()
    {
        super.configure();
        bind(
            new TypeLiteral<ComputeServiceAdapter<VirtualMachine, VirtualMachineTemplate, VirtualMachineTemplate, Datacenter>>()
            {
            }).to(AbiquoComputeServiceAdapter.class);
        bind(new TypeLiteral<Function<VirtualMachine, NodeMetadata>>()
        {
        }).to(VirtualMachineToNodeMetadata.class);
        bind(new TypeLiteral<Function<VirtualMachineTemplate, Image>>()
        {
        }).to(VirtualMachineTemplateToImage.class);
        bind(new TypeLiteral<Function<VirtualMachineTemplate, Hardware>>()
        {
        }).to(VirtualMachineTemplateToHardware.class);
        bind(new TypeLiteral<Function<Datacenter, Location>>()
        {
        }).to(DatacenterToLocation.class);
        bind(ImplicitLocationSupplier.class).to(OnlyLocationOrFirstZone.class).in(Scopes.SINGLETON);
        bind(TemplateOptions.class).to(AbiquoTemplateOptions.class);
        install(new LocationsFromComputeServiceAdapterModule<VirtualMachine, VirtualMachineTemplate, VirtualMachineTemplate, Datacenter>()
        {
        });
    }

    @Provides
    @Singleton
    @Memoized
    public Supplier<User> getCurrentUser(
        final AtomicReference<AuthorizationException> authException,
        @Named(PROPERTY_SESSION_INTERVAL) final long seconds, final GetCurrentUser getCurrentUser)
    {
        return new MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier<User>(authException,
            seconds,
            getCurrentUser);
    }

    @Provides
    @Singleton
    @Memoized
    public Supplier<Enterprise> getCurrentEnterprise(
        final AtomicReference<AuthorizationException> authException,
        @Named(PROPERTY_SESSION_INTERVAL) final long seconds,
        final GetCurrentEnterprise getCurrentEnterprise)
    {
        return new MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier<Enterprise>(authException,
            seconds,
            getCurrentEnterprise);
    }

}
