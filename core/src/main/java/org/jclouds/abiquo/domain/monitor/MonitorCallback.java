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

package org.jclouds.abiquo.domain.monitor;

import org.jclouds.abiquo.features.services.MonitoringService;

/**
 * Callback that will be executed when a monitored object is in the complete state.
 * 
 * @author Ignasi Barrera
 * @see MonitoringService
 */
public interface MonitorCallback<T>
{
    /**
     * Callback method to be executed when the monitor completes without errors.
     * 
     * @param object The completed monitored object.
     */
    public void onCompleted(T object);

    /**
     * Callback method to be executed when the monitor completes with errors.
     * 
     * @param object The failed monitored object.
     */
    public void onFailed(T object);

    /**
     * Callback method to be executed when the monitor reaches the timeout.
     * 
     * @param object The monitored object.
     */
    public void onTimeout(T object);
}
