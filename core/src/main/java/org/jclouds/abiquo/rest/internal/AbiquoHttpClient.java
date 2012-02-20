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
package org.jclouds.abiquo.rest.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.HttpMethod;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.logging.Logger;
import org.jclouds.rest.HttpClient;
import org.jclouds.xml.XMLParser;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.SingleResourceTransportDto;
import com.google.inject.TypeLiteral;

/**
 * Custom Rest methods to work with the Abiquo Api.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class AbiquoHttpClient
{
    @Resource
    protected Logger logger = Logger.NULL;

    private HttpClient http;

    private XMLParser xml;

    @Inject
    protected AbiquoHttpClient(final HttpClient http, final XMLParser xml)
    {
        this.http = checkNotNull(http, "http");
        this.xml = checkNotNull(xml, "xml");
    }

    /**
     * Get the resource referenced with the given link.
     * 
     * @param link The link to get.
     * @param expectedClass The class of the resource to get.
     * @return The requested resource.
     */
    public <T extends SingleResourceTransportDto> T get(final RESTLink link,
        final Class<T> expectedClass)
    {
        HttpRequest request =
            HttpRequest.builder().endpoint(URI.create(link.getHref())).method(HttpMethod.GET)
                .build();
        HttpResponse response = http.invoke(request);

        ParseXMLWithJAXB<T> parser = new ParseXMLWithJAXB<T>(xml, TypeLiteral.get(expectedClass));
        return parser.apply(response);
    }
}
