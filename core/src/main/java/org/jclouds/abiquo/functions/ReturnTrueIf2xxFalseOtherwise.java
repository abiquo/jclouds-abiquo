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

package org.jclouds.abiquo.functions;

import static org.jclouds.http.HttpUtils.releasePayload;

import org.jclouds.http.HttpResponse;

import com.google.common.base.Function;

/**
 * Simply returns true when the http response code is in the range 200-299.
 * 
 * @author Ignasi Barrera
 */
public class ReturnTrueIf2xxFalseOtherwise implements Function<HttpResponse, Boolean>
{
    @Override
    public Boolean apply(final HttpResponse from)
    {
        releasePayload(from);
        int code = from.getStatusCode();
        return code >= 200 && code < 300;
    }
}
