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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import javax.ws.rs.core.Response.Status;

import org.jclouds.http.HttpResponse;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.xml.internal.JAXBParser;
import org.testng.annotations.Test;

import com.abiquo.model.transport.AcceptedRequestDto;
import com.google.common.base.Function;
import com.google.inject.TypeLiteral;

/**
 * Unit tests for the {@link ReturnTaskReferenceOrNull} function.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class ReturnTaskReferenceOrNullTest
{
    @SuppressWarnings("rawtypes")
    public void testReturnNullIfNoContent()
    {
        Function<HttpResponse, AcceptedRequestDto> function =
            new ReturnTaskReferenceOrNull(new JAXBParser(),
                TypeLiteral.get(AcceptedRequestDto.class));

        HttpResponse response = createMock(HttpResponse.class);

        expect(response.getStatusCode()).andReturn(Status.NO_CONTENT.getStatusCode());
        expect(response.getPayload()).andReturn(null);

        replay(response);

        assertNull(function.apply(response));

        verify(response);
    }

    @SuppressWarnings("rawtypes")
    public void testReturnTaskIfAccepted() throws IOException
    {
        JAXBParser parser = new JAXBParser();
        AcceptedRequestDto< ? > task = new AcceptedRequestDto<String>();
        Payload payload = Payloads.newPayload(parser.toXML(task));

        Function<HttpResponse, AcceptedRequestDto> function =
            new ReturnTaskReferenceOrNull(parser, TypeLiteral.get(AcceptedRequestDto.class));

        HttpResponse response = createMock(HttpResponse.class);

        expect(response.getStatusCode()).andReturn(Status.ACCEPTED.getStatusCode());
        // Get payload is called three times: one to deserialize it, and twice to release it
        expect(response.getPayload()).andReturn(payload);
        expect(response.getPayload()).andReturn(payload);
        expect(response.getPayload()).andReturn(payload);

        replay(response);

        assertTrue(function.apply(response) instanceof AcceptedRequestDto);

        verify(response);
    }
}
