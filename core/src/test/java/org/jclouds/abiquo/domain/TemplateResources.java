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

package org.jclouds.abiquo.domain;

import static org.jclouds.abiquo.domain.DomainUtils.link;

import com.abiquo.model.enumerator.ConversionState;
import com.abiquo.model.enumerator.DiskFormatType;
import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.appslibrary.ConversionDto;
import com.abiquo.server.core.appslibrary.ConversionRequestDto;
import com.abiquo.server.core.appslibrary.VirtualMachineTemplateDto;

/**
 * VM template domain utilities.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class TemplateResources
{
    public static VirtualMachineTemplateDto virtualMachineTemplatePut()
    {
        VirtualMachineTemplateDto template = new VirtualMachineTemplateDto();
        template.setName("Template");
        template.setId(1);
        template.setDescription("Description");
        template
            .addLink(new RESTLink("edit",
                "http://localhost/api/admin/enterprises/1/datacenterrepositories/1/virtualmachinetemplates/1"));
        template.addLink(new RESTLink("enterprise", "http://localhost/api/admin/enterprises/1"));
        template.addLink(new RESTLink("convert", "http://localhost/api/admin/enterprises/1"
            + "/datacenterrepositories/1/virtualmachinetemplates/1/action/convert"));
        template.addLink(new RESTLink("conversions", "http://localhost/api/admin/enterprises/1"
            + "/datacenterrepositories/1/virtualmachinetemplates/1/conversions"));
        template.addLink(new RESTLink("tasks", "http://localhost/api/admin/enterprises/1"
            + "/datacenterrepositories/1/virtualmachinetemplates/1/tasks"));

        return template;
    }

    public static String virtualMachineTemplatePutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualMachineTemplate>");
        buffer.append(link(
            "/admin/enterprises/1/datacenterrepositories/1/virtualmachinetemplates/1", "edit"));
        buffer.append(link("/admin/enterprises/1", "enterprise"));
        buffer.append(link("/admin/enterprises/1"
            + "/datacenterrepositories/1/virtualmachinetemplates/1/action/convert", "convert"));
        buffer.append(link("/admin/enterprises/1"
            + "/datacenterrepositories/1/virtualmachinetemplates/1/conversions", "conversions"));

        buffer.append(link("/admin/enterprises/1"
            + "/datacenterrepositories/1/virtualmachinetemplates/1/tasks", "tasks"));
        buffer.append("<id>1</id>");
        buffer.append("<name>Template</name>");
        buffer.append("<description>Description</description>");
        buffer.append("<diskFileSize>0</diskFileSize>");
        buffer.append("<cpuRequired>0</cpuRequired>");
        buffer.append("<ramRequired>0</ramRequired>");
        buffer.append("<hdRequired>0</hdRequired>");
        buffer.append("<shared>false</shared>");
        buffer.append("<costCode>0</costCode>");
        buffer.append("<chefEnabled>false</chefEnabled>");
        buffer.append("</virtualMachineTemplate>");
        return buffer.toString();
    }

    public static ConversionRequestDto conversionRequestPut()
    {
        ConversionRequestDto req = new ConversionRequestDto();
        req.setFormat(DiskFormatType.RAW);
        return req;
    }

    public static String conversionRequestPutPlayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<conversionRequest>");
        buffer.append("<format>RAW</format>");
        buffer.append("</conversionRequest>");
        return buffer.toString();
    }

    public static ConversionDto conversionPut()
    {
        ConversionDto conversion = new ConversionDto();
        conversion.setState(ConversionState.ENQUEUED);
        conversion.setSourceFormat(DiskFormatType.VMDK_STREAM_OPTIMIZED);
        conversion.setSourcePath("source/path.vmkd");
        conversion.setTargetFormat(DiskFormatType.RAW);
        conversion.setTargetPath("target/path.raw");
        conversion.setTargetSizeInBytes(1000000l);

        conversion
            .addLink(new RESTLink("edit",
                "http://localhost/api/admin/enterprises/1/datacenterrepositories/1/virtualmachinetemplates/1/conversions/RAW"));

        return conversion;
    }

    public static String conversionPutPlayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<conversion>");
        buffer
            .append(link(
                "/admin/enterprises/1/datacenterrepositories/1/virtualmachinetemplates/1/conversions/RAW",
                "edit"));
        buffer.append("<state>ENQUEUED</state>");
        buffer.append("<sourceFormat>VMDK_STREAM_OPTIMIZED</sourceFormat>");
        buffer.append("<sourcePath>source/path.vmkd</sourcePath>");
        buffer.append("<targetFormat>RAW</targetFormat>");
        buffer.append("<targetPath>target/path.raw</targetPath>");
        buffer.append("<targetSizeInBytes>1000000</targetSizeInBytes>");
        buffer.append("</conversion>");
        return buffer.toString();
    }
}
