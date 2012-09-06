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

package org.jclouds.abiquo.domain.exception;

import com.abiquo.model.transport.SingleResourceTransportDto;

/**
 * Exception thrown during the wrapping process.
 * 
 * @author Ignasi Barrera
 */
public class WrapperException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private Class< ? > wrapperClass;

    private SingleResourceTransportDto target;

    public WrapperException(final Class< ? > wrapperClass, final SingleResourceTransportDto target,
        final Throwable cause)
    {
        super(cause);
        this.wrapperClass = wrapperClass;
        this.target = target;
    }

    @Override
    public String getMessage()
    {
        String msg =
            "Could not wrap object [" + target + "] in class " + wrapperClass.getName() + ": ";
        return msg + super.getMessage();
    }

    public Class< ? > getWrapperClass()
    {
        return wrapperClass;
    }

    public void setWrapperClass(final Class< ? > wrapperClass)
    {
        this.wrapperClass = wrapperClass;
    }

    public SingleResourceTransportDto getTarget()
    {
        return target;
    }

    public void setTarget(final SingleResourceTransportDto target)
    {
        this.target = target;
    }

}
