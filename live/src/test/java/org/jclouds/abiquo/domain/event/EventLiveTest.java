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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
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
import com.abiquo.server.core.event.EventDto;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;

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
        String currentDate = String.valueOf(new Date().getTime());

        env.datacenter.setName("Datacenter updated");
        env.datacenter.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).datacenter("Datacenter updated").build();
        assertEvents(options);
    }

    public void testListEventsFilteredByRack()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.rack.setName("Rack updated");
        env.rack.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).rack("Rack updated").build();
        assertEvents(options);
    }

    public void testListEventsFilteredByPM()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.machine.setName("PhysicalMachine updated");
        env.machine.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).physicalMachine("PhysicalMachine updated")
                .build();
        assertEvents(options);
    }

    /** XXX: The tracer does not save the storage device **/
    @Test(enabled = false)
    public void testListEventsFilteredByStorageDevice()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.storageDevice.setName("StorageDevice updated");
        env.storageDevice.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).storageSystem("StorageDevice updated")
                .build();
        assertEvents(options);
    }

    /** XXX: The tracer does not save the storage pool **/
    @Test(enabled = false)
    public void testListEventsFilteredByStoragePool()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.storagePool.setName("StoragePool updated");
        env.storagePool.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).storagePool("StoragePool updated").build();
        assertEvents(options);
    }

    /** XXX: The tracer does not save the enterprise event **/
    @Test(enabled = false)
    public void testListEventsFilteredByEnterprise()
    {
        String currentDate = String.valueOf(new Date().getTime());

        String entName = env.enterprise.getName();
        env.enterprise.setName("Abiquo updated");
        env.enterprise.update();

        // Enterprise current =
        // env.enterpriseAdminContext.getAdministrationService().getCurrentEnterprise();
        // current.setName("Enterprise updated");
        // current.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).enterprise("Abiquo updated").build();
        assertEvents(options);

        env.enterprise.setName(entName);
        env.enterprise.update();
    }

    /**
     * XXX: Using the painUserContext, modifiing the user returns this error: HTTP/1.1 401
     * Unauthorized
     **/
    @Test(enabled = false)
    public void testListEventsFilteredByUser()
    {
        String currentDate = String.valueOf(new Date().getTime());

        User current = env.plainUserContext.getAdministrationService().getCurrentUser();
        current.setEmail("test@test.com");
        current.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).user(current.getName()).build();
        assertEvents(options);
    }

    /** XXX: The tracer does not save the virtual datacenter **/
    @Test(enabled = false)
    public void testListEventsFilteredByVDC()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.virtualDatacenter.setName("VirtualDatacenter updated");
        env.virtualDatacenter.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate)
                .virtualDatacenter("VirtualDatacenter updated").build();
        assertEvents(options);
    }

    public void testListEventsFilteredByVapp()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.virtualAppliance.setName("VirtualApp test");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).virtualApp("VirtualApp test").build();
        assertEvents(options);
    }

    public void testListEventsFilteredByVM()
    {
        String currentDate = String.valueOf(new Date().getTime());

        VirtualMachine vm = createVirtualMachine();
        vm.delete();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).virtualMachine("Second VirtualMachine")
                .actionPerformed(EventType.VM_DELETE).build();
        assertEvents(options);
    }

    public void testListEventsFilteredByVolume()
    {
        String currentDate = String.valueOf(new Date().getTime());

        Volume volume = createVolume();
        volume.setName(PREFIX + "Event volume");
        volume.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).volume(PREFIX + "Event volume").build();
        assertEvents(options);

        volume.delete();
    }

    public void testListEventsFilteredBySeverity()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.virtualAppliance.setName("VirtualApp severity");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).virtualApp("VirtualApp severity")
                .severity(SeverityType.INFO).build();
        assertEvents(options);
    }

    public void testListEventsFilteredByActionPerformed()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.virtualAppliance.setName("VirtualApp updated");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).virtualApp("VirtualApp updated")
                .actionPerformed(EventType.VAPP_MODIFY).build();
        assertEvents(options);
    }

    public void testListEventsFilteredByComponent()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.virtualAppliance.setName("VirtualApp component");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).virtualApp("VirtualApp component")
                .component(ComponentType.VIRTUAL_APPLIANCE).build();
        assertEvents(options);
    }

    public void testListEventsFilteredByStacktrace()
    {
        String currentDate = String.valueOf(new Date().getTime());

        env.virtualAppliance.setName("VirtualApp stracktrace");
        env.virtualAppliance.update();

        EventOptions options =
            EventOptions.builder().dateFrom(currentDate).virtualApp("VirtualApp stracktrace")
                .stacktrace("Virtual appliance 'VirtualApp stracktrace' has been modified.")
                .build();
        assertEvents(options);
    }

    // Helpers

    private static void assertEvents(final EventOptions options)
    {
        List<EventDto> events = env.eventClient.listEvents(options).getCollection();
        assertEquals(events.size(), 1);
    }

    private Volume createVolume()
    {
        Tier tier = env.virtualDatacenter.findStorageTier(TierPredicates.name("Default Tier 1"));
        Volume volume =
            Volume.builder(context.getApiContext(), env.virtualDatacenter, tier)
                .name(PREFIX + "Event vol").sizeInMb(128).build();
        volume.save();

        return volume;
    }

    protected VirtualMachine createVirtualMachine()
    {
        List<VirtualMachineTemplate> templates = env.virtualDatacenter.listAvailableTemplates();
        assertFalse(templates.isEmpty());

        // Sort by size to use the smallest one
        Collections.sort(templates, new Ordering<VirtualMachineTemplate>()
        {
            @Override
            public int compare(final VirtualMachineTemplate left, final VirtualMachineTemplate right)
            {
                return Longs.compare(left.getDiskFileSize(), right.getDiskFileSize());
            }
        });

        env.template = templates.get(0);

        VirtualMachine virtualMachine =
            VirtualMachine.builder(context.getApiContext(), env.virtualAppliance, env.template)
                .cpu(2).name(PREFIX + "Second VirtualMachine").ram(128).build();

        virtualMachine.save();
        assertNotNull(virtualMachine.getId());

        return virtualMachine;
    }
}
