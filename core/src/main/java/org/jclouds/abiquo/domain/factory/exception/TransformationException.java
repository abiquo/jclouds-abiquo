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

package org.jclouds.abiquo.domain.factory.exception;

/**
 * Exception thrown during model to client object transformation.
 * 
 * @author Francesc Montserrat
 */
public class TransformationException extends RuntimeException
{
    private static final long serialVersionUID = -3093923238120960853L;

    private Class< ? > source;

    private Class< ? > target;

    public TransformationException(final Class< ? > source, final Class< ? > target)
    {
        this.source = source;
        this.target = target;
    }

    public TransformationException(final Class< ? > source, final Class< ? > target,
        final String message)
    {
        super(message);
        this.source = source;
        this.target = target;
    }

    public TransformationException(final Class< ? > source, final Class< ? > target,
        final Throwable cause)
    {
        super(cause);
        this.source = source;
        this.target = target;
    }

    public TransformationException(final Class< ? > source, final Class< ? > target,
        final String message, final Throwable cause)
    {
        super(message, cause);
        this.source = source;
        this.target = target;
    }

    @Override
    public String getMessage()
    {
        String msg =
            "Could not transform source " + source.getName() + " to target " + target.getName()
                + ": ";
        return msg + super.getMessage();
    }

    public Class< ? > getSource()
    {
        return source;
    }

    public Class< ? > getTarget()
    {
        return target;
    }
}
