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

import static com.google.common.collect.Iterables.getOnlyElement;

import java.io.File;

import org.jclouds.abiquo.compute.options.AbiquoTemplateOptions;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.io.Payloads;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.rest.internal.ContextBuilder;
import org.jclouds.ssh.SshClient;
import org.jclouds.ssh.jsch.config.JschSshClientModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Abiquo node bootstrap example.
 * 
 * @author Ignasi Barrera
 */
public class AbiquoComputeExample
{
    public static void main(final String[] args) throws Exception
    {
        AbiquoContext context = ContextBuilder.newBuilder(new AbiquoApiMetadata()) //
            .endpoint("http://mothership.bcn.abiquo.com/api") //
            .credentials("ibarrera", "ibarrera") //
            .modules(ImmutableSet.<Module> of(new JschSshClientModule(), new SLF4JLoggingModule())) //
            .build();

        ComputeService compute = context.getComputeService();

        try
        {
            System.out.println("Loading template...");

            LoginCredentials login = LoginCredentials.builder() //
                .authenticateSudo(true) //
                .user("ubuntu") //
                .password("ubuntu") //
                .build();

            TemplateOptions options = compute.templateOptions() //
                .overrideLoginCredentials(login) //
                .runScript("mkdir ~/jclouds-uploads") //
                .as(AbiquoTemplateOptions.class).virtualDatacenter("Ignasi");

            Template template = compute.templateBuilder() //
                .imageNameMatches("Development Environment-HOTFIX 1") //
                .options(options) //
                .build();

            System.out.println("Deploying virtual machine and runnign bootstrap script...");
            NodeMetadata node = getOnlyElement(compute.createNodesInGroup("jclouds", 1, template));

            System.out.println("Uploading file...");
            uploadFile(context, node, "/home/ibarrera/Downloads/bootstrap.js");

            System.out.println("Moving file to upload directory...");
            compute.runScriptOnNode(node.getId(), "mv /tmp/bootstrap.js ~/jclouds-uploads");
        }
        catch (RunNodesException ex)
        {
            for (Throwable error : ex.getExecutionErrors().values())
            {
                System.err.println(error.getMessage());
            }
        }
        finally
        {
            context.close();
        }
    }

    private static void uploadFile(final AbiquoContext context, final NodeMetadata node,
        final String filePath)
    {
        File file = new File(filePath);
        SshClient ssh = context.getUtils().sshForNode().apply(node);
        try
        {
            ssh.connect();
            ssh.put("/tmp/" + file.getName(), Payloads.newFilePayload(file));
        }
        finally
        {
            if (ssh != null)
            {
                ssh.disconnect();
            }
        }
    }
}
