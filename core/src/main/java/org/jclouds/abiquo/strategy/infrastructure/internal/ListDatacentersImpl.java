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

import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.strategy.infrastructure.ListDatacenters;

import com.abiquo.server.core.infrastructure.DatacentersDto;
import com.google.common.base.Predicate;
import com.google.inject.Inject;

/**
 * List datacenters.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Singleton
public class ListDatacentersImpl implements ListDatacenters
{
    // This strategy does not have still an Executor instance because the current methods call
    // single client methods

    protected final AbiquoContext context;

    @Inject
    ListDatacentersImpl(final AbiquoContext context)
    {
        this.context = context;
    }

    @Override
    public Iterable<Datacenter> execute()
    {
        DatacentersDto result = context.getApi().getInfrastructureClient().listDatacenters();
        return wrap(context, Datacenter.class, result.getCollection());
    }

    @Override
    public Iterable<Datacenter> execute(final Predicate<Datacenter> selector)
    {
        return filter(execute(), selector);
    }

}
