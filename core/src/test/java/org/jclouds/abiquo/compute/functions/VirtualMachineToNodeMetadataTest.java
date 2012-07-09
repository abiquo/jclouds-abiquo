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

package org.jclouds.abiquo.compute.functions;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.jclouds.abiquo.domain.DomainWrapper.wrap;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.net.URI;

import org.easymock.EasyMock;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.network.Ip;
import org.jclouds.abiquo.domain.network.PrivateIp;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadata.Status;
import org.jclouds.rest.RestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.abiquo.server.core.cloud.VirtualMachineState;
import com.abiquo.server.core.infrastructure.network.PrivateIpDto;
import com.google.common.collect.ImmutableList;

/**
 * Unit tests for the {@link VirtualMachineToNodeMetadata} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class VirtualMachineToNodeMetadataTest
{
    private VirtualMachineToNodeMetadata function;

    private VirtualMachineDto vm;

    private PrivateIpDto nic;

    @BeforeMethod
    public void setup()
    {
        function =
            new VirtualMachineToNodeMetadata(templateToImage(),
                templateToHardware(),
                stateToNodeState(),
                datacenterToLocation());

        vm = new VirtualMachineDto();
        vm.setName("VM");
        vm.setId(5);
        vm.setVdrpPort(22);
        vm.setState(VirtualMachineState.ON);
        vm.addLink(new RESTLink("edit", "http://foo/bar"));

        nic = new PrivateIpDto();
        nic.setIp("192.168.1.2");
        nic.setMac("2a:6e:40:69:84:e0");
    }

    public void testVirtualMachineToNodeMetadata()
    {
        VirtualAppliance vapp = EasyMock.createMock(VirtualAppliance.class);
        VirtualMachine mockVm = mockVirtualMachine(vapp);

        NodeMetadata node = function.apply(mockVm);

        verify(mockVm);

        assertEquals(node.getId(), vm.getId().toString());
        assertEquals(node.getUri(), URI.create("http://foo/bar"));
        assertEquals(node.getName(), vm.getName());
        assertEquals(node.getHostname(), vm.getName());
        assertEquals(node.getGroup(), "VAPP");
        assertEquals(node.getImageId(), "1");
        assertNull(node.getHardware());
        assertEquals(node.getLoginPort(), vm.getVdrpPort());
        assertEquals(node.getPublicAddresses().size(), 0);
        assertEquals(node.getPrivateAddresses().size(), 1);
        assertEquals(node.getPrivateAddresses().iterator().next(), nic.getIp());
    }

    private VirtualMachineTemplateToImage templateToImage()
    {
        VirtualMachineTemplateToImage templateToImage =
            EasyMock.createMock(VirtualMachineTemplateToImage.class);
        Image image = EasyMock.createMock(Image.class);

        expect(image.getId()).andReturn("1");
        expect(image.getOperatingSystem()).andReturn(null);
        expect(templateToImage.apply(anyObject(VirtualMachineTemplate.class))).andReturn(image);

        replay(image);
        replay(templateToImage);

        return templateToImage;
    }

    private VirtualMachineTemplateToHardware templateToHardware()
    {
        return EasyMock.createMock(VirtualMachineTemplateToHardware.class);
    }

    private DatacenterToLocation datacenterToLocation()
    {
        return EasyMock.createMock(DatacenterToLocation.class);
    }

    private VirtualMachineStateToNodeState stateToNodeState()
    {
        VirtualMachineStateToNodeState stateToNodeState =
            EasyMock.createMock(VirtualMachineStateToNodeState.class);
        expect(stateToNodeState.apply(anyObject(VirtualMachineState.class))).andReturn(
            Status.RUNNING);
        replay(stateToNodeState);
        return stateToNodeState;
    }

    @SuppressWarnings("unchecked")
    private VirtualMachine mockVirtualMachine(final VirtualAppliance vapp)
    {
        VirtualMachine mockVm = EasyMock.createMock(VirtualMachine.class);

        // Nic domain object does not have a builder, it is read only
        Ip< ? , ? > mockNic = wrap(EasyMock.createMock(RestContext.class), PrivateIp.class, nic);

        expect(mockVm.getId()).andReturn(vm.getId());
        expect(mockVm.getName()).andReturn(vm.getName());
        expect(mockVm.getName()).andReturn(vm.getName());
        expect(mockVm.unwrap()).andReturn(vm);
        expect(mockVm.getTemplate()).andReturn(null);
        expect(mockVm.getState()).andReturn(vm.getState());
        expect(mockVm.listAttachedNics()).andReturn(ImmutableList.<Ip< ? , ? >> of(mockNic));
        expect(mockVm.getVirtualAppliance()).andReturn(vapp);
        expect(vapp.getName()).andReturn("VAPP");

        replay(mockVm);
        replay(vapp);

        return mockVm;
    }
}
