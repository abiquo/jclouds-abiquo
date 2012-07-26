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

package org.jclouds.abiquo.reference;

import com.abiquo.model.enumerator.NetworkType;

/**
 * Error constants.
 * 
 * @author Francesc Montserrat
 */
public class ValidationErrors
{
    public static String missingLink(final String name)
    {
        return "Missing required link " + name;
    }

    public static String missingField(final String name, final Class< ? > clazz)
    {
        return "Missing required field " + name + " in " + clazz.getName();
    }

    public static String invalidNetworkType(final NetworkType type)
    {
        return "Invalid network type " + type.name();
    }

    public static String nullResource(final Class< ? > clazz)
    {
        return "The resource should be assigned to a " + clazz.getName();
    }
}
