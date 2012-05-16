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

package org.jclouds.abiquo.binders.cloud;

import static org.testng.Assert.assertEquals;

import java.net.URI;

import org.jclouds.abiquo.domain.NetworkResources;
import org.jclouds.http.HttpRequest;
import org.jclouds.xml.XMLParser;
import org.jclouds.xml.internal.JAXBParser;
import org.testng.annotations.Test;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.infrastructure.network.IpPoolManagementDto;

/**
 * Unit tests for the {@link BindIpRefToPayload} binder.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class BindIpRefToPayloadTest
{

    @Test(expectedExceptions = NullPointerException.class)
    public void testInvalidNullInput()
    {
        BindIpRefToPayload binder = new BindIpRefToPayload(new JAXBParser("false"));
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://localhost")).build();
        binder.bindToRequest(request, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidTypeInput()
    {
        BindIpRefToPayload binder = new BindIpRefToPayload(new JAXBParser("false"));
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://localhost")).build();
        binder.bindToRequest(request, new Object());
    }

    public void testBindIpRef()
    {
        IpPoolManagementDto ip = NetworkResources.privateIpPut();
        RESTLink selfLink = ip.searchLink("self");
        BindIpRefToPayload binder = new BindIpRefToPayload(new JAXBParser("false"));
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://localhost")).build();
        request = binder.bindToRequest(request, ip);
        assertEquals(request.getPayload().getRawContent(), XMLParser.DEFAULT_XML_HEADER
            + "<links><link href=\"" + selfLink.getHref() + "\" rel=\"" + selfLink.getTitle()
            + "\"/></links>");
    }
}
