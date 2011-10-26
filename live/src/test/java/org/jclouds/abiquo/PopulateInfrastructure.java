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

package org.jclouds.abiquo;

import java.util.Properties;

import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.Machine;
import org.jclouds.abiquo.domain.infrastructure.Rack;
import org.jclouds.abiquo.reference.AbiquoEdition;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;

import com.abiquo.model.enumerator.HypervisorType;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class PopulateInfrastructure
{
    public static void main(final String[] args)
    {
        /* ******************* Create context *************************************** */

        Properties props = new Properties();
        props.put("abiquo.endpoint", "http://10.60.21.33/api");
        AbiquoContext context =
            new AbiquoContextFactory().createContext("admin", "xabiquo",
                ImmutableSet.<Module> of(new Log4JLoggingModule()), props);

        try
        {

            /* ******************* Create datacenter and rack *************************** */

            Datacenter datacenter =
                Datacenter.builder(context).name("API datacenter").location("Barcelona")
                    .remoteServices("10.60.21.34", AbiquoEdition.ENTERPRISE).build();
            datacenter.save();

            Rack rack = Rack.builder(context, datacenter).name("API rack").build();
            rack.save();

            /* ******************* Discover and add hypervisor *************************** */

            Machine machine =
                datacenter.discoverSingleMachine("10.60.1.79", HypervisorType.XENSERVER, "root",
                    "temporal");

            // We need to enable at least one datastore, and set the virtual switch to use
            machine.getDatastores().iterator().next().setEnabled(true);
            machine.setVirtualSwitch(machine.getVirtualSwitches().get(0));
            machine.setRack(rack);

            machine.save();
        }
        finally
        {
            if (context != null)
            {
                context.close();
            }
        }
    }
}
