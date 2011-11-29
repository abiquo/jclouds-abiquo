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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.Constants.PROPERTY_CREDENTIAL;
import static org.jclouds.Constants.PROPERTY_IDENTITY;

import java.io.UnsupportedEncodingException;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;

import org.jclouds.crypto.CryptoStreams;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.http.utils.ModifyRequest;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;

/**
 * Authenticates using Basic Authentication or a generated token from previous API sessions.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class AbiquoAuthentication implements HttpRequestFilter
{
    /** The name of the authentication token. */
    public static final String AUTH_TOKEN_NAME = "auth";

    /** The identity or the authentication token. */
    @Inject
    @Named(PROPERTY_IDENTITY)
    protected String identityOrToken;

    @Inject(optional = true)
    @Named(PROPERTY_CREDENTIAL)
    protected String credential;

    @Override
    public HttpRequest filter(final HttpRequest request) throws HttpException
    {
        try
        {
            boolean isBasicAuth = credential != null;
            String header =
                isBasicAuth ? basicAuth(identityOrToken, credential) : tokenAuth(identityOrToken);
            return ModifyRequest.replaceHeader(request, isBasicAuth ? HttpHeaders.AUTHORIZATION
                : HttpHeaders.COOKIE, header);
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new HttpException(ex);
        }
    }

    @VisibleForTesting
    static String basicAuth(final String user, final String password)
        throws UnsupportedEncodingException
    {
        return "Basic "
            + CryptoStreams.base64(String.format("%s:%s", checkNotNull(user, "user"),
                checkNotNull(password, "password")).getBytes("UTF-8"));
    }

    @VisibleForTesting
    static String tokenAuth(final String token)
    {
        return AUTH_TOKEN_NAME + "=" + checkNotNull(token, "token");
    }
}
