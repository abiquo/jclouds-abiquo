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

package org.jclouds.abiquo.http.filters;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.http.HttpRequest;
import org.testng.annotations.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Unit tests for the {@link AppendApiVersionToMediaType} filter.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class AppendApiVersionToMediaTypeTest
{
    public void testAppendVersionToEmptyCollection()
    {
        AppendApiVersionToMediaType filter =
            new AppendApiVersionToMediaType(AbiquoAsyncClient.API_VERSION);
        assertTrue(Iterables.isEmpty(filter.appendVersion(new ArrayList<String>())));
    }

    public void testAppendVersionToCollectionWithVersion()
    {
        AppendApiVersionToMediaType filter =
            new AppendApiVersionToMediaType(AbiquoAsyncClient.API_VERSION);

        List<String> headers = new ArrayList<String>();
        headers.add("application/xml;version=2.1-SNAPSHOT");
        headers.add("application/vnd.abiquo-datacentersdto+xml;version=1.8.5");

        List<String> headersWithVersion = Lists.newArrayList(filter.appendVersion(headers));
        assertEquals(headersWithVersion.get(0), "application/xml;version=2.1-SNAPSHOT");
        assertEquals(headersWithVersion.get(1),
            "application/vnd.abiquo-datacentersdto+xml;version=1.8.5");
    }

    public void testAppendVersionToCollectionWithoutVersion()
    {
        AppendApiVersionToMediaType filter =
            new AppendApiVersionToMediaType(AbiquoAsyncClient.API_VERSION);

        List<String> headers = new ArrayList<String>();
        headers.add("application/xml");
        headers.add("application/vnd.abiquo-datacentersdto+xml");

        List<String> headersWithVersion = Lists.newArrayList(filter.appendVersion(headers));
        assertEquals(headersWithVersion.get(0), "application/xml;version="
            + AbiquoAsyncClient.API_VERSION);
        assertEquals(headersWithVersion.get(1),
            "application/vnd.abiquo-datacentersdto+xml;version=" + AbiquoAsyncClient.API_VERSION);
    }

    public void testFilterWithoutHeaders()
    {
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://foo")).build();

        AppendApiVersionToMediaType filter =
            new AppendApiVersionToMediaType(AbiquoAsyncClient.API_VERSION);

        HttpRequest filtered = filter.filter(request);

        assertTrue(filtered.getHeaders().get(HttpHeaders.ACCEPT).isEmpty());
        assertTrue(filtered.getHeaders().get(HttpHeaders.CONTENT_TYPE).isEmpty());
    }

    public void testFilterWithVersionInMediaType()
    {
        Multimap<String, String> headers = LinkedHashMultimap.<String, String> create();
        headers.put(HttpHeaders.CONTENT_TYPE, "application/xml;version=2.1-SNAPSHOT");

        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://foo")).headers(headers)
                .build();

        AppendApiVersionToMediaType filter =
            new AppendApiVersionToMediaType(AbiquoAsyncClient.API_VERSION);

        HttpRequest filtered = filter.filter(request);

        assertTrue(filtered.getHeaders().get(HttpHeaders.ACCEPT).isEmpty());

        Collection<String> contentType = filtered.getHeaders().get(HttpHeaders.CONTENT_TYPE);
        assertEquals(contentType.size(), 1);
        assertEquals(contentType.iterator().next(), "application/xml;version=2.1-SNAPSHOT");
    }

    public void testFilterWithoutVersionInMediaType()
    {
        Multimap<String, String> headers = LinkedHashMultimap.<String, String> create();
        headers.put(HttpHeaders.ACCEPT, "application/xml");

        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://foo")).headers(headers)
                .build();

        AppendApiVersionToMediaType filter =
            new AppendApiVersionToMediaType(AbiquoAsyncClient.API_VERSION);

        HttpRequest filtered = filter.filter(request);

        assertTrue(filtered.getHeaders().get(HttpHeaders.CONTENT_TYPE).isEmpty());

        Collection<String> accept = filtered.getHeaders().get(HttpHeaders.ACCEPT);
        assertEquals(accept.size(), 1);
        assertEquals(accept.iterator().next(), "application/xml;version="
            + AbiquoAsyncClient.API_VERSION);
    }

    public void testFilterWithMixedVersionInMediaType()
    {
        Multimap<String, String> headers = LinkedHashMultimap.<String, String> create();
        headers.put(HttpHeaders.ACCEPT, "application/xml");
        headers.put(HttpHeaders.ACCEPT, "application/json;version=3.5");
        headers.put(HttpHeaders.CONTENT_TYPE, "application/xml;version=1.8.5");

        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://foo")).headers(headers)
                .build();

        AppendApiVersionToMediaType filter =
            new AppendApiVersionToMediaType(AbiquoAsyncClient.API_VERSION);

        HttpRequest filtered = filter.filter(request);

        List<String> accept = Lists.newArrayList(filtered.getHeaders().get(HttpHeaders.ACCEPT));
        assertEquals(accept.size(), 2);
        assertEquals(accept.get(0), "application/xml;version=" + AbiquoAsyncClient.API_VERSION);
        assertEquals(accept.get(1), "application/json;version=3.5");

        Collection<String> contentType = filtered.getHeaders().get(HttpHeaders.CONTENT_TYPE);
        assertEquals(contentType.size(), 1);
        assertEquals(contentType.iterator().next(), "application/xml;version=1.8.5");
    }
}
