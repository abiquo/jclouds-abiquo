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
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.model.enumerator.StorageTechnologyType;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.storage.StorageDeviceDto;

/**
 * Adds high level functionality to {@link StorageDeviceDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI20/Storage+Device+Resource
 */
public class StorageDevice extends DomainWrapper<StorageDeviceDto>
{
    /** The datacenter where the storage device is. */
    // Package protected to allow navigation from children
    Datacenter datacenter;

    StorageDeviceDto target;

    /**
     * Constructor to be used only by the builder.
     */
    protected StorageDevice(final AbiquoContext context, final StorageDeviceDto target)
    {
        super(context, target);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-DeleteaStorageDevice">
     *      http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-DeleteaStorageDevice</a>
     */
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteStorageDevice(target);
        target = null;
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-CreateaStorageDevice">
     *      http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-CreateaStorageDevice</a>
     */
    public void save()
    {
        target =
            context.getApi().getInfrastructureClient().createStorageDevice(datacenter.unwrap(),
                target);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-UpdateaStorageDevice">
     *      http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-UpdateaStorageDevice</a>
     */
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateStorageDevice(target);
    }

    // Parent access

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-RetrieveaDatacenter">
     *      http://community.abiquo.com/display/ABI20/Datacenter+Resource#DatacenterResource-RetrieveaDatacenter</a>
     */
    public Datacenter getDatacenter()
    {
        Integer datacenterId = target.getIdFromLink(ParentLinkName.DATACENTER);
        DatacenterDto dto = context.getApi().getInfrastructureClient().getDatacenter(datacenterId);
        datacenter = wrap(context, Datacenter.class, dto);
        return datacenter;
    }

    public static Builder builder(final AbiquoContext context, final Datacenter datacenter)
    {
        return new Builder(context, datacenter);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private Datacenter datacenter;

        private String iscsiIp;

        private Integer iscsiPort;

        private String managementIp;

        private Integer managementPort;

        private String name;

        private String password;

        private StorageTechnologyType storageTechnology;

        private String username;

        public Builder(final AbiquoContext context, final Datacenter datacenter)
        {
            super();
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Datacenter.class);
            this.datacenter = datacenter;
            this.context = context;
        }

        public Builder datacenter(final Datacenter datacenter)
        {
            checkNotNull(datacenter, ValidationErrors.NULL_RESOURCE + Datacenter.class);
            this.datacenter = datacenter;
            return this;
        }

        public Builder iscsiIp(final String iscsiIp)
        {
            this.iscsiIp = iscsiIp;
            return this;
        }

        public Builder iscsiPort(final int iscsiPort)
        {
            this.iscsiPort = iscsiPort;
            return this;
        }

        public Builder password(final String password)
        {
            this.password = password;
            return this;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder managementPort(final int managementPort)
        {
            this.managementPort = managementPort;
            return this;
        }

        public Builder managementIp(final String managementIp)
        {
            this.managementIp = managementIp;
            return this;
        }

        public Builder storageTechnology(final StorageTechnologyType storageTechnology)
        {
            this.storageTechnology = storageTechnology;
            return this;
        }

        public Builder username(final String username)
        {
            this.username = username;
            return this;
        }

        public StorageDevice build()
        {
            StorageDeviceDto dto = new StorageDeviceDto();
            dto.setIscsiIp(iscsiIp);
            dto.setIscsiPort(iscsiPort);
            dto.setManagementIp(managementIp);
            dto.setManagementPort(managementPort);
            dto.setName(name);
            dto.setPassword(password);
            dto.setStorageTechnology(storageTechnology);
            dto.setUsername(username);
            StorageDevice storageDevice = new StorageDevice(context, dto);
            storageDevice.datacenter = datacenter;
            return storageDevice;
        }

        public static Builder fromStorageDevice(final StorageDevice in)
        {
            Builder builder =
                StorageDevice.builder(in.context, in.getDatacenter()).iscsiIp(in.getIscsiIp())
                    .iscsiPort(in.getIscsiPort()).managementIp(in.getManagementIp())
                    .managementPort(in.getManagementPort()).name(in.getName()).password(
                        in.getPassword()).storageTechnology(in.getStorageTechnology()).username(
                        in.getUsername());

            return builder;
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getIscsiIp()
    {
        return target.getIscsiIp();
    }

    public int getIscsiPort()
    {
        return target.getIscsiPort();
    }

    public String getManagementIp()
    {
        return target.getManagementIp();
    }

    public int getManagementPort()
    {
        return target.getManagementPort();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getPassword()
    {
        return target.getPassword();
    }

    public StorageTechnologyType getStorageTechnology()
    {
        return target.getStorageTechnology();
    }

    public String getUsername()
    {
        return target.getUsername();
    }

    public void setIscsiIp(final String iscsiIp)
    {
        target.setIscsiIp(iscsiIp);
    }

    public void setIscsiPort(final int iscsiPort)
    {
        target.setIscsiPort(iscsiPort);
    }

    public void setManagementIp(final String managementIp)
    {
        target.setManagementIp(managementIp);
    }

    public void setManagementPort(final int managementPort)
    {
        target.setManagementPort(managementPort);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPassword(final String password)
    {
        target.setPassword(password);
    }

    public void setStorageTechnology(final StorageTechnologyType storageTechnology)
    {
        target.setStorageTechnology(storageTechnology);
    }

    public void setUsername(final String username)
    {
        target.setUsername(username);
    }
}
