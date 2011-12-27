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

package org.jclouds.abiquo.reference;

import org.jclouds.abiquo.features.services.MonitoringService;

/**
 * Global constants used in the Abiquo provider.
 * 
 * @author Ignasi Barrera
 */
public interface AbiquoConstants
{
    /**
     * The delay (in ms) used between requests by the {@link MonitoringService} when monitoring
     * asynchronous task state.
     * <p>
     * Default value: 5000 ms
     */
    public static final String ASYNC_TASK_MONITOR_DELAY = "abiquo.monitor-delay";

    /**
     * The maximum number of scheduler threads used to perform periodical tasks.
     * <p>
     * Default value: 5 (a 0 value will configure a single threaded executor service that will not
     * be able to execute tasks concurrently).
     */
    public static final String MAX_SCHEDULER_THREADS = "abiquo.scheduler-max-threads";
}
