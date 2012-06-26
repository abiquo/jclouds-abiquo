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

package org.jclouds.abiquo.domain.options;

import org.jclouds.abiquo.domain.options.search.FilterOptions;
import org.jclouds.abiquo.domain.options.search.reference.OrderBy;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

/**
 * Base class for all query parameter based options.
 * 
 * @author Francesc Montserrat
 */
public abstract class QueryOptions
{
    /** The option map. */
    protected Multimap<String, String> map = LinkedListMultimap.create();

    /**
     * Gets the option map.
     */
    public Multimap<String, String> getOptions()
    {
        return map;
    }

    public static class QueryOptionsBuilder<T extends QueryOptionsBuilder<T>>
    {
        protected Integer startWith;

        protected Integer limit;

        protected OrderBy by;

        protected String has;

        protected Boolean asc;

        @SuppressWarnings("unchecked")
        public T startWith(final int startWith)
        {
            this.startWith = startWith;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T has(final String has)
        {
            this.has = has;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T limit(final int limit)
        {
            this.limit = limit;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T orderBy(final OrderBy by)
        {
            this.by = by;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T ascendant(final boolean asc)
        {
            this.asc = asc;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T descendant(final boolean desc)
        {
            this.asc = !desc;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T disablePagination()
        {
            this.limit = 0;
            return (T) this;
        }

        public QueryOptions build()
        {
            FilterOptions options = new FilterOptions();
            if (startWith != null)
            {
                options.map.put("startwith", startWith.toString());
            }

            if (limit != null)
            {
                options.map.put("limit", limit.toString());
            }

            if (has != null)
            {
                options.map.put("has", has);
            }

            if (by != null)
            {
                options.map.put("by", by.getValue());
            }

            if (asc != null)
            {
                options.map.put("asc", asc.toString());
            }

            return options;
        }
    }
}
