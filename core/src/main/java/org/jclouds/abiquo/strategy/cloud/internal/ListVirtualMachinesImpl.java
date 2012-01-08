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
package org.jclouds.abiquo.strategy.cloud.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.filter;
import static org.jclouds.abiquo.domain.DomainWrapper.wrap;
import static org.jclouds.concurrent.FutureIterables.transformParallel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.strategy.cloud.ListVirtualMachines;
import org.jclouds.logging.Logger;

import com.abiquo.model.transport.SingleResourceTransportDto;
import com.abiquo.model.transport.WrapperDto;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualAppliancesDto;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.abiquo.server.core.cloud.VirtualMachinesDto;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * List virtual machines in each virtual datacenter and each virtual appliance.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class ListVirtualMachinesImpl implements ListVirtualMachines
{
    protected AbiquoContext context;

    protected final ExecutorService userExecutor;

    @Resource
    protected Logger logger = Logger.NULL;

    @Inject(optional = true)
    @Named(Constants.PROPERTY_REQUEST_TIMEOUT)
    protected Long maxTime;

    @Inject
    ListVirtualMachinesImpl(AbiquoContext context,
        @Named(Constants.PROPERTY_USER_THREADS) ExecutorService userExecutor)
    {
        super();
        this.context = checkNotNull(context, "context");
        this.userExecutor = checkNotNull(userExecutor, "userExecutor");
    }

    @Override
    public Iterable<VirtualMachine> execute()
    {
        // Find virtual machines in concurrent requests
        Iterable<VirtualDatacenter> vdcs = context.getCloudService().listVirtualDatacenters();
        Iterable<VirtualApplianceDto> vapps = listConcurrentVirtualAppliances(vdcs);
        Iterable<VirtualMachineDto> vms = listConcurrentVirtualMachines(vapps);

        return wrap(context, VirtualMachine.class, vms);
    }

    @Override
    public Iterable<VirtualMachine> execute(Predicate<VirtualMachine> selector)
    {
        return filter(execute(), selector);
    }

    private Iterable<VirtualApplianceDto> listConcurrentVirtualAppliances(
        Iterable<VirtualDatacenter> vdcs)
    {
        Iterable<VirtualAppliancesDto> vapps =
            transformParallel(vdcs, new Function<VirtualDatacenter, Future<VirtualAppliancesDto>>()
            {
                @Override
                public Future<VirtualAppliancesDto> apply(VirtualDatacenter input)
                {
                    return context.getAsyncApi().getCloudClient()
                        .listVirtualAppliances(input.unwrap());
                }
            }, userExecutor, maxTime, logger, "getting virtual appliances");

        return join(vapps);
    }

    private Iterable<VirtualMachineDto> listConcurrentVirtualMachines(
        Iterable<VirtualApplianceDto> vapps)
    {
        Iterable<VirtualMachinesDto> vms =
            transformParallel(vapps,
                new Function<VirtualApplianceDto, Future<VirtualMachinesDto>>()
                {
                    @Override
                    public Future<VirtualMachinesDto> apply(VirtualApplianceDto input)
                    {
                        return context.getAsyncApi().getCloudClient().listVirtualMachines(input);
                    }
                }, userExecutor, maxTime, logger, "getting virtual machines");

        return join(vms);
    }

    private static <T extends SingleResourceTransportDto> Iterable<T> join(
        Iterable< ? extends WrapperDto<T>> collection)
    {
        List<T> dtos = Lists.newLinkedList();
        for (WrapperDto<T> wrapper : collection)
        {
            dtos.addAll(wrapper.getCollection());
        }
        return dtos;
    }

}
