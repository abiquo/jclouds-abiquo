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

package org.jclouds.abiquo.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.abiquo.domain.factory.TransformerFactory.getClientTransformer;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.Datacenter;
import org.jclouds.abiquo.features.InfrastructureService;
import org.jclouds.abiquo.reference.AbiquoConstants;
import org.jclouds.abiquo.srategy.ListDatacenters;
import org.jclouds.logging.Logger;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.google.common.base.Predicate;

/**
 * Provides high level Abiquo operations.
 * 
 * @author Ignasi Barrera
 */
public class BaseInfrastructureService implements InfrastructureService
{
    @Resource
    @Named(AbiquoConstants.ABIQUO_LOGGER)
    protected Logger logger = Logger.NULL;

    private final AbiquoContext abiquoContext;

    private final ListDatacenters listDatacenters;

    @Inject
    protected BaseInfrastructureService(final AbiquoContext abiquoContext,
        final ListDatacenters listDatacenters)
    {
        this.abiquoContext = checkNotNull(abiquoContext, "abiquoContext");
        this.listDatacenters = checkNotNull(listDatacenters, "listDatacenters");
    }

    @Override
    public Iterable<Datacenter> listDatacenters()
    {
        return getClientTransformer(DatacenterDto.class, Datacenter.class).createResourceIterable(
            listDatacenters.execute());
    }

    @Override
    public Datacenter createDatacenter(final String name, final String location)
    {
        DatacenterDto dto = new DatacenterDto();
        dto.setName(name);
        dto.setLocation(location);

        dto = abiquoContext.getApi().getInfrastructureClient().createDatacenter(dto);

        return getClientTransformer(DatacenterDto.class, Datacenter.class).createResource(dto);
    }

    @Override
    public Iterable<Datacenter> listDatacenters(final Predicate<DatacenterDto> filter)
    {
        return getClientTransformer(DatacenterDto.class, Datacenter.class).createResourceIterable(
            listDatacenters.execute(filter));
    }

    @Override
    public Datacenter getDatacenter(final Integer datacenterId)
    {
        DatacenterDto dto =
            abiquoContext.getApi().getInfrastructureClient().getDatacenter(datacenterId);

        return getClientTransformer(DatacenterDto.class, Datacenter.class).createResource(dto);
    }

    @Override
    public void deleteDatacenter(final Integer datacenterId)
    {
        abiquoContext.getApi().getInfrastructureClient().deleteDatacenter(datacenterId);
    }

    @Override
    public Datacenter updateDatacenter(final Datacenter dc)
    {
        DatacenterDto dto = getClientTransformer(DatacenterDto.class, Datacenter.class).toDto(dc);

        dto = abiquoContext.getApi().getInfrastructureClient().updateDatacenter(dto.getId(), dc);

        return getClientTransformer(DatacenterDto.class, Datacenter.class).createResource(dto);
    }
}
