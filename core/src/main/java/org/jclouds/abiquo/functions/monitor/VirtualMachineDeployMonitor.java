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

package org.jclouds.abiquo.functions.monitor;

import javax.annotation.Resource;

import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.monitor.MonitorStatus;
import org.jclouds.abiquo.features.services.MonitoringService;
import org.jclouds.logging.Logger;

import com.abiquo.server.core.cloud.VirtualMachineState;
import com.google.common.base.Function;
import com.google.inject.Singleton;

/**
 * This class takes care of monitoring the a deploy of a {@link VirtualMachine}.
 * 
 * @author Serafin Sedano
 * @see MonitoringService
 */
@Singleton
public class VirtualMachineDeployMonitor implements Function<VirtualMachine, MonitorStatus>
{
    @Resource
    private Logger logger = Logger.NULL;

    @Override
    public MonitorStatus apply(final VirtualMachine virtualMachine)
    {
        try
        {
            VirtualMachineState state = virtualMachine.getState();

            if (VirtualMachineState.NOT_ALLOCATED == state || VirtualMachineState.UNKNOWN == state)
            {
                return MonitorStatus.FAILED;
            }

            return VirtualMachineState.ON == state ? MonitorStatus.DONE : MonitorStatus.CONTINUE;
        }
        catch (Exception ex)
        {
            logger.warn(ex, "exception thrown while monitoring %s on %s, returning CONTINUE",
                virtualMachine, getClass().getName());

            return MonitorStatus.CONTINUE;
        }
    }
}