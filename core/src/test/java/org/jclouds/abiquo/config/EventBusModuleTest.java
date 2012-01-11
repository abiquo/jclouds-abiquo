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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jclouds.abiquo.config.annotations.AsyncBus;
import org.jclouds.abiquo.internal.BaseInjectionTest;
import org.testng.annotations.Test;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.Key;

/**
 * Unit tests for the {@link EventBusModule} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class EventBusModuleTest extends BaseInjectionTest
{
    public void testAsyncExecutorIsProvided()
    {
        assertNotNull(injector.getInstance(AsyncEventBus.class));
    }

    public void testAsyncAnnotatedEventBusIsBound()
    {
        Key<EventBus> eventBusKey = Key.get(EventBus.class, AsyncBus.class);
        EventBus eventBus = injector.getInstance(eventBusKey);

        assertNotNull(eventBus);
        assertTrue(eventBus instanceof AsyncEventBus);
    }

    public void testEventBusIsSingleton()
    {
        EventBus eventBus1 = injector.getInstance(EventBus.class);
        EventBus eventBus2 = injector.getInstance(EventBus.class);

        assertTrue(eventBus1 == eventBus2);
    }
}
