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
import java.util.Properties;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.network.PrivateNetwork;
import org.jclouds.abiquo.predicates.enterprise.EnterprisePredicates;
import org.jclouds.abiquo.predicates.infrastructure.DatacenterPredicates;
import org.jclouds.abiquo.predicates.infrastructure.HypervisorPredicates;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;

import com.abiquo.model.enumerator.HypervisorType;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class test
{

    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        // First of all create the Abiquo context pointing to the Abiquo API
        Properties props = new Properties();
        props.put("abiquo.endpoint", "http://10.60.1.249/api");
        AbiquoContext context =
            new AbiquoContextFactory().createContext("admin", "xabiquo", ImmutableSet
                .<Module> of(new SLF4JLoggingModule()), props);

        try
        {
            // Get the enterprise you are managing
            Enterprise enterprise =
                context.getAdministrationService().findEnterprise(
                    EnterprisePredicates.name("Abiquo"));

            // Get the datacenter where the virtual datacenter will be created (must be allowed to
            // the enterprise)
            Datacenter datacenter =
                enterprise.findAllowedDatacenter(DatacenterPredicates.name("Hawaii"));

            // Choose the type of hypervisor among the availables in the datacenter
            HypervisorType hypervisor =
                datacenter.findHypervisor(HypervisorPredicates.type(HypervisorType.KVM));

            // Create a default private network for the virtual datacenter
            PrivateNetwork network =
                PrivateNetwork.builder(context).name("DefaultNetwork").gateway("192.168.1.1")
                    .address("192.168.1.0").mask(24).build();

            // Create the virtual datacenter
            VirtualDatacenter virtualDatacenter =
                VirtualDatacenter.builder(context, datacenter, enterprise).name(
                    "API virtual datacenter").cpuCountLimits(18, 20).hdLimitsInMb(279172872,
                    279172872).publicIpsLimits(2, 2).ramLimits(19456, 20480).storageLimits(
                    289910292, 322122547).vlansLimits(1, 2).hypervisorType(hypervisor).network(
                    network).build();

            virtualDatacenter.save();

            // At this point the virtual infrastructure is ready and users will be able to start
            // deploying virtual machines
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
