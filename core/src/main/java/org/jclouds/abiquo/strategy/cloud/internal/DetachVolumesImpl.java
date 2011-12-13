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

import static com.google.common.collect.Maps.newHashMap;
import static org.jclouds.concurrent.FutureIterables.awaitCompletion;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.Volume;
import org.jclouds.abiquo.features.CloudAsyncClient;
import org.jclouds.abiquo.reference.AbiquoConstants;
import org.jclouds.abiquo.strategy.cloud.DetachVolumes;
import org.jclouds.logging.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * @author Ignasi Barrera
 */
@Singleton
public class DetachVolumesImpl implements DetachVolumes
{
    protected final ExecutorService userExecutor;

    protected final AbiquoContext context;

    @Resource
    @Named(AbiquoConstants.ABIQUO_LOGGER)
    protected Logger logger = Logger.NULL;

    @Inject(optional = true)
    @Named(Constants.PROPERTY_REQUEST_TIMEOUT)
    protected Long maxTime;

    @Inject
    DetachVolumesImpl(@Named(Constants.PROPERTY_USER_THREADS) final ExecutorService userExecutor,
        final AbiquoContext context)
    {
        this.userExecutor = userExecutor;
        this.context = context;
    }

    @Override
    public void execute(final VirtualMachine virtualMachine, final Iterable<Volume> volumes)
    {
        Map<String, Exception> exceptions = newHashMap();
        Map<String, Future< ? >> responses = newHashMap();
        CloudAsyncClient asyncClient = context.getAsyncApi().getCloudClient();

        Iterable<String> names = Iterables.transform(volumes, new Function<Volume, String>()
        {
            @Override
            public String apply(final Volume input)
            {
                return input.getName();
            }
        });

        for (Volume volume : volumes)
        {
            responses.put(volume.getName(),
                asyncClient.detachVolume(virtualMachine.unwrap(), volume.unwrap()));
        }

        exceptions =
            awaitCompletion(responses, userExecutor, maxTime, logger,
                String.format("Detaching volumes from VM %s: %s", virtualMachine.getName(), names));

        if (exceptions.size() > 0)
        {
            throw new RuntimeException(String.format("Errors detaching volumes from VM %s: %s: %s",
                virtualMachine.getName(), names, exceptions));
        }
    }
}
