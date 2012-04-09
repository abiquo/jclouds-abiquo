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
package org.jclouds.abiquo.compute.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.location.suppliers.JustProvider;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

/**
 * Converts a {@link Datacenter} to a {@link Location} one.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class DatacenterToLocation implements Function<Datacenter, Location>
{
    private final JustProvider provider;

    @Inject
    public DatacenterToLocation(final JustProvider provider)
    {
        this.provider = checkNotNull(provider, "provider");
    }

    @Override
    public Location apply(final Datacenter datacenter)
    {
        LocationBuilder builder = new LocationBuilder();
        builder.id(datacenter.getId().toString());
        builder.description(datacenter.getName() + " - " + datacenter.getLocation());
        builder.metadata(ImmutableMap.<String, Object> of());
        builder.scope(LocationScope.PROVIDER); // TODO: LocationScope?
        builder.parent(Iterables.getOnlyElement(provider.get()));
        // TODO: Convert to ISO3166 code?
        builder.iso3166Codes(ImmutableSet.<String> of(datacenter.getLocation()));
        return builder.build();
    }

}
