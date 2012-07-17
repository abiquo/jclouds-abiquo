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

package org.jclouds.abiquo.testng;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Utility class to log the results of methods invoked insite the setup or tearwodn methods.
 * 
 * @author Ignasi Barrera
 */
public class TestRunner
{
    /** The result logger. */
    private static TestOutputListener OUTPUT = new TestOutputListener();

    /**
     * Executes the given method and logs the output.
     * 
     * @param target The target object.
     * @param methodName The target method.
     * @throws If the method excecution fails.
     */
    public static Object runAndLog(final Object target, final String methodName,
        final Object... args) throws Exception
    {

        Class< ? >[] argClasses = new Class< ? >[args.length];
        for (int i = 0; i < args.length; i++)
        {
            argClasses[i] = args[i].getClass();
        }

        long start = System.currentTimeMillis();
        String testName = null;

        try
        {
            Method m = findMethod(target.getClass(), methodName, argClasses);
            testName = m.getDeclaringClass().getSimpleName() + "." + methodName;

            m.setAccessible(true);
            Object result = m.invoke(target, args);

            OUTPUT.success(testName, System.currentTimeMillis() - start);

            return result;
        }
        catch (Exception ex)
        {
            OUTPUT.fail(testName, System.currentTimeMillis() - start, ex.getMessage());
            throw ex;
        }
    }

    /**
     * Find the given method in the superclass hierarchy.
     * 
     * @param targetClass The class too lookup.
     * @param name The name of the method.
     * @param parameterTypes The argument types.
     * @return The requested method.
     * @throws NoSuchMethodException If the method does not exist.
     */
    private static Method findMethod(final Class< ? > targetClass, final String name,
        final Class< ? >... parameterTypes) throws NoSuchMethodException
    {
        Class< ? > current = targetClass;

        while (!Object.class.equals(current) && current != null)
        {
            for (final Method method : current.getDeclaredMethods())
            {
                if (name.equals(method.getName())
                    && Arrays.equals(parameterTypes, method.getParameterTypes()))
                {
                    return method;
                }
            }

            current = current.getSuperclass();
        }

        throw new NoSuchMethodException("Method " + name + "not found in " + targetClass.getName());
    }
}
