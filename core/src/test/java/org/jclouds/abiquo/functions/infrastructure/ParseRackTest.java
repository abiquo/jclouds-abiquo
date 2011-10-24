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

package org.jclouds.abiquo.functions.infrastructure;

import static org.jclouds.abiquo.domain.DomainUtils.withHeader;
import static org.testng.Assert.assertEquals;

import org.jclouds.abiquo.domain.Infrastructure;
import org.jclouds.abiquo.functions.ParseXMLTest;
import org.jclouds.abiquo.xml.internal.JAXBParser;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.RackDto;
import com.google.inject.TypeLiteral;

/**
 * Test Datacenters parsing.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class ParseRackTest extends ParseXMLTest<RackDto>
{

    @Override
    protected ParseRack getParser()
    {
        return new ParseRack(new JAXBParser(), TypeLiteral.get(RackDto.class));
    }

    @Override
    protected String getPayload()
    {
        return withHeader(Infrastructure.rackPostPayload());
    }

    @Override
    protected void verifyObject(final RackDto object)
    {
        verifyRack(object);
    }

    static void verifyRack(final RackDto rack)
    {
        assertEquals(rack.getName(), "Aloha");
        assertEquals(rack.getShortDescription(), "A hawaian rack");
        assertEquals(rack.isHaEnabled(), false);
        assertEquals(rack.getVlanIdMin(), Integer.valueOf(6));
        assertEquals(rack.getVlanIdMax(), Integer.valueOf(3024));
        assertEquals(rack.getVlanPerVdcReserved(), Integer.valueOf(6));
        assertEquals(rack.getNrsq(), Integer.valueOf(80));
    }

}
