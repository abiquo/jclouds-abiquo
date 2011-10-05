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

package org.jclouds.abiquo.domain;

import static com.google.common.collect.Iterables.transform;

import java.lang.reflect.Constructor;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.exception.WrapperException;

import com.abiquo.model.transport.SingleResourceTransportDto;
import com.google.common.base.Function;

/**
 * This class is used to decorate transport objects with high level functionality.
 * 
 * @author Francesc Montserrat
 * @author Ignasi Barrera
 */
public abstract class DomainWrapper<T extends SingleResourceTransportDto>
{
    /** The rest context. */
    protected AbiquoContext context;

    /** The wrapped object. */
    protected T target;

    protected DomainWrapper(final AbiquoContext context, final T target)
    {
        super();
        this.context = context;
        this.target = target;
    }

    /**
     * Returns the wrapped object.
     */
    public T unwrap()
    {
        return target;
    }

    protected Integer getParentId(final String parentLinkRel)
    {
        return target.getIdFromLink(parentLinkRel);
    }

    /**
     * Default save operation in domain.
     */
    public abstract void save();

    /**
     * Default update operation in domain.
     */
    public abstract void update();

    /**
     * Default delete operation in domain.
     */
    public abstract void delete();

    /**
     * Wraps an object in the given wrapper class.
     */
    public static <T extends SingleResourceTransportDto, W extends DomainWrapper<T>> W wrap(
        final AbiquoContext context, final Class<W> wrapperClass, final T target)
    {
        try
        {
            Constructor<W> cons =
                wrapperClass.getDeclaredConstructor(AbiquoContext.class, target.getClass());
            if (!cons.isAccessible())
            {
                cons.setAccessible(true);
            }
            return cons.newInstance(context, target);
        }
        catch (Exception ex)
        {
            throw new WrapperException(wrapperClass, target, ex);
        }
    }

    /**
     * Wrap a collection of objects to the given wrapper class.
     */
    public static <T extends SingleResourceTransportDto, W extends DomainWrapper<T>> Iterable<W> wrap(
        final AbiquoContext context, final Class<W> wrapperClass, final Iterable<T> targets)
    {
        return transform(targets, new Function<T, W>()
        {
            @Override
            public W apply(final T input)
            {
                return wrap(context, wrapperClass, input);
            }
        });
    }
}
