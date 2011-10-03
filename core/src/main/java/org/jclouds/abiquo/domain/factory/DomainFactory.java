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

import java.lang.reflect.Constructor;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoContext;

/**
 * factory used to create domain objects.
 * <p>
 * This factory must be used to create new domain objects with the {@link AbiquoContext}.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class DomainFactory
{
    /** The {@link AbiquoContext} to be propagated to the domain objects. */
    private AbiquoContext context;

    @Inject
    DomainFactory(final AbiquoContext context)
    {
        this.context = context;
    }

    /**
     * Creates a domain object of the given class.
     * 
     * @param clazz The class of the domain object to create.
     * @return The domain object with the <code>AbiquoContext</code>.
     */
    public <T> T create(final Class<T> clazz)
    {
        try
        {
            Constructor<T> constructor = clazz.getConstructor(AbiquoContext.class);
            return constructor.newInstance(context);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Could not generate and object of class: "
                + clazz.getName(), ex);
        }
    }

    public AbiquoContext getContext()
    {
        return context;
    }

}
