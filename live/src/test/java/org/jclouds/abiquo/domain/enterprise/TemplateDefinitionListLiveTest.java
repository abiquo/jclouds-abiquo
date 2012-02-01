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

package org.jclouds.abiquo.domain.enterprise;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.List;

import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.jclouds.abiquo.predicates.enterprise.TemplateDefinitionListPredicates;
import org.testng.annotations.Test;

/**
 * Live integration tests for the {@link TemplateDefinitionList} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class TemplateDefinitionListLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

    public void testCreateAndGet()
    {
        TemplateDefinitionList list =
            TemplateDefinitionList.builder(context, env.enterprise).name("myList").url(
                "http://virtualapp-repository.com/vapp1.ovf").build();

        list.save();

        assertNotNull(list.getId());

        List<TemplateDefinitionList> lists =
            env.enterprise.listTemplateDefinitionLists(TemplateDefinitionListPredicates
                .name("myList"));

        assertEquals(lists.size(), 1);

        Integer idTemplateList = list.getId();
        list.delete();
        assertNull(env.enterprise.getTemplateDefinitionList(idTemplateList));
    }
}
