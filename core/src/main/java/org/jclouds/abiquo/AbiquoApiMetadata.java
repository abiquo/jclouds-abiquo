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

import org.jclouds.abiquo.compute.config.AbiquoComputeServiceContextModule;
import org.jclouds.abiquo.config.AbiquoRestClientModule;
import org.jclouds.abiquo.config.SchedulerModule;
import org.jclouds.apis.ApiMetadata;
import org.jclouds.apis.internal.BaseApiMetadata;
import org.jclouds.rest.RestContext;
import org.jclouds.rest.internal.BaseRestApiMetadata;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import com.google.inject.Module;

/**
 * Implementation of {@link ApiMetadata} for Abiquo API.
 * 
 * @author Ignasi Barrera
 */
public class AbiquoApiMetadata extends BaseApiMetadata
{
    /** Serial UID. */
    private static final long serialVersionUID = -8355533493674898171L;

    public static final TypeToken<RestContext<AbiquoClient, AbiquoAsyncClient>> CONTEXT_TOKEN =
        new TypeToken<RestContext<AbiquoClient, AbiquoAsyncClient>>()
        {
            private static final long serialVersionUID = -5070937833892503232L;
        };

    public AbiquoApiMetadata()
    {
        this(new Builder());
    }

    protected AbiquoApiMetadata(final Builder builder)
    {
        super(builder);
    }

    public static Properties defaultProperties()
    {
        Properties properties = BaseRestApiMetadata.defaultProperties();
        // By default redirects will be handled in the domain objects
        properties.setProperty(PROPERTY_MAX_REDIRECTS, "0");
        // The default polling delay between AsyncTask monitor requests
        properties.setProperty(ASYNC_TASK_MONITOR_DELAY, "5000");
        // The default number of concurrent scheduler threads to be used
        properties.setProperty(MAX_SCHEDULER_THREADS, "10");
        return properties;
    }

    @Override
    public Builder toBuilder()
    {
        return new Builder().fromApiMetadata(this);
    }

    public static class Builder extends BaseRestApiMetadata.Builder
    {
        private static final String DOCUMENTATION_ROOT = "http://community.abiquo.com/display/ABI"
            + CharMatcher.DIGIT.retainFrom(AbiquoAsyncClient.API_VERSION);

        protected Builder()
        {
            super(AbiquoClient.class, AbiquoAsyncClient.class);
            id("abiquo")
                .name("Abiquo API")
                .identityName("API Username")
                .credentialName("API Password")
                .documentation(URI.create(DOCUMENTATION_ROOT + "/API+Reference"))
                .defaultEndpoint("http://localhost/api")
                .version(AbiquoAsyncClient.API_VERSION)
                .buildVersion(AbiquoAsyncClient.BUILD_VERSION)
                .wrapper(TypeToken.of(AbiquoContext.class))
                .defaultProperties(AbiquoApiMetadata.defaultProperties())
                .defaultModules(
                    ImmutableSet.<Class< ? extends Module>> of(AbiquoRestClientModule.class,
                        AbiquoComputeServiceContextModule.class, SchedulerModule.class));
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
        public Builder fromApiMetadata(final ApiMetadata in)
        {
            super.fromApiMetadata(in);
            return this;
        }
    }

}
