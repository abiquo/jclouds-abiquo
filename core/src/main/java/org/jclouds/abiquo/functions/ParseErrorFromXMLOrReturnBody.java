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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ReturnStringIf2xx;

import com.google.common.base.Function;

/**
 * Parse XML error documents and extract the error message.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class ParseErrorFromXMLOrReturnBody implements Function<HttpResponse, String>
{
    private final ReturnStringIf2xx returnStringIf200;

    @Inject
    ParseErrorFromXMLOrReturnBody(ReturnStringIf2xx returnStringIf200)
    {
        this.returnStringIf200 = returnStringIf200;
    }

    @Override
    public String apply(HttpResponse response)
    {
        String content = returnStringIf200.apply(response);
        if (content == null)
            return null;
        return parse(content);
    }

    public String parse(String in)
    {
        // TODO: Parse the XML and extract the error message
        return in;
    }

}
