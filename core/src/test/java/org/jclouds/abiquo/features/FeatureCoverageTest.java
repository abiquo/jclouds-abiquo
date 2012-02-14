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
package org.jclouds.abiquo.features;

import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jclouds.abiquo.config.AbiquoRestClientModule;
import org.testng.annotations.Test;

/**
 * Tests that all features have a unit test.
 * <p>
 * This test is disabled by default, since it may fail due to unit tests having different method
 * names. However, it can be enabled and used to have an idea of the feature test coverage.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit", enabled = false)
public class FeatureCoverageTest
{
    public void testAllFeaturesHaveTest() throws ClassNotFoundException
    {
        List<Method> missingTests = new ArrayList<Method>();
        Collection<Class< ? >> featureClasses = AbiquoRestClientModule.DELEGATE_MAP.values();

        for (Class< ? > featureClass : featureClasses)
        {
            Class< ? > testClass = loadTestClass(featureClass);

            for (Method method : featureClass.getMethods())
            {
                try
                {
                    String testName = "test" + capitalize(method.getName());
                    testClass.getMethod(testName);
                }
                catch (NoSuchMethodException e)
                {
                    missingTests.add(method);
                }
            }
        }

        if (!missingTests.isEmpty())
        {
            StringBuffer sb = new StringBuffer("Missing tests: ");
            for (Method method : missingTests)
            {
                sb.append("\n");
                sb.append(method.getDeclaringClass().getSimpleName());
                sb.append(".").append(method.getName());
            }

            fail(sb.toString());
        }
    }

    private Class< ? > loadTestClass(final Class< ? > featureClass) throws ClassNotFoundException
    {
        String testClassName =
            this.getClass().getPackage().getName() + "." + featureClass.getSimpleName() + "Test";
        return Thread.currentThread().getContextClassLoader().loadClass(testClassName);
    }

    private static String capitalize(final String str)
    {
        if (str == null)
        {
            return null;
        }

        switch (str.length())
        {
            case 0:
                return str;
            case 1:
                return str.toUpperCase();
            default:
                return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }
}
