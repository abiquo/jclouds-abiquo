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

package org.jclouds.abiquo.domain.config.options;

import org.jclouds.abiquo.domain.options.QueryOptions;

/**
 * Available options to query system properties.
 * 
 * @author Francesc Montserrat
 */
public class PropertyOptions extends QueryOptions
{
    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        PropertyOptions options = new PropertyOptions();
        options.map.putAll(map);
        return options;
    }

    @Override
    public String toString()
    {
        return this.map.toString();
    }

    public static class Builder
    {
        private String component;

        private String name;

        public Builder component(final String component)
        {
            this.component = component;
            return this;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public PropertyOptions build()
        {
            PropertyOptions options = new PropertyOptions();
            if (component != null)
            {
                options.map.put("component", component.toString());
            }

            if (name != null)
            {
                options.map.put("name", name.toString());
            }
            return options;
        }
    }
}
