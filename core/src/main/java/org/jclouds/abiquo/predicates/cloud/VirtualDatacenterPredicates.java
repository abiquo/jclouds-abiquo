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

package org.jclouds.abiquo.predicates.cloud;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;

import com.abiquo.model.enumerator.HypervisorType;
import com.google.common.base.Predicate;

/**
 * Container for {@link VirtualDatacenter} filters.
 * 
 * @author Ignasi Barrera
 */
public class VirtualDatacenterPredicates
{
    public static Predicate<VirtualDatacenter> name(final String... names)
    {
        checkNotNull(names, "names must be defined");

        return new Predicate<VirtualDatacenter>()
        {
            @Override
            public boolean apply(final VirtualDatacenter virtualDatacenter)
            {
                return Arrays.asList(names).contains(virtualDatacenter.getName());
            }
        };
    }

    public static Predicate<VirtualDatacenter> type(final HypervisorType... types)
    {
        checkNotNull(types, "types must be defined");

        return new Predicate<VirtualDatacenter>()
        {
            @Override
            public boolean apply(final VirtualDatacenter virtualDatacenter)
            {
                return Arrays.asList(types).contains(virtualDatacenter.getHypervisorType());
            }
        };
    }
}
