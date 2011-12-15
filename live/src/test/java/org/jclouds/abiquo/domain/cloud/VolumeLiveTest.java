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

package org.jclouds.abiquo.domain.cloud;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.options.VolumeOptions;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.storage.VolumeManagementDto;

/**
 * Live integration tests for the {@link Volume} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class VolumeLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

    @Override
    protected CloudTestEnvironment environment(final AbiquoContext context)
    {
        return new CloudTestEnvironment(context);
    }

    public void testFilterDevices()
    {
        // Create a second volume with a complete different name
        Volume volume = Volume.Builder.fromVolume(env.volume).build();
        volume.setName("El fari es dios");

        // Create a valid filter for the original volume
        String volumeName = env.volume.getName().substring(1);
        VolumeOptions options = VolumeOptions.builder().has(volumeName).build();

        // Check
        List<VolumeManagementDto> volumes =
            env.cloudClient.listVolumes(env.virtualDatacenter.unwrap(), options).getCollection();

        assertNotNull(volumes);
        assertTrue(volumes.size() == 1);
    }
}
