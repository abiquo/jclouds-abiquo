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

package org.jclouds.abiquo;

import org.jclouds.abiquo.features.AdministrationService;
import org.jclouds.abiquo.internal.AbiquoContextImpl;
import org.jclouds.rest.RestContext;

import com.google.inject.ImplementedBy;

/**
 * Abiquo {@link RestContext} implementation to expose high level Abiquo functionalities.
 * 
 * @author Ignasi Barrera
 */
@ImplementedBy(AbiquoContextImpl.class)
public interface AbiquoContext extends RestContext<AbiquoClient, AbiquoAsyncClient>
{
    AdministrationService getInfrastructureService();
}
