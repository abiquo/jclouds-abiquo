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

package org.jclouds.abiquo;

import java.lang.annotation.Annotation;
import java.util.List;

import org.jclouds.abiquo.compute.config.AbiquoComputeServiceContextModule;
import org.jclouds.abiquo.config.AbiquoRestClientModule;
import org.jclouds.abiquo.config.ConfiguresScheduler;
import org.jclouds.abiquo.config.SchedulerModule;
import org.jclouds.compute.ComputeServiceContextBuilder;
import org.jclouds.providers.ProviderMetadata;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Builds the Abiquo context.
 * 
 * @author Ignasi Barrera
 */
public class AbiquoContextBuilder extends
    ComputeServiceContextBuilder<AbiquoClient, AbiquoAsyncClient, AbiquoContext, AbiquoApiMetadata>
{
    public AbiquoContextBuilder(
        final ProviderMetadata<AbiquoClient, AbiquoAsyncClient, AbiquoContext, AbiquoApiMetadata> providerMetadata)
    {
        super(providerMetadata);
    }

    public AbiquoContextBuilder(final AbiquoApiMetadata apiMetadata)
    {
        super(apiMetadata);
    }

    @Override
    protected void addClientModule(final List<Module> modules)
    {
        modules.add(new AbiquoRestClientModule());
    }

    @Override
    protected void addContextModule(final List<Module> modules)
    {
        modules.add(new AbiquoComputeServiceContextModule());
    }

    @Override
    public Injector buildInjector()
    {
        if (!isModulePresent(ConfiguresScheduler.class))
        {
            modules.add(new SchedulerModule());
        }

        return super.buildInjector();
    }

    private boolean isModulePresent(final Class< ? extends Annotation> annotatedWith)
    {
        return Iterables.any(modules, new Predicate<Module>()
        {
            @Override
            public boolean apply(final Module input)
            {
                return input.getClass().isAnnotationPresent(annotatedWith);
            }

        });
    }

}
