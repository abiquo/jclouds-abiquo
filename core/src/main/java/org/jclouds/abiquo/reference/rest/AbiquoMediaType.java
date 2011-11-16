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

package org.jclouds.abiquo.reference.rest;

import javax.ws.rs.core.MediaType;

/**
 * Defines JAX-RS {@link MediaType} used in Abiquo API.
 * 
 * @author Francesc Montserrat
 */
public class AbiquoMediaType extends MediaType
{
    public final static String APPLICATION_MANAGEDRACKDTO_XML = "application/managedrackdto+xml";

    public final static String APPLICATION_NOTMANAGEDRACKSDTO_XML =
        "application/notmanagedrackdto+xml";

    public final static String APPLICATION_LINK_XML = "application/link+xml";

    public final static String APPLICATION_FLAT_XML = "application/flat+xml";

    public final static String APPLICATION_OCTET_STEAM = "application/octet-stream";

    public static final String APPLICATION_SINGLE_MACHINE_XML = "application/machinedto+xml";

    public static final String APPLICATION_MULTIPLE_MACHINES_XML = "application/machinesdto+xml";

    public static final String APPLICATION_STORAGEPOOLDTO_XML = "application/storagepooldto+xml";

    public static final String APPLICATION_STORAGEPOOLSDTO_XML = "application/storagepoolsdto+xml";
}
