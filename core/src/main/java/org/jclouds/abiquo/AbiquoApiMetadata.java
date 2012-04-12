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

import static org.jclouds.Constants.PROPERTY_MAX_REDIRECTS;
import static org.jclouds.abiquo.reference.AbiquoConstants.ASYNC_TASK_MONITOR_DELAY;
import static org.jclouds.abiquo.reference.AbiquoConstants.MAX_SCHEDULER_THREADS;

import java.net.URI;
import java.util.Properties;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.apis.ApiType;
import org.jclouds.compute.internal.BaseComputeServiceApiMetadata;

import com.google.common.base.CharMatcher;
import com.google.common.reflect.TypeToken;

/**
 * Implementation of {@link ApiMetadata} for Abiquo API.
 * 
 * @author Ignasi Barrera
 */
public class AbiquoApiMetadata
    extends
    BaseComputeServiceApiMetadata<AbiquoClient, AbiquoAsyncClient, AbiquoContext, AbiquoApiMetadata>
{
    public AbiquoApiMetadata()
    {
        this(builder());
    }

    protected AbiquoApiMetadata(final Builder builder)
    {
        super(builder);
    }

    protected static Properties defaultProperties()
    {
        Properties properties =
            org.jclouds.apis.internal.BaseApiMetadata.Builder.defaultProperties();
        // By default redirects will be handled in the domain objects
        properties.setProperty(PROPERTY_MAX_REDIRECTS, "0");
        // The default polling delay between AsyncTask monitor requests
        properties.setProperty(ASYNC_TASK_MONITOR_DELAY, "5000");
        // The default number of concurrent scheduler threads to be used
        properties.setProperty(MAX_SCHEDULER_THREADS, "10");
        return properties;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    public Builder toBuilder()
    {
        return builder().fromApiMetadata(this);
    }

    public static class Builder
        extends
        BaseComputeServiceApiMetadata.Builder<AbiquoClient, AbiquoAsyncClient, AbiquoContext, AbiquoApiMetadata>
    {
        private static final String DOCUMENTATION_ROOT = "http://community.abiquo.com/display/ABI"
            + CharMatcher.DIGIT.retainFrom(AbiquoAsyncClient.API_VERSION);

        protected Builder()
        {
            id("abiquo").name("Abiquo API").type(ApiType.COMPUTE).identityName("API Username")
                .credentialName("API Password")
                .documentation(URI.create(DOCUMENTATION_ROOT + "/API+Reference"))
                .defaultEndpoint("http://localhost/api").version(AbiquoAsyncClient.API_VERSION)
                .buildVersion(AbiquoAsyncClient.BUILD_VERSION)
                .defaultProperties(AbiquoApiMetadata.defaultProperties())
                .javaApi(AbiquoClient.class, AbiquoAsyncClient.class)
                .context(TypeToken.of(AbiquoContext.class))
                .contextBuilder(TypeToken.of(AbiquoContextBuilder.class));
        }

        public Builder useTokenAuth()
        {
            // Token auth has the credential coded in the token, so we should not ask for it
            return (Builder) this.credentialName(null);
        }

        public Builder useBase64Auth()
        {
            return (Builder) this.credentialName("API Password");
        }

        @Override
        public AbiquoApiMetadata build()
        {
            return new AbiquoApiMetadata(this);
        }

        @Override
        public Builder fromApiMetadata(final AbiquoApiMetadata in)
        {
            super.fromApiMetadata(in);
            return this;
        }
    }

}
