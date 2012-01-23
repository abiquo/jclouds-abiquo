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
import static org.jclouds.http.HttpUtils.releasePayload;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Collection;
import java.util.Properties;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.utils.ModifyRequest;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.rest.AuthorizationException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.abiquo.server.core.enterprise.UserDto;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Live tests for the {@link AbiquoAuthentication} filter.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class AbiquoAuthenticationLiveTest
{
    private String identity;

    private String credential;

    private String endpoint;

    @BeforeMethod
    public void setupToken()
    {
        identity = checkNotNull(System.getProperty("test.abiquo.identity"), "test.abiquo.identity");
        credential =
            checkNotNull(System.getProperty("test.abiquo.credential"), "test.abiquo.credential");
        endpoint = checkNotNull(System.getProperty("test.abiquo.endpoint"), "test.abiquo.endpoint");
    }

    @Test
    public void testAuthenticateWithToken() throws IOException
    {
        String token = getAuthtenticationToken();

        // Create a new context that uses the generated token to perform the API calls
        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", endpoint);

        AbiquoContext tokenContext =
            new AbiquoContextFactory().createContext(token,
                ImmutableSet.<Module> of(new SLF4JLoggingModule()), props);

        try
        {
            // Perform a call to get the logged user and verify the identity
            UserDto user = tokenContext.getApi().getAdminClient().getCurrentUser();
            assertNotNull(user);
            assertEquals(user.getNick(), identity);
        }
        finally
        {
            if (tokenContext != null)
            {
                tokenContext.close();
            }
        }
    }

    @Test
    public void testAuthenticateWithInvalidToken() throws IOException
    {
        String token = getAuthtenticationToken() + "INVALID";

        // Create a new context that uses the generated token to perform the API calls
        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", endpoint);

        AbiquoContext tokenContext =
            new AbiquoContextFactory().createContext(token,
                ImmutableSet.<Module> of(new SLF4JLoggingModule()), props);

        // Perform a call to get the logged user. It should fail
        try
        {
            tokenContext.getApi().getAdminClient().getCurrentUser();
        }
        catch (AuthorizationException ex)
        {
            // Test succeeded
            return;
        }
        finally
        {
            if (tokenContext != null)
            {
                tokenContext.close();
            }
        }

        fail("Token authentication should have failed");
    }

    private String getAuthtenticationToken() throws UnsupportedEncodingException
    {
        String token = null;

        // Create a standard context with the configured credentials
        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", endpoint);

        AbiquoContext context =
            new AbiquoContextFactory().createContext(identity, credential,
                ImmutableSet.<Module> of(new SLF4JLoggingModule()), props);

        try
        {
            // Create a request to authenticate to the API and generate the token
            HttpRequest request =
                HttpRequest.builder().method("GET").endpoint(URI.create(endpoint)).build();
            String auth = AbiquoAuthentication.basicAuth(identity, credential);
            request = ModifyRequest.replaceHeader(request, HttpHeaders.AUTHORIZATION, auth);

            // Execute the request and read the generated token
            HttpResponse response = context.utils().http().invoke(request);
            assertEquals(response.getStatusCode(), 200);

            token = readAuthenticationToken(response);
            assertNotNull(token);

            releasePayload(response);
        }
        finally
        {
            if (context != null)
            {
                context.close();
            }
        }

        return token;
    }

    private String readAuthenticationToken(final HttpResponse response)
    {
        Collection<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertFalse(cookies.isEmpty());

        for (String cookie : cookies)
        {
            Cookie c = Cookie.valueOf(cookie);
            if (c.getName().equals(AbiquoAuthentication.AUTH_TOKEN_NAME))
            {
                return c.getValue();
            }
        }

        return null;
    }
}
