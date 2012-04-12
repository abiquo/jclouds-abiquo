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
package org.jclouds.abiquo;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.predicates.NodePredicates;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.rest.internal.ContextBuilder;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Abiquo node bootstrap example.
 * 
 * @author Ignasi Barrera
 */
public class AbiquoComputeExample
{
    public static void main(final String[] args) throws RunNodesException
    {
        AbiquoContext context = ContextBuilder.newBuilder(new AbiquoApiMetadata()) //
            .endpoint("http://localhost/api") //
            .credentials("ent", "xabiquo") //
            .modules(ImmutableSet.<Module> of(new SLF4JLoggingModule())) //
            .build();

        ComputeService compute = context.getComputeService();

        try
        {
            Template template = compute.templateBuilder().imageNameMatches("m0n0wall-vhd").build();
            compute.createNodesInGroup("jclouds-m0n0wall", 1, template);
            compute.destroyNodesMatching(NodePredicates.inGroup("jclouds-m0n0wall"));
        }
        finally
        {
            context.close();
        }
    }
}
