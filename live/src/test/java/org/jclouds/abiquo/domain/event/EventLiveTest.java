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

package org.jclouds.abiquo.domain.event;

import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Date;

import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.Volume;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.domain.event.options.EventOptions;
import org.jclouds.abiquo.domain.infrastructure.Tier;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.jclouds.abiquo.predicates.infrastructure.TierPredicates;
import org.testng.annotations.Test;

import com.abiquo.model.enumerator.ComponentType;
import com.abiquo.model.enumerator.EventType;
import com.abiquo.model.enumerator.SeverityType;
import com.google.common.collect.Iterables;

/**
 * Live integration tests for the {@link Event} domain class.
 * 
 * @author Vivien Mahé
 */
@Test(groups = "live")
public class EventLiveTest extends BaseAbiquoClientLiveTest
{
    public void testListEventsFilteredByDatacenter()
    {
        env.datacenter.setName("Datacenter updated");
        env.datacenter.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).datacenterName("Datacenter updated")
                .build();
        assertEvents(options);
    }

    public void testListEventsFilteredByRack()
    {
        env.rack.setName("Rack updated");
        env.rack.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).rackName("Rack updated").build();
        assertEvents(options);
    }

    public void testListEventsFilteredByPM()
    {
        env.machine.setName("PhysicalMachine updated");
        env.machine.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date())
                .physicalMachineName("PhysicalMachine updated").build();
        assertEvents(options);
    }

    /** TODO: The tracer does not save the storage device **/
    @Test(enabled = false)
    public void testListEventsFilteredByStorageDevice()
    {
        env.storageDevice.setName("StorageDevice updated");
        env.storageDevice.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).storageSystemName("StorageDevice updated")
                .build();
        assertEvents(options);
    }

    /** TODO: The tracer does not save the storage pool **/
    @Test(enabled = false)
    public void testListEventsFilteredByStoragePool()
    {
        env.storagePool.setName("StoragePool updated");
        env.storagePool.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).storagePoolName("StoragePool updated")
                .build();
        assertEvents(options);
    }

    /** TODO: The tracer does not save the enterprise event **/
    @Test(enabled = false)
    public void testListEventsFilteredByEnterprise()
    {
        String entName = env.enterprise.getName();
        env.enterprise.setName("Abiquo updated");
        env.enterprise.update();

        // Enterprise current =
        // env.enterpriseAdminContext.getAdministrationService().getCurrentEnterprise();
        // current.setName("Enterprise updated");
        // current.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).enterpriseName("Abiquo updated").build();
        assertEvents(options);

        env.enterprise.setName(entName);
        env.enterprise.update();
    }

    /**
     * TODO: Using the painUserContext, modifiing the user returns this error: HTTP/1.1 401
     * Unauthorized
     **/
    @Test(enabled = false)
    public void testListEventsFilteredByUser()
    {
        User current = env.plainUserContext.getAdministrationService().getCurrentUser();
        current.setEmail("test@test.com");
        current.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).userName(current.getName()).build();
        assertEvents(options);
    }

    /** TODO: The tracer does not save the virtual datacenter **/
    @Test(enabled = false)
    public void testListEventsFilteredByVDC()
    {
        env.virtualDatacenter.setName("VirtualDatacenter updated");
        env.virtualDatacenter.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date())
                .virtualDatacenterName("VirtualDatacenter updated").build();
        assertEvents(options);
    }

    public void testListEventsFilteredByVapp()
    {
        env.virtualAppliance.setName("VirtualApp test");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).virtualAppName("VirtualApp test").build();
        assertEvents(options);
    }

    public void testListEventsFilteredByVM()
    {
        VirtualMachine vm = createVirtualMachine();
        vm.delete();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).virtualMachineName("Second VirtualMachine")
                .actionPerformed(EventType.VM_DELETE).build();
        assertEvents(options);
    }

    public void testListEventsFilteredByVolume()
    {
        Volume volume = createVolume();
        volume.setName(PREFIX + "Event volume");
        volume.update();
        volume.delete(); // We don't it any more. events already exist

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).volumeName(PREFIX + "Event volume").build();
        assertEvents(options);
    }

    public void testListEventsFilteredBySeverity()
    {
        env.virtualAppliance.setName("VirtualApp severity");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).virtualAppName("VirtualApp severity")
                .severity(SeverityType.INFO).build();
        assertEvents(options);
    }

    public void testListEventsFilteredByActionPerformed()
    {
        env.virtualAppliance.setName("VirtualApp updated");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).virtualAppName("VirtualApp updated")
                .actionPerformed(EventType.VAPP_MODIFY).build();
        assertEvents(options);
    }

    public void testListEventsFilteredByComponent()
    {
        env.virtualAppliance.setName("VirtualApp component");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).virtualAppName("VirtualApp component")
                .component(ComponentType.VIRTUAL_APPLIANCE).build();
        assertEvents(options);
    }

    public void testListEventsFilteredByDescription()
    {
        env.virtualAppliance.setName("VirtualApp description");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(new Date()).virtualAppName("VirtualApp description")
                .description("Virtual appliance 'VirtualApp description' has been modified.")
                .build();
        assertEvents(options);
    }

    // Helpers

    private static void assertEvents(final EventOptions options)
    {
        Iterable<Event> events = env.eventService.listEvents(options);
        assertTrue(Iterables.size(events) >= 1);
    }

    private Volume createVolume()
    {
        Tier tier = env.virtualDatacenter.findStorageTier(TierPredicates.name("Default Tier 1"));
        Volume volume =
            Volume.builder(context.getApiContext(), env.virtualDatacenter, tier)
                .name(PREFIX + "Event vol").sizeInMb(32).build();

        volume.save();
        assertNotNull(volume.getId());

        return volume;
    }

    private VirtualMachine createVirtualMachine()
    {
        VirtualMachine virtualMachine =
            VirtualMachine.builder(context.getApiContext(), env.virtualAppliance, env.template)
                .cpu(2).name(PREFIX + "Second VirtualMachine").ram(128).build();

        virtualMachine.save();
        assertNotNull(virtualMachine.getId());

        return virtualMachine;
    }
}
