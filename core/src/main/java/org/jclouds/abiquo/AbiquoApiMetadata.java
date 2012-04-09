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
package org.jclouds.abiquo;

import java.net.URI;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.apis.ApiType;
import org.jclouds.apis.BaseApiMetadata;

/**
 * Implementation of {@link ApiMetadata} for Abiquo API.
 * 
 * @author Ignasi Barrera
 */
public class AbiquoApiMetadata extends BaseApiMetadata
{
    public AbiquoApiMetadata()
    {
        this(builder().id("abiquo").type(ApiType.COMPUTE).name("Abiquo API")
            .identityName("API Username").credentialName("API Password")
            .documentation(URI.create("http://community.abiquo.com")));
    }

    // below are so that we can reuse builders, toString, hashCode, etc.
    // we have to set concrete classes here, as our base class cannot be
    // concrete due to serviceLoader
    protected AbiquoApiMetadata(final Builder< ? > builder)
    {
        super(builder);
    }

    private static class ConcreteBuilder extends Builder<ConcreteBuilder>
    {

        @Override
        public AbiquoApiMetadata build()
        {
            return new AbiquoApiMetadata(this);
        }
    }

    public static ConcreteBuilder builder()
    {
        return new ConcreteBuilder();
    }

    @Override
    public ConcreteBuilder toBuilder()
    {
        return builder().fromApiMetadata(this);
    }
}
