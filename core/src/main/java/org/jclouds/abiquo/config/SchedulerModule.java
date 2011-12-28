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

package org.jclouds.abiquo.config;

import static org.jclouds.abiquo.reference.AbiquoConstants.MAX_SCHEDULER_THREADS;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.lifecycle.Closer;
import org.jclouds.logging.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Configures the scheduler.
 * <p>
 * This class will provide a {@link ScheduledExecutorService} to be used to perform periodical
 * tasks.
 * 
 * @author Ignasi Barrera
 * @see ExecutorServiceModule
 */
@ConfiguresScheduler
public class SchedulerModule extends AbstractModule
{
    @Provides
    @Singleton
    ScheduledExecutorService provideScheduler(@Named(MAX_SCHEDULER_THREADS) final int count,
        final Closer closer)
    {
        ThreadFactory factory =
            new ThreadFactoryBuilder().setNameFormat("scheduled thread %d").setThreadFactory(
                Executors.defaultThreadFactory()).build();

        if (count == 0)
        {
            return shutdownOnClose(Executors.newSingleThreadScheduledExecutor(factory), closer);
        }
        else
        {
            return shutdownOnClose(Executors.newScheduledThreadPool(count, factory), closer);
        }
    }

    @Override
    protected void configure()
    {

    }

    private static ScheduledExecutorService shutdownOnClose(final ScheduledExecutorService service,
        final Closer closer)
    {
        closer.addToClose(new ShutdownExecutorOnClose(service));
        return service;
    }

    // This inner class is a copy of the one in org.jclouds.concurrent.config.ExecutorServiceModule
    // Since it is package protected and only visible for testing we are using a copy instead of
    // reusing it

    private static final class ShutdownExecutorOnClose implements Closeable
    {
        @Resource
        protected Logger logger = Logger.NULL;

        private final ExecutorService service;

        private ShutdownExecutorOnClose(final ExecutorService service)
        {
            this.service = service;
        }

        @Override
        public void close() throws IOException
        {
            List<Runnable> runnables = service.shutdownNow();
            if (runnables.size() > 0)
            {
                logger.warn("when shutting down executor %s, runnables outstanding: %s", service,
                    runnables);
            }
        }
    }

}
