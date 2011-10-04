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
package org.jclouds.abiquo.domain.factory;

import java.util.ArrayList;
import java.util.Collection;

import com.abiquo.model.transport.SingleResourceTransportDto;
import com.abiquo.model.util.ModelTransformer;

/**
 * Generic transformer. Transforms Abiquo model dto resources into Client resources and the other
 * way around.
 * 
 * @author Francesc Montserrat
 * @param <TDto> Abiquo model resource (MUST extend SingleResourceTransportDto)
 * @param <TClient> Client resource (MUST extend TDto)
 */
public class ClientTransformer<TDto extends SingleResourceTransportDto, TClient extends TDto>
    extends ModelTransformer
{
    /**
     * .class of the client resource.
     */
    Class<TClient> clientClass;

    /**
     * .class of the model dto resource.
     */
    Class<TDto> modelClass;

    /**
     * ClientTransformer constructor for a generic client resource - model dto pair
     * 
     * @param modelClass .class of the model dto resource.
     * @param clientClass .class of the APIClient resource.
     */
    public ClientTransformer(final Class<TDto> modelClass, final Class<TClient> clientClass)
    {
        super();
        this.clientClass = clientClass;
        this.modelClass = modelClass;
    }

    /**
     * Creates a client resource from an Abiquo model dto resource
     * 
     * @param dto Abiquo model dto resource.
     * @return Client resource.
     * @throws Exception Throws Exception if there is an error during transformation.
     */
    public TClient createResource(final TDto dto) throws Exception
    {
        TClient out = persistenceFromTransport(clientClass, dto);
        out.setLinks(dto.getLinks());
        return out;
    }

    /**
     * Creates a client resource Iterable from an Abiquo model dto resource Iterable
     * 
     * @param iterable Abiquo model dto resource iterable.
     * @return Client resource iterable.
     * @throws Exception Throws Exception if there is an error during transformation.
     */
    public Iterable<TClient> createResourceIterable(final Iterable<TDto> iterable) throws Exception
    {
        Collection<TClient> listc = new ArrayList<TClient>();

        for (TDto element : iterable)
        {
            listc.add(createResource(element));
        }
        return listc;
    }

    /**
     * Creates an Abiquo model dto resource from a client resource
     * 
     * @param resource Client resource.
     * @return Abiquo model dto resource.
     * @throws Exception Throws Exception if there is an error during transformation.
     */
    public TDto toDto(final TClient resource) throws Exception
    {
        TDto dto = transportFromPersistence(modelClass, resource);
        dto.setLinks(resource.getLinks());
        return dto;
    }

    /**
     * Creates an Abiquo model dto resource iterable from a client resource iterable
     * 
     * @param iterable Client resource iterable.
     * @return Abiquo model dto resource iterable.
     * @throws Exception Throws Exception if there is an error during transformation.
     */
    public Iterable<TDto> toDtoList(final Iterable<TClient> iterable) throws Exception
    {
        Collection<TDto> listd = new ArrayList<TDto>();

        for (TDto element : iterable)
        {
            listd.add(createResource(element));
        }
        return listd;
    }

    /**
     * Updates an existing client resource with an existing Abiquo model dto resource's data
     * 
     * @param source Abiquo model dto source.
     * @param target Client resource object to update with source's info.
     * @throws Exception Throws Exception if there is an error during transformation.
     */
    public void updateResource(final TDto source, final TDto target) throws Exception
    {
        transform(source.getClass(), modelClass, source, target);
        target.setLinks(source.getLinks());
    }
}
