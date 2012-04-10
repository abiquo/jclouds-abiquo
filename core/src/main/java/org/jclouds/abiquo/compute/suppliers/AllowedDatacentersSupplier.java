package org.jclouds.abiquo.compute.suppliers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.abiquo.compute.functions.DatacenterToLocation;
import org.jclouds.abiquo.compute.strategy.AbiquoComputeServiceAdapter;
import org.jclouds.domain.Location;
import org.jclouds.location.suppliers.LocationsSupplier;
import org.jclouds.logging.Logger;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * Supplies the allowed datacenters for the current user.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class AllowedDatacentersSupplier implements LocationsSupplier
{
    @Resource
    protected Logger logger = Logger.NULL;

    private final AbiquoComputeServiceAdapter adapter;

    private final DatacenterToLocation datacenterToLocation;

    @Inject
    AllowedDatacentersSupplier(final AbiquoComputeServiceAdapter adapter,
        final DatacenterToLocation datacenterToLocation)
    {
        super();
        this.adapter = checkNotNull(adapter, "adapter");
        this.datacenterToLocation = checkNotNull(datacenterToLocation, "datacenterToLocation");
    }

    @Override
    public Set< ? extends Location> get()
    {
        return Sets.newHashSet(Iterables.transform(adapter.listLocations(), datacenterToLocation));
    }

}
