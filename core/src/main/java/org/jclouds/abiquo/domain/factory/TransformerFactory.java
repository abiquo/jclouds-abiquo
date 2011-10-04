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

import com.abiquo.model.transport.SingleResourceTransportDto;

/**
 * TransformerFactory staticly creates a ClientTransformer to transform Abiquo model dto resources
 * into client resources and the other way around for the specified types.
 * 
 * @author Francesc Montserrat
 * @param <T> Dto
 */
public final class TransformerFactory<T extends SingleResourceTransportDto>
{
    private TransformerFactory()
    {
        super();
    }

    /**
     * Static Transformer getter. It creates a new Transformer for the specified resource pair.
     * 
     * @param <TDto> Abiquo Model dto resource type
     * @param <TClient> Abiquo APIClient resource type
     * @param modelClass Abiquo Model dto resource type .class
     * @param clientClass Abiquo APIClient resource type .class
     * @return Specific ClientTransformer for the requested resource
     */
    public static <TDto extends SingleResourceTransportDto, TClient extends TDto> ClientTransformer<TDto, TClient> getClientTransformer(
        final Class<TDto> modelClass, final Class<TClient> clientClass)
    {
        return new ClientTransformer<TDto, TClient>(modelClass, clientClass);
    }
}
