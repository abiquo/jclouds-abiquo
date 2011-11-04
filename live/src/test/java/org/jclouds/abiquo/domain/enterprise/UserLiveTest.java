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

import static org.jclouds.abiquo.util.Assert.assertHasError;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.environment.EnterpriseTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

import com.abiquo.server.core.enterprise.UserDto;

/**
 * Live integration tests for the {@link User} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class UserLiveTest extends BaseAbiquoClientLiveTest<EnterpriseTestEnvironment>
{

    @Override
    protected EnterpriseTestEnvironment environment(final AbiquoContext context)
    {
        return new EnterpriseTestEnvironment(context);
    }

    public void testUpdate()
    {
        env.user.setName("Manolo");
        env.user.update();

        // Recover the updated user
        UserDto updated = env.enterpriseClient.getUser(env.enterprise.unwrap(), env.user.getId());

        assertEquals(updated.getName(), "Manolo");
    }

    public void testCreateRepeated()
    {
        User repeated = User.Builder.fromUser(env.user).build();

        try
        {
            repeated.save();
            fail("Should not be able to create users with the same nick");
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.CONFLICT, "USER-4");
        }
    }
}
