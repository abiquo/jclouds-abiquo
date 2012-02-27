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

package org.jclouds.abiquo.domain.options.search;

import org.jclouds.abiquo.domain.options.QueryOptions;

/**
 * Available options to filter and pagination methods.
 * 
 * @author Francesc Montserrat
 */
public class FilterOptions extends QueryOptions
{
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        FilterOptions options = new FilterOptions();
        options.map.putAll(map);
        return options;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    public String toString()
    {
        return this.map.toString();
    }

    public static class Builder extends QueryOptionsBuilder<Builder>
    {
        @Override
        public FilterOptions build()
        {
            FilterOptions options = new FilterOptions();

            // Add FilterOptions options
            options.map.putAll(super.build().getOptions());

            return options;
        }
    }
}
