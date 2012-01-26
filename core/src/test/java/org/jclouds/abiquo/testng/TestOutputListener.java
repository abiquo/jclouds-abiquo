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

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Prints each test and its execution time.
 * 
 * @author Ignasi Barrera
 */
public class TestOutputListener extends TestListenerAdapter
{

    @Override
    public void onTestFailure(final ITestResult tr)
    {
        System.out.println(String.format("FAIL %s [%s] Cause: %s", testMethod(tr), execTime(tr), tr
            .getThrowable().getMessage()));
    }

    @Override
    public void onTestSkipped(final ITestResult tr)
    {
        System.out.println(String.format("SKIP %s [%s]", testMethod(tr), execTime(tr)));
    }

    @Override
    public void onTestSuccess(final ITestResult tr)
    {
        System.out.println(String.format("OK   %s [%s]", testMethod(tr), execTime(tr)));
    }

    private static String testMethod(final ITestResult tr)
    {
        return tr.getTestClass().getRealClass().getSimpleName() + "."
            + tr.getMethod().getMethodName();
    }

    private static String execTime(final ITestResult tr)
    {
        return tr.getEndMillis() - tr.getStartMillis() + " ms";
    }
}
