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

import static org.jclouds.rest.RestContextFactory.createContextBuilder;

import java.util.Properties;

import javax.annotation.Nullable;

import org.jclouds.rest.RestContextBuilder;
import org.jclouds.rest.RestContextFactory;
import org.jclouds.rest.RestContextSpec;
import org.jclouds.util.Throwables2;

import com.google.inject.Module;

/**
 * Helper class to create the {@link RestContext}.
 * 
 * @author Ignasi Barrera
 */
public class AbiquoContextFactory
{
    /** The Abiquo provider name */
    public static String PROVIDER_NAME = "abiquo";

    /** The delegating {@link RestContextFactory}. */
    private final RestContextFactory contextFactory;

    /**
     * Initializes with the default properties built-in to jclouds. This is typically stored in the
     * classpath resource {@code rest.properties}
     * 
     * @see RestContextFactory#getPropertiesFromResource
     */
    public AbiquoContextFactory()
    {
        this(new RestContextFactory());
    }

    /**
     * Finds definitions in the specified properties.
     */
    public AbiquoContextFactory(final Properties properties)
    {
        this(new RestContextFactory(properties));
    }

    public AbiquoContextFactory(final RestContextFactory contextFactory)
    {
        this.contextFactory = contextFactory;
    }

    /**
     * @see #createContext(String, String, String)
     */
    public AbiquoContext createContext(final String identity, final String credential)
    {
        return createContext(PROVIDER_NAME, identity, credential);
    }

    /**
     * @see #createContext(String, String, Properties)
     */
    public AbiquoContext createContext(final String identity, final String credential,
        final Properties overrides)
    {
        return createContext(PROVIDER_NAME, identity, credential, overrides);
    }

    /**
     * @see #createContext(String, Properties)
     */
    public AbiquoContext createContext(final Properties overrides)
    {
        return createContext(PROVIDER_NAME, overrides);
    }

    /**
     * @see RestContextFactory#createContextBuilder(String, String, String)
     */
    public AbiquoContext createContext(final String provider, final String identity,
        final String credential)
    {
        RestContextBuilder< ? , ? > builder =
            RestContextBuilder.class.cast(contextFactory.createContextBuilder(provider, identity,
                credential));
        return buildContextUnwrappingExceptions(builder);
    }

    /**
     * @see RestContextFactory#createContextBuilder(String, String, String)
     */
    public AbiquoContext createContext(final String provider, final String identity,
        final String credential, final Properties overrides)
    {
        RestContextBuilder< ? , ? > builder =
            RestContextBuilder.class.cast(contextFactory.createContextBuilder(provider, identity,
                credential, overrides));
        return buildContextUnwrappingExceptions(builder);
    }

    /**
     * @see RestContextFactory#createContextBuilder(String, Properties)
     */
    public AbiquoContext createContext(final String provider, final Properties overrides)
    {
        RestContextBuilder< ? , ? > builder =
            RestContextBuilder.class.cast(contextFactory.createContextBuilder(provider, overrides));
        return buildContextUnwrappingExceptions(builder);
    }

    /**
     * @see #createContext(String, Iterable, Properties)
     */
    public AbiquoContext createContext(final Iterable< ? extends Module> modules,
        final Properties overrides)
    {
        return createContext(PROVIDER_NAME, modules, overrides);
    }

    /**
     * @see RestContextFactory#createContextBuilder(String, Iterable, Properties)
     */
    public AbiquoContext createContext(final String provider,
        final Iterable< ? extends Module> modules, final Properties overrides)
    {
        RestContextBuilder< ? , ? > builder =
            RestContextBuilder.class.cast(contextFactory.createContextBuilder(provider, modules,
                overrides));
        return buildContextUnwrappingExceptions(builder);
    }

    /**
     * @see #createContext(String,String,String,Iterable)
     */
    public AbiquoContext createContext(@Nullable final String identity,
        @Nullable final String credential, final Iterable< ? extends Module> modules)
    {
        return createContext(PROVIDER_NAME, identity, credential, modules);
    }

    /**
     * @see RestContextFactory#createContextBuilder(String,String String, Iterable)
     */
    public AbiquoContext createContext(final String provider, @Nullable final String identity,
        @Nullable final String credential, final Iterable< ? extends Module> modules)
    {
        RestContextBuilder< ? , ? > builder =
            RestContextBuilder.class.cast(contextFactory.createContextBuilder(provider, identity,
                credential, modules));
        return buildContextUnwrappingExceptions(builder);
    }

    /**
     * @see #createContext(String,String, String, Iterable, Properties)
     */
    public AbiquoContext createContext(@Nullable final String identity,
        @Nullable final String credential, final Iterable< ? extends Module> modules,
        final Properties overrides)
    {
        return createContext(PROVIDER_NAME, identity, credential, modules, overrides);
    }

    /**
     * @see RestContextFactory#createContextBuilder(String,String,String, Iterable, Properties)
     */
    public AbiquoContext createContext(final String provider, @Nullable final String identity,
        @Nullable final String credential, final Iterable< ? extends Module> modules,
        final Properties overrides)
    {
        RestContextBuilder< ? , ? > builder =
            RestContextBuilder.class.cast(contextFactory.createContextBuilder(provider, identity,
                credential, modules, overrides));
        return buildContextUnwrappingExceptions(builder);
    }

    /**
     * @see RestContextFactory#createContextBuilder(ContextSpec)
     */
    public <S, A> AbiquoContext createContext(final RestContextSpec<S, A> contextSpec)
    {
        RestContextBuilder< ? , ? > builder =
            RestContextBuilder.class.cast(createContextBuilder(contextSpec));
        return buildContextUnwrappingExceptions(builder);

    }

    /**
     * @see RestContextFactory#createContextBuilder(ContextSpec, Properties)
     */
    public <S, A> AbiquoContext createContext(final RestContextSpec<S, A> contextSpec,
        final Properties overrides)
    {
        RestContextBuilder< ? , ? > builder =
            RestContextBuilder.class.cast(createContextBuilder(contextSpec, overrides));
        return buildContextUnwrappingExceptions(builder);
    }

    public static <S, A> AbiquoContext buildContextUnwrappingExceptions(
        final RestContextBuilder<S, A> builder)
    {
        try
        {
            return (AbiquoContext) builder.buildContext();
        }
        catch (Exception ex)
        {
            return Throwables2.propagateAuthorizationOrOriginalException(ex);
        }
    }
}
