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

package org.jclouds.abiquo.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.domain.exception.AbiquoException;

import com.abiquo.model.transport.error.ErrorDto;

/**
 * Assertion utilities.
 * 
 * @author Ignasi Barrera
 */
public class Assert
{
    /**
     * Assert that the exception contains the given error.
     * 
     * @param exception The exception.
     * @param expectedHttpStatus The expected HTTP status code.
     * @param expectedErrorCode The expected error code.
     */
    public static void assertHasError(final AbiquoException exception,
        final Status expectedHttpStatus, final String expectedErrorCode)
    {
        assertEquals(exception.getHttpStatus(), expectedHttpStatus);
        ErrorDto error = exception.findError(expectedErrorCode);
        assertNotNull(error);
    }

}
