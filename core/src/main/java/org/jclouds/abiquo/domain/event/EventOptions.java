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
package org.jclouds.abiquo.domain.event;

import java.util.HashMap;
import java.util.Map.Entry;

import org.jclouds.abiquo.domain.options.QueryOptions;

/**
 * Available options to query events.
 * 
 * @author Vivien Mahé
 */
public class EventOptions extends QueryOptions
{
    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        EventOptions options = new EventOptions();
        options.map.putAll(map);
        return options;
    }

    @Override
    public String toString()
    {
        return this.map.toString();
    }

    public static class Builder extends QueryOptionsBuilder<Builder>
    {
        private HashMap<String, String> filters;

        public Builder filters(final HashMap<String, String> filters)
        {
            this.filters = filters;
            return this;
        }

        @Override
        public EventOptions build()
        {
            EventOptions options = new EventOptions();

            if (filters != null)
            {
                for (Entry<String, String> filter : filters.entrySet())
                {
                    options.map.put(filter.getKey(), filter.getValue());
                }
            }

            // Add FilterOptions options
            options.map.putAll(super.build().getOptions());

            return options;
        }
    }
}
