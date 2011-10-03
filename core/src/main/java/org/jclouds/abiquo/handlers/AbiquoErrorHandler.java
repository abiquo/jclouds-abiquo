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

package org.jclouds.abiquo.handlers;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.functions.ParseErrors;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.HttpResponseException;
import org.jclouds.rest.AuthorizationException;
import org.jclouds.rest.ResourceNotFoundException;

import com.abiquo.model.transport.error.ErrorDto;
import com.abiquo.model.transport.error.ErrorsDto;
import com.google.common.io.Closeables;

/**
 * Parse Abiquo API errors and set the appropiate exteption.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class AbiquoErrorHandler implements HttpErrorHandler
{
    /** The error parser. */
    private ParseErrors errorParser;

    @Inject
    AbiquoErrorHandler(final ParseErrors errorParser)
    {
        super();
        this.errorParser = errorParser;
    }

    @Override
    public void handleError(final HttpCommand command, final HttpResponse response)
    {
        ErrorsDto errors = errorParser.apply(response);
        String message = getMessage(errors);
        Exception exception = new HttpResponseException(command, response, message);

        try
        {
            message =
                message != null ? message : String.format("%s -> %s", command.getCurrentRequest()
                    .getRequestLine(), response.getStatusLine());

            switch (response.getStatusCode())
            {
                case 401:
                case 403:
                    exception = new AuthorizationException(message, exception);
                    break;
                case 404:
                    exception = new ResourceNotFoundException(message, exception);
                    break;
            }
        }
        finally
        {
            if (response.getPayload() != null)
            {
                Closeables.closeQuietly(response.getPayload().getInput());
            }
            command.setException(exception);
        }
    }

    private String getMessage(final ErrorsDto errors)
    {
        StringBuffer buffer = new StringBuffer();
        for (ErrorDto error : errors.getCollection())
        {
            buffer.append(error.getMessage());
            buffer.append("\n");
        }
        return buffer.toString();
    }

}
