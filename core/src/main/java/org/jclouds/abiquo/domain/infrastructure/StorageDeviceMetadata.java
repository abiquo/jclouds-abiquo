package org.jclouds.abiquo.domain.infrastructure;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.rest.RestContext;

import com.abiquo.server.core.infrastructure.storage.StorageDeviceMetadataDto;

/**
 * metadata describing a Storage Device.
 * 
 * @author Ignasi Barrera
 */
public class StorageDeviceMetadata extends DomainWrapper<StorageDeviceMetadataDto>
{
    /**
     * Constructor to be used only by the builder.
     */
    protected StorageDeviceMetadata(final RestContext<AbiquoClient, AbiquoAsyncClient> context,
        final StorageDeviceMetadataDto target)
    {
        super(context, target);
    }

    // Delegate methods

    public String getType()
    {
        return target.getType();
    }

    public int getDefaultManagementPort()
    {
        return target.getDefaultManagementPort();
    }

    public int getDefaultIscsiPort()
    {
        return target.getDefaultIscsiPort();
    }

    public boolean requiresAuthentication()
    {
        return target.isRequiresAuthentication();
    }

}
