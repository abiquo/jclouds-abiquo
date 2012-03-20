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

package org.jclouds.abiquo.domain.infrastructure;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;
import org.jclouds.abiquo.rest.internal.ExtendedUtils;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseXMLWithJAXB;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.infrastructure.BladeLocatorLedDto;
import com.abiquo.server.core.infrastructure.LogicServerDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.UcsRackDto;
import com.google.inject.TypeLiteral;

/**
 * Adds high level functionality to a {@link MachineDto} managed in a UCS rack.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI20/Machine+Resource
 */
public class Blade extends AbstractPhysicalMachine
{
    /** The rack where the machine belongs. */
    protected UcsRackDto rack;

    /**
     * Constructor to be used only by the builder.
     */
    protected Blade(final AbiquoContext context, final MachineDto target)
    {
        super(context, target);
        extractVirtualSwitches();
    }

    // Parent access

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/RackResource#RackResource-RetrieveaUCSRack" >
     *      http://community.abiquo.com/display/ABI20/RackResource#RackResource-RetrieveaUCSRack</a>
     */
    public ManagedRack getRack()
    {
        RESTLink link =
            checkNotNull(target.searchLink(ParentLinkName.RACK),
                ValidationErrors.MISSING_REQUIRED_LINK + " " + ParentLinkName.RACK);

        ExtendedUtils utils = (ExtendedUtils) context.getUtils();
        HttpResponse response = utils.getAbiquoHttpClient().get(link);

        ParseXMLWithJAXB<UcsRackDto> parser =
            new ParseXMLWithJAXB<UcsRackDto>(utils.getXml(), TypeLiteral.get(UcsRackDto.class));

        return wrap(context, ManagedRack.class, parser.apply(response));
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/MachineResource-RetrieveslogicserverassociatedwithamachineinaCiscoUCS"
     *      > http://community.abiquo.com/display/ABI20/MachineResource-
     *      RetrieveslogicserverassociatedwithamachineinaCiscoUCS</a>
     */
    public LogicServer getLogicServer()
    {
        LogicServerDto server = context.getApi().getInfrastructureClient().getLogicServer(target);

        return wrap(context, LogicServer.class, server);
    }

    // Actions

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/MachineResource#MachineResource-PoweroffanexistingmachineinCiscoUCS"
     *      > http://community.abiquo.com/display/ABI20/MachineResource#MachineResource-
     *      PoweroffanexistingmachineinCiscoUCS</a>
     */
    public void powerOff()
    {
        context.getApi().getInfrastructureClient().powerOff(target);
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/MachineResource#MachineResource-PoweronanexistingmachineinCiscoUCS"
     *      > http://community.abiquo.com/display/ABI20/MachineResource#MachineResource-
     *      PoweronanexistingmachineinCiscoUCS</a>
     */
    public void powerOn()
    {
        context.getApi().getInfrastructureClient().powerOn(target);
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/MachineResource-LightthelocatorledofanexistingmachineinCiscoUCS"
     *      > http://community.abiquo.com/display/ABI20/MachineResource-
     *      LightthelocatorledofanexistingmachineinCiscoUCS</a>
     */
    public void ledOn()
    {
        context.getApi().getInfrastructureClient().ledOn(target);
    }

    /**
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/MachineResource-LightoffthelocatorledofanexistingmachineinaCiscoUCS"
     *      > http://community.abiquo.com/display/ABI20/MachineResource-
     *      LightoffthelocatorledofanexistingmachineinaCiscoUCS</a>
     */
    public void ledOff()
    {
        context.getApi().getInfrastructureClient().ledOff(target);
    }

    public BladeLocatorLed getLocatorLed()
    {
        BladeLocatorLedDto led = context.getApi().getInfrastructureClient().getLocatorLed(target);

        return wrap(context, BladeLocatorLed.class, led);
    }
}
