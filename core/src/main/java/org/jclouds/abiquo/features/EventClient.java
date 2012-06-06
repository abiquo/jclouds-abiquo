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
package org.jclouds.abiquo.features;

import java.util.concurrent.TimeUnit;

import org.jclouds.abiquo.domain.options.QueryOptions;
import org.jclouds.concurrent.Timeout;

import com.abiquo.server.core.event.EventsDto;

/**
 * Provides synchronous access to Abiquo Event API.
 * 
 * @see API: <a href="http://community.abiquo.com/display/ABI20/API+Reference">
 *      http://community.abiquo.com/display/ABI20/API+Reference</a>
 * @see EventAsyncClient
 * @author Ignasi Barrera
 * @author Vivien Mahé
 */
@Timeout(duration = 30, timeUnit = TimeUnit.SECONDS)
public interface EventClient
{
    /**
     * List events.
     * 
     * @return The list of events.
     */
    EventsDto listEvents();

    /**
     * List events using filters.
     * 
     * @return The list of events using filters.
     */
    EventsDto listEvents(QueryOptions options);
}