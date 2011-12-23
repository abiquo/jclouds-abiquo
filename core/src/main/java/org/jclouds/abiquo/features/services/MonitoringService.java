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

package org.jclouds.abiquo.features.services;

import org.jclouds.abiquo.domain.task.AsyncJob;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.domain.task.AsyncTaskCallback;
import org.jclouds.abiquo.internal.BaseMonitoringService;

import com.google.inject.ImplementedBy;

/**
 * Utility service to monitor {@link AsyncTask} and {@link AsyncJob} progress.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@ImplementedBy(BaseMonitoringService.class)
public interface MonitoringService
{
    /**
     * Monitors the given task and blocks until it completes.
     * 
     * @param task The task to monitor.
     */
    void awaitCompletion(final AsyncTask task);

    /**
     * Monitors the given task.
     * 
     * @param task The task to monitor.
     * @param callback The callback to execute when the task completes (with or without errors).
     */
    void monitor(final AsyncTask task, final AsyncTaskCallback callback);
}
