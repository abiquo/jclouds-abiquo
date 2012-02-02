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
import org.jclouds.abiquo.predicates.config.CategoryPredicates;
import org.testng.annotations.Test;

/**
 * Live integration tests for the {@link Category} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class CategoryLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

    public void testCreateAndGet()
    {
        Category category = Category.builder(context).name("Fake category").build();
        category.save();

        Category apiCategory =
            context.getAdministrationService().findCategory(
                CategoryPredicates.name("Fake category"));
        assertNotNull(apiCategory);
        assertEquals(category.getName(), apiCategory.getName());

        apiCategory.delete();
    }

    public void testUpdate()
    {
        Iterable<Category> categories = context.getAdministrationService().listCategories();
        assertNotNull(categories);

        Category category = categories.iterator().next();
        String name = category.getName();

        category.setName("temp name");
        category.update();

        Category apiCategory =
            context.getAdministrationService().findCategory(CategoryPredicates.name("temp name"));

        assertNotNull(apiCategory);
        assertEquals("temp name", apiCategory.getName());

        category.setName(name);
        category.update();
    }
}
