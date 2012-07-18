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

package org.jclouds.abiquo.binders;

import static org.testng.Assert.assertEquals;

import java.net.URI;

import org.jclouds.abiquo.domain.options.QueryOptions;
import org.jclouds.http.HttpRequest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * Unit tests for the {@link AppendOptionsToPath} binder.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class AppendOptionsToPathTest
{

    @Test(expectedExceptions = NullPointerException.class)
    public void testInvalidNullInput()
    {
        AppendOptionsToPath binder = new AppendOptionsToPath();
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://localhost")).build();
        binder.bindToRequest(request, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidTypeInput()
    {
        AppendOptionsToPath binder = new AppendOptionsToPath();
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://localhost")).build();
        binder.bindToRequest(request, new Object());
    }

    public void testBindEmptyOptions()
    {
        AppendOptionsToPath binder = new AppendOptionsToPath();
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://localhost")).build();
        HttpRequest newRequest = binder.bindToRequest(request, EMPTY_OPTIONS);
        assertEquals(newRequest.getRequestLine(), "GET http://localhost HTTP/1.1");
    }

    public void testBindOptions()
    {
        AppendOptionsToPath binder = new AppendOptionsToPath();
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://localhost")).build();
        HttpRequest newRequest = binder.bindToRequest(request, DUMMY_OPTIONS);
        assertEquals(newRequest.getRequestLine(),
            "GET http://localhost?option=optionvalue HTTP/1.1");
    }

    private QueryOptions EMPTY_OPTIONS = new QueryOptions()
    {
        @Override
        public Multimap<String, String> getOptions()
        {
            return ImmutableMultimap.<String, String> builder().build();
        }
    };

    private QueryOptions DUMMY_OPTIONS = new QueryOptions()
    {
        @Override
        public Multimap<String, String> getOptions()
        {
            return ImmutableMultimap.<String, String> of("option", "optionvalue");
        }
    };
}
