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
import org.jclouds.abiquo.domain.config.Privilege;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.server.core.infrastructure.storage.StorageDeviceDto;
import com.abiquo.server.core.infrastructure.storage.StoragePoolDto;
import com.abiquo.server.core.infrastructure.storage.TierDto;

/**
 * Adds high level functionality to {@link StoragePoolDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Storage+Pool+Resource">
 *      http://community.abiquo.com/display/ABI20/Storage+Pool+Resource</a>
 */
public class StoragePool extends DomainWrapper<StoragePoolDto>
{
    /** The default value for the used space. */
    private static final long DEFAULT_USED_SIZE = 0;

    /** The datacenter where the storage device is. */
    // Package protected to allow navigation from children
    StorageDevice storageDevice;

    /**
     * Constructor to be used only by the builder.
     */
    protected StoragePool(final AbiquoContext context, final StoragePoolDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Pool+Resource#StoragePoolResource-DeleteaStoragePool">
     *      http://community.abiquo.com/display/ABI20/Storage+Pool+Resource#StoragePoolResource-DeleteaStoragePool</a>
     */
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteStoragePool(target);
        target = null;
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Pool+Resource#StoragePoolResource-CreateaStoragePoolWithaTierLink">
     *      http://community.abiquo.com/display/ABI20/Storage+Pool+Resource#StoragePoolResource-CreateaStoragePoolWithaTierLink</a>
     */
    public void save()
    {
        target =
            context.getApi().getInfrastructureClient().createStoragePool(storageDevice.unwrap(),
                target);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Pool+Resource#StoragePoolResource-UpdateaStoragePool">
     *      http://community.abiquo.com/display/ABI20/Storage+Pool+Resource#StoragePoolResource-UpdateaStoragePool</a>
     */
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateStoragePool(target);
    }

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Pool+Resource#StoragePoolResource-CreateaStoragePoolWithaTierLink">
     *      http://community.abiquo.com/display/ABI20/Storage+Pool+Resource#StoragePoolResource-CreateaStoragePoolWithaTierLink</a>
     */
    public void setTier(final Tier tier)
    {
        checkNotNull(tier, ValidationErrors.NULL_RESOURCE + Privilege.class);
        checkNotNull(tier.getId(), ValidationErrors.MISSING_REQUIRED_FIELD + " id in " + Tier.class);

        this.updateLink(target, ParentLinkName.TIER, tier.unwrap(), "edit");
    }

    // Parent access

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-RetrieveaStorageDevice">
     *      http://community.abiquo.com/display/ABI20/Storage+Device+Resource#StorageDeviceResource-RetrieveaStorageDevice</a>
     */
    public StorageDevice getStorageDevice()
    {
        Integer storageId = target.getIdFromLink(ParentLinkName.STORAGE_DEVICE);
        checkNotNull(storageId, ValidationErrors.MISSING_REQUIRED_LINK);

        StorageDeviceDto dto =
            context.getApi().getInfrastructureClient().getStorageDevice(
                storageDevice.datacenter.unwrap(), storageId);
        storageDevice = wrap(context, StorageDevice.class, dto);
        return storageDevice;
    }

    // Children access

    public Tier getTier()
    {
        Integer tierId = target.getIdFromLink(ParentLinkName.TIER);
        checkNotNull(tierId, ValidationErrors.MISSING_REQUIRED_LINK);

        TierDto dto =
            context.getApi().getInfrastructureClient().getTier(storageDevice.datacenter.unwrap(),
                tierId);
        return wrap(context, Tier.class, dto);
    }

    // Builder

    public static Builder builder(final AbiquoContext context, final StorageDevice storageDevice)
    {
        return new Builder(context, storageDevice);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private StorageDevice storageDevice;

        private Long availableSizeInMb;

        private Boolean enabled;

        private String name;

        private String idStorage;

        private Long totalSizeInMb;

        private Long usedSizeInMb = DEFAULT_USED_SIZE;

        public Builder(final AbiquoContext context, final StorageDevice storageDevice)
        {
            super();
            checkNotNull(storageDevice, ValidationErrors.NULL_RESOURCE + StorageDevice.class);
            this.storageDevice = storageDevice;
            this.context = context;
        }

        public Builder storageDevice(final StorageDevice storageDevice)
        {
            checkNotNull(storageDevice, ValidationErrors.NULL_RESOURCE + StorageDevice.class);
            this.storageDevice = storageDevice;
            return this;
        }

        public Builder availableSizeInMb(final long availableSizeInMb)
        {
            this.availableSizeInMb = availableSizeInMb;
            return this;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder enabled(final boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }

        public Builder idStorage(final String idStorage)
        {
            this.idStorage = idStorage;
            return this;
        }

        public Builder totalSizeInMb(final long totalSizeInMb)
        {
            this.totalSizeInMb = totalSizeInMb;
            return this;
        }

        public Builder usedSizeInMb(final long usedSizeInMb)
        {
            this.usedSizeInMb = usedSizeInMb;
            return this;
        }

        public StoragePool build()
        {
            StoragePoolDto dto = new StoragePoolDto();
            dto.setAvailableSizeInMb(availableSizeInMb);
            dto.setEnabled(enabled);
            dto.setName(name);
            dto.setIdStorage(idStorage);
            dto.setTotalSizeInMb(totalSizeInMb);
            dto.setUsedSizeInMb(usedSizeInMb);
            StoragePool storagePool = new StoragePool(context, dto);
            storagePool.storageDevice = storageDevice;
            return storagePool;
        }

        public static Builder fromStorageDevice(final StoragePool in)
        {
            Builder builder =
                StoragePool.builder(in.context, in.getStorageDevice()).availableSizeInMb(
                    in.getAvailableSizeInMb()).enabled(in.getEnabled())
                    .idStorage(in.getIdStorage()).name(in.getName()).totalSizeInMb(
                        in.getTotalSizeInMb()).usedSizeInMb(in.getUsedSizeInMb());

            return builder;
        }
    }

    // Delegate methods

    public long getAvailableSizeInMb()
    {
        return target.getAvailableSizeInMb();
    }

    public boolean getEnabled()
    {
        return target.getEnabled();
    }

    public String getName()
    {
        return target.getName();
    }

    public long getTotalSizeInMb()
    {
        return target.getTotalSizeInMb();
    }

    public long getUsedSizeInMb()
    {
        return target.getUsedSizeInMb();
    }

    public void setAvailableSizeInMb(final long availableSizeInMb)
    {
        target.setAvailableSizeInMb(availableSizeInMb);
    }

    public void setEnabled(final boolean enabled)
    {
        target.setEnabled(enabled);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setTotalSizeInMb(final long totalSizeInMb)
    {
        target.setTotalSizeInMb(totalSizeInMb);
    }

    public void setUsedSizeInMb(final long usedSizeInMb)
    {
        target.setUsedSizeInMb(usedSizeInMb);
    }

    public String getIdStorage()
    {
        return target.getIdStorage();
    }

    public void setIdStorage(final String idStorage)
    {
        target.setIdStorage(idStorage);
    }

}
