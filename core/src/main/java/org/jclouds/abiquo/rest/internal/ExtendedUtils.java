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

package org.jclouds.abiquo.rest.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Named;

import org.jclouds.Constants;
import org.jclouds.crypto.Crypto;
import org.jclouds.date.DateService;
import org.jclouds.json.Json;
import org.jclouds.logging.Logger.LoggerFactory;
import org.jclouds.rest.HttpAsyncClient;
import org.jclouds.rest.HttpClient;
import org.jclouds.rest.Utils;
import org.jclouds.rest.internal.UtilsImpl;
import org.jclouds.xml.XMLParser;

import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Custom utility methods.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class ExtendedUtils extends UtilsImpl implements Utils
{
    private XMLParser xml;

    private AbiquoHttpClient abiquoHttpClient;

    private AbiquoHttpAsyncClient abiquoHttpAsyncClient;

    @Inject
    public ExtendedUtils(final Injector injector, final Json json, final HttpClient simpleClient,
        final HttpAsyncClient simpleAsyncClient, final Crypto encryption, final DateService date,
        @Named(Constants.PROPERTY_USER_THREADS) final ExecutorService userThreads,
        @Named(Constants.PROPERTY_IO_WORKER_THREADS) final ExecutorService ioThreads,
        final LoggerFactory loggerFactory, final XMLParser xml,
        final AbiquoHttpClient abiquoHttpClient, final AbiquoHttpAsyncClient abiquoHttpAsyncClient)
    {
        super(injector, json, simpleClient, simpleAsyncClient, encryption, date, userThreads,
            ioThreads, loggerFactory);
        this.xml = checkNotNull(xml, "xml");
        this.abiquoHttpClient = checkNotNull(abiquoHttpClient, "abiquoHttpClient");
        this.abiquoHttpAsyncClient = checkNotNull(abiquoHttpAsyncClient, "abiquoHttpAsyncClient");
    }

    public XMLParser getXml()
    {
        return xml;
    }

    public AbiquoHttpClient getAbiquoHttpClient()
    {
        return abiquoHttpClient;
    }

    public AbiquoHttpAsyncClient getAbiquoHttpAsyncClient()
    {
        return abiquoHttpAsyncClient;
    }

}
