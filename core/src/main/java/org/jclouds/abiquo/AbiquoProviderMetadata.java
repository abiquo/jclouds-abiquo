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
import java.util.Set;

import org.jclouds.providers.BaseProviderMetadata;
import org.jclouds.providers.ProviderMetadata;

import com.google.common.collect.ImmutableSet;

/**
 * Implementation of {@link ProviderMetadata} for Abiquo.
 * 
 * @author Ignasi Barrera
 */
public class AbiquoProviderMetadata extends BaseProviderMetadata
{

    @Override
    public String getId()
    {
        return "abiquo";
    }

    @Override
    public String getType()
    {
        return ProviderMetadata.COMPUTE_TYPE;
    }

    @Override
    public String getName()
    {
        return "abiquo";
    }

    @Override
    public String getIdentityName()
    {
        return "API username";
    }

    @Override
    public String getCredentialName()
    {
        return "API password";
    }

    @Override
    public URI getHomepage()
    {
        return URI.create("http://www.abiquo.com");
    }

    @Override
    public URI getConsole()
    {
        return URI.create("http://www.abiquo.com");
    }

    @Override
    public URI getApiDocumentation()
    {
        // TODO: Link to concrete version space
        return URI.create("http://community.abiquo.com");
    }

    @Override
    public Set<String> getLinkedServices()
    {
        return ImmutableSet.<String> of("abiquo");
    }

    @Override
    public Set<String> getIso3166Codes()
    {
        // TODO
        return ImmutableSet.<String> of();
    }

}
