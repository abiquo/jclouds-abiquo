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
import static org.testng.Assert.assertFalse;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.ws.rs.core.HttpHeaders;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.utils.ModifyRequest;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link AbiquoAuthentication} filter.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class AbiquoAuthenticationTest
{

    public void testBasicAuthentication() throws UnsupportedEncodingException,
        NoSuchAlgorithmException, CertificateException
    {
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://foo")).build();

        AbiquoAuthentication filter = basicAuthFilter();
        HttpRequest filtered = filter.filter(request);
        HttpRequest expected =
            ModifyRequest.replaceHeader(request, HttpHeaders.AUTHORIZATION,
                AbiquoAuthentication.basicAuth("user", "password"));

        assertFalse(filtered.getHeaders().containsKey(HttpHeaders.COOKIE));
        assertEquals(filtered, expected);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testBasicAuthenticationWithoutIdentity() throws UnsupportedEncodingException,
        NoSuchAlgorithmException, CertificateException
    {
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://foo")).build();

        AbiquoAuthentication filter = basicAuthFilter();
        filter.identityOrToken = null;

        filter.filter(request);
    }

    public void testTokenAuthentication() throws UnsupportedEncodingException,
        NoSuchAlgorithmException, CertificateException
    {
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://foo")).build();

        AbiquoAuthentication filter = tokenFilter();
        HttpRequest filtered = filter.filter(request);
        HttpRequest expected =
            ModifyRequest.replaceHeader(request, HttpHeaders.COOKIE,
                AbiquoAuthentication.tokenAuth("token"));

        assertFalse(filtered.getHeaders().containsKey(HttpHeaders.AUTHORIZATION));
        assertEquals(filtered, expected);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testTokenAuthenticationWithoutToken() throws UnsupportedEncodingException,
        NoSuchAlgorithmException, CertificateException
    {
        HttpRequest request =
            HttpRequest.builder().method("GET").endpoint(URI.create("http://foo")).build();

        AbiquoAuthentication filter = tokenFilter();
        filter.identityOrToken = null;

        filter.filter(request);
    }

    private static AbiquoAuthentication basicAuthFilter() throws UnsupportedEncodingException,
        NoSuchAlgorithmException, CertificateException
    {
        AbiquoAuthentication filter = new AbiquoAuthentication();
        filter.identityOrToken = "user";
        filter.credential = "password";
        return filter;
    }

    private static AbiquoAuthentication tokenFilter() throws UnsupportedEncodingException,
        NoSuchAlgorithmException, CertificateException
    {
        AbiquoAuthentication filter = new AbiquoAuthentication();
        filter.identityOrToken = "token";
        return filter;
    }
}
