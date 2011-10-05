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

package org.jclouds.abiquo.strategy.infrastructure.internal;

import static com.google.common.collect.Iterables.filter;
import static org.jclouds.abiquo.domain.DomainWrapper.wrap;

import java.util.concurrent.ExecutorService;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.Rack;
import org.jclouds.abiquo.strategy.infrastructure.ListRacks;

import com.abiquo.server.core.infrastructure.RacksDto;
import com.google.common.base.Predicate;
import com.google.inject.Inject;

/**
 * List racks.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Singleton
public class ListRacksImpl implements ListRacks
{
    protected final AbiquoContext context;

    protected final ExecutorService userExecutor;

    @Inject(optional = true)
    @Named(Constants.PROPERTY_REQUEST_TIMEOUT)
    protected Long maxTime;

    @Inject
    ListRacksImpl(@Named(Constants.PROPERTY_USER_THREADS) final ExecutorService userExecutor,
        final AbiquoContext context)
    {
        this.userExecutor = userExecutor;
        this.context = context;
    }

    @Override
    public Iterable<Rack> execute(final Datacenter datacenter)
    {
        RacksDto result = context.getApi().getInfrastructureClient().listRacks(datacenter.getId());
        return wrap(context, Rack.class, result.getCollection());
    }

    @Override
    public Iterable<Rack> execute(final Datacenter datacenter, final Predicate<Rack> selector)
    {
        return filter(execute(datacenter), selector);
    }
}
