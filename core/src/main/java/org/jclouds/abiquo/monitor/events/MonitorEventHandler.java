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
package org.jclouds.abiquo.monitor.events;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.abiquo.monitor.MonitorCallback;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

/**
 * base class for monitor event handlers.
 * 
 * @author Ignasi Barrera
 */
public abstract class MonitorEventHandler<T>
{
    /** The callback to be invoked when an event is notified. */
    private MonitorCallback<T> callback;

    public MonitorEventHandler(final MonitorCallback<T> callback)
    {
        super();
        this.callback = checkNotNull(callback, "callback");
    }

    /**
     * Checks if the current handler must handle the incoming event.
     */
    protected abstract boolean handles(final MonitorEvent<T> event);

    @AllowConcurrentEvents
    @Subscribe
    public void handle(final MonitorEvent<T> event)
    {
        switch (event.getType())
        {
            case COMPLETED:
                callback.onCompleted(event.getTarget());
                break;
            case FAILED:
                callback.onFailed(event.getTarget());
                break;
            case TIMEOUT:
                callback.onTimeout(event.getTarget());
                break;
        }
    }
}
