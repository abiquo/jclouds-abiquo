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

package org.jclouds.abiquo.domain.network.options;

import org.jclouds.abiquo.domain.options.QueryOptions;
import org.jclouds.abiquo.domain.options.QueryOptions.QueryOptionsBuilder;

/**
 * Available options to query ips.
 * 
 * @author Francesc Montserrat
 */
public class IpOptions extends QueryOptions
{
    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        IpOptions options = new IpOptions();
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
        private Boolean free;

        public Builder free(final boolean free)
        {
            this.free = free;
            return this;
        }

        @Override
        public IpOptions build()
        {
            IpOptions options = new IpOptions();

            if (free != null)
            {
                options.map.put("free", String.valueOf(free));
            }

            // Add FilterOptions options
            options.map.putAll(super.build().getOptions());

            return options;
        }
    }
}
