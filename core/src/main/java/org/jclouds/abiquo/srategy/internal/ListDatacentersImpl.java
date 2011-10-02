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

package org.jclouds.abiquo.srategy.internal;

import static com.google.common.collect.Iterables.filter;

import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.reference.AbiquoConstants;
import org.jclouds.abiquo.srategy.ListDatacenters;
import org.jclouds.logging.Logger;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.google.common.base.Predicate;
import com.google.inject.Inject;

/**
 * List datacenters.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class ListDatacentersImpl implements ListDatacenters
{
    protected final AbiquoClient abiquoClient;

    protected final AbiquoAsyncClient abiquoAsyncClient;

    protected final ExecutorService userExecutor;

    @Resource
    @Named(AbiquoConstants.ABIQUO_LOGGER)
    protected Logger logger = Logger.NULL;

    @Inject(optional = true)
    @Named(Constants.PROPERTY_REQUEST_TIMEOUT)
    protected Long maxTime;

    @Inject
    ListDatacentersImpl(@Named(Constants.PROPERTY_USER_THREADS) ExecutorService userExecutor,
        AbiquoClient client, AbiquoAsyncClient asyncClient)
    {
        this.userExecutor = userExecutor;
        this.abiquoClient = client;
        this.abiquoAsyncClient = asyncClient;
    }

    @Override
    public Iterable<DatacenterDto> execute()
    {
        return abiquoClient.listDatacenters().getCollection();
    }

    @Override
    public Iterable<DatacenterDto> execute(Predicate<DatacenterDto> selector)
    {
        return filter(execute(), selector);
    }

}
