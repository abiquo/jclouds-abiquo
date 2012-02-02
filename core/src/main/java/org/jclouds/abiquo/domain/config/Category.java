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

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;

import com.abiquo.server.core.appslibrary.CategoryDto;

/**
 * Adds high level functionality to {@link CategoryDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Category+Resource">
 *      http://community.abiquo.com/display/ABI20/Category+Resource</a>
 */

public class Category extends DomainWrapper<CategoryDto>
{
    /** The default value for the default category flag. */
    private static final boolean DEFAULT_DEFAULT_CATEGORY = false;

    /** The default value for the erasable flag. */
    private static final boolean DEFAULT_ERASABLE = true;

    /**
     * Constructor to be used only by the builder. This resource cannot be created.
     */
    private Category(final AbiquoContext context, final CategoryDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Category+Resource#CategoryResource-Deleteacategory">
     *      http://community.abiquo.com/display/ABI20/Category+Resource#CategoryResource-Deleteacategory</a>
     */
    public void delete()
    {
        context.getApi().getConfigClient().deleteCategory(target);
        target = null;
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Category+Resource#CategoryResource-Createacategory">
     *      http://community.abiquo.com/display/ABI20/Category+Resource#CategoryResource-Createacategory</a>
     */
    public void save()
    {
        target = context.getApi().getConfigClient().createCategory(target);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Category+Resource#CategoryResource-Updateanexistingcategory">
     *      http://community.abiquo.com/display/ABI20/Category+Resource#CategoryResource-Updateanexistingcategory</a>
     */
    public void update()
    {
        target = context.getApi().getConfigClient().updateCategory(target);
    }

    // Builder

    public static Builder builder(final AbiquoContext context)
    {
        return new Builder(context);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private String name;

        private Boolean erasable = DEFAULT_ERASABLE;

        private Boolean defaultCategory = DEFAULT_DEFAULT_CATEGORY;

        public Builder(final AbiquoContext context)
        {
            super();
            this.context = context;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder defaultCategory(final boolean defaultCategory)
        {
            this.defaultCategory = defaultCategory;
            return this;
        }

        public Builder erasable(final boolean erasable)
        {
            this.erasable = erasable;
            return this;
        }

        public Category build()
        {
            CategoryDto dto = new CategoryDto();
            dto.setErasable(erasable);
            dto.setDefaultCategory(defaultCategory);
            dto.setName(name);
            Category category = new Category(context, dto);

            return category;
        }

        public static Builder fromCategory(final Category in)
        {
            Builder builder =
                Category.builder(in.context).name(in.getName()).erasable(in.isErasable())
                    .defaultCategory(in.isDefaultCategory());

            return builder;
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getName()
    {
        return target.getName();
    }

    public boolean isDefaultCategory()
    {
        return target.isDefaultCategory();
    }

    public boolean isErasable()
    {
        return target.isErasable();
    }

    public void setDefaultCategory(final boolean defaultCategory)
    {
        target.setDefaultCategory(defaultCategory);
    }

    public void setErasable(final boolean erasable)
    {
        target.setErasable(erasable);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }
}