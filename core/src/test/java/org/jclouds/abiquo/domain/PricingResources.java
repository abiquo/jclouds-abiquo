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

import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.pricing.CostCodeDto;
import com.abiquo.server.core.pricing.CurrencyDto;

/**
 * Enterprise domain utilities.
 * 
 * @author Ignasi Barrera
 * @author Susana Acedo
 */
public class PricingResources
{

    public static CurrencyDto currencyPost()
    {
        CurrencyDto currency = new CurrencyDto();
        currency.setName("yuan");
        currency.setSymbol("¥");
        currency.setDigits(3);
        return currency;
    }

    public static CurrencyDto currencyPut()
    {
        CurrencyDto currency = new CurrencyDto();
        currency.setName("yuan");
        currency.setSymbol("¥");
        currency.setDigits(3);
        currency.setId(1);
        currency.addLink(new RESTLink("edit", "http://localhost/api/config/currencies/1"));
        return currency;
    }

    public static String currencyPostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<currency>");
        buffer.append("<symbol>¥</symbol>");
        buffer.append("<digits>3</digits>");
        buffer.append("<name>yuan</name>");
        buffer.append("</currency>");
        return buffer.toString();
    }

    public static String currencyPutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<currency>");
        buffer.append(link("/config/currencies/1", "edit"));
        buffer.append("<symbol>¥</symbol>");
        buffer.append("<digits>3</digits>");
        buffer.append("<id>1</id>");
        buffer.append("<name>yuan</name>");
        buffer.append("</currency>");
        return buffer.toString();
    }

    public static Object costcodePost()
    {
        CostCodeDto costcode = new CostCodeDto();
        costcode.setName("cost code");
        costcode.setDescription("description");
        return costcode;
    }

    public static Object costcodePut()
    {
        CostCodeDto costcode = new CostCodeDto();
        costcode.setName("cost code");
        costcode.setDescription("description");
        costcode.setId(1);
        costcode.addLink(new RESTLink("edit", "http://localhost/api/config/costcodes/1"));
        return costcode;
    }

    public static String costcodePostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<costCode>");
        buffer.append("<name>cost code</name>");
        buffer.append("<description>description</description>");
        buffer.append("</costCode>");
        return buffer.toString();
    }

    public static String costcodePutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<costCode>");
        buffer.append(link("/config/costcodes/1", "edit"));
        buffer.append("<description>description</description>");
        buffer.append("<id>1</id>");
        buffer.append("<name>cost code</name>");
        buffer.append("</costCode>");
        return buffer.toString();
    }

}
