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

package org.jclouds.abiquo.domain.cloud.options;

import org.jclouds.abiquo.domain.options.QueryOptions;

import com.abiquo.model.enumerator.HypervisorType;

/**
 * Available options to query virtual machine templates.
 * 
 * @author Ignasi Barrera
 */
public class VirtualMachineTemplateOptions extends QueryOptions
{
    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        VirtualMachineTemplateOptions options = new VirtualMachineTemplateOptions();
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
        private Boolean persistent;

        private HypervisorType hypervisorType;

        private String category;

        public Builder persistent(final Boolean persistent)
        {
            this.persistent = persistent;
            return this;
        }

        public Builder hypervisorType(final HypervisorType hypervisorType)
        {
            this.hypervisorType = hypervisorType;
            return this;
        }

        public Builder category(final String category)
        {
            this.category = category;
            return this;
        }

        public VirtualMachineTemplateOptions build()
        {
            VirtualMachineTemplateOptions options = new VirtualMachineTemplateOptions();

            if (persistent != null)
            {
                options.map.put("stateful", String.valueOf(persistent));
            }
            if (hypervisorType != null)
            {
                options.map.put("hypervisorTypeName", hypervisorType.name());
            }
            if (category != null)
            {
                options.map.put("categoryName", category);
            }

            return options;
        }
    }
}
