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

package org.jclouds.abiquo.config;

import java.util.concurrent.ExecutorService;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.concurrent.config.ExecutorServiceModule;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Configures the {@link EventBus} to be used in the platform.
 * <p>
 * This class will provide a {@link AsyncEventBus} to be used to provide a basic pub/sub system for
 * asynchronous operations.
 * 
 * @author Ignasi Barrera
 * @see ExecutorServiceModule
 * @see AsyncEventBus
 * @see EventBus
 */
@ConfiguresEventBus
public class EventBusModule extends AbstractModule
{
    /**
     * Provides an {@link AsyncEventBus} that will use the configured executor service to dispatch
     * events to subscribers.
     */
    @Provides
    @Singleton
    AsyncEventBus provideEventBus(
        @Named(Constants.PROPERTY_USER_THREADS) final ExecutorService executor)
    {
        return new AsyncEventBus("abiquo-event-bus", executor);
    }

    @Override
    protected void configure()
    {

    }

}
