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

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.io.Payload;
import org.testng.annotations.Test;

/**
 * Base class for parser unit tests.
 * 
 * @author Ignasi Barrera
 */

@Test(groups = "unit")
public abstract class ParseXMLTest<T> extends BaseHandlerTest
{
    @Test
    public void testObjectFromResponse() throws Exception
    {
        ParseXMLWithJAXB<T> function = getParser();
        HttpResponse response = createMock(HttpResponse.class);
        Payload payload = createMock(Payload.class);

        expect(payload.getInput()).andReturn(getPayloadInput()).atLeastOnce();
        expect(response.getPayload()).andReturn(payload).atLeastOnce();
        payload.release();

        replay(payload);
        replay(response);

        T object = function.apply(response);
        verifyObject(object);

        verify(payload);
        verify(response);
    }

    /**
     * Get the parser to test.
     */
    protected abstract ParseXMLWithJAXB<T> getParser();

    /**
     * Get the payload to be used in the test.
     */
    protected abstract String getPayload();

    /**
     * Verify the object returned by the parser.
     * 
     * @param object The object returned by the parser.
     */
    protected abstract void verifyObject(T object);

    protected InputStream getPayloadInput()
    {
        return new ByteArrayInputStream(getPayload().getBytes());
    }
}
