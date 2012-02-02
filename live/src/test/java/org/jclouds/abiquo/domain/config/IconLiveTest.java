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

package org.jclouds.abiquo.domain.config;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.jclouds.abiquo.predicates.config.IconPredicates;
import org.testng.annotations.Test;

/**
 * Live integration tests for the {@link User} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class IconLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

    public void testCreateAndGet()
    {
        Icon icon =
            Icon.builder(context).name("icon").path("http://fakepath.com/fakeimage.jpg").build();
        icon.save();

        Icon apiIcon =
            context.getAdministrationService().findIcon("http://fakepath.com/fakeimage.jpg");

        assertNotNull(apiIcon);
        assertEquals(icon.getName(), apiIcon.getName());

        icon.delete();
    }

    public void testUpdate()
    {
        Iterable<Icon> icons = context.getAdministrationService().listIcons();
        assertNotNull(icons);

        Icon icon = icons.iterator().next();
        String name = icon.getName();

        icon.setName("temp name");
        icon.update();

        Icon apiIcon =
            context.getAdministrationService().findIcon(IconPredicates.name("temp name"));

        assertNotNull(apiIcon);
        assertEquals("temp name", apiIcon.getName());

        icon.setName(name);
        icon.update();
    }
}
