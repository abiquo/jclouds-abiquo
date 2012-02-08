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
import static org.testng.Assert.assertNull;

import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

/**
 * Live integration tests for the {@link HardDisk} domain class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "live")
public class HardDiskLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{
    private Integer diskId;

    public void createHardDisk()
    {
        HardDisk hardDisk = HardDisk.builder(context, env.virtualDatacenter).sizeInMb(64L).build();
        hardDisk.save();

        // TODO: Hard disk does not have an id field
        assertNotNull(hardDisk.unwrap().getEditLink());
        assertNotNull(hardDisk.getSequence());

        diskId = hardDisk.unwrap().getIdFromLink("edit");
        assertNotNull(env.virtualDatacenter.getHardDisk(diskId));
    }

    @Test(dependsOnMethods = "createHardDisk")
    public void deleteHardDiske()
    {
        HardDisk hardDisk = env.virtualDatacenter.getHardDisk(diskId);
        assertNotNull(hardDisk);

        hardDisk.delete();
        assertNull(env.virtualDatacenter.getVolume(diskId));
    }

}
