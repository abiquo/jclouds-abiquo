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

package org.jclouds.abiquo.domain.cloud;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.filter;

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.model.transport.AcceptedRequestDto;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualApplianceState;
import com.abiquo.server.core.cloud.VirtualApplianceStateDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.abiquo.server.core.cloud.VirtualMachineTaskDto;
import com.abiquo.server.core.cloud.VirtualMachinesDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Adds high level functionality to {@link VirtualApplianceDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see <a href="http://community.abiquo.com/display/ABI20/Virtual+Appliance+Resource">
 *      http://community.abiquo.com/display/ABI20/Virtual+Appliance+Resource</a>
 */
public class VirtualAppliance extends DomainWrapper<VirtualApplianceDto>
{
    /** The virtual datacenter where the virtual appliance belongs. */
    // Package protected to allow navigation from children
    VirtualDatacenter virtualDatacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected VirtualAppliance(final AbiquoContext context, final VirtualApplianceDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * Delete the virtual appliance.
     */
    public void delete()
    {
        context.getApi().getCloudClient().deleteVirtualAppliance(target);
        target = null;
    }

    /**
     * Create the virtual appliance in the selected virtual datacenter.
     */
    public void save()
    {
        target =
            context.getApi().getCloudClient()
                .createVirtualAppliance(virtualDatacenter.unwrap(), target);
    }

    /**
     * Update the virtual appliance information when some of its properties have changed.
     */
    public void update()
    {
        target = context.getApi().getCloudClient().updateVirtualAppliance(target);
    }

    // Parent access

    /**
     * Get the virtual datacenter where the virtual appliance belongs to.
     * 
     * @resturn The virtual datacenter where the virtual appliance belongs to.
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-RetrieveaVirtualDatacenter">
     *      http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-RetrieveaVirtualDatacenter</a>
     */
    public VirtualDatacenter getVirtualDatacenter()
    {
        Integer virtualDatacenterId = target.getIdFromLink(ParentLinkName.VIRTUAL_DATACENTER);
        VirtualDatacenterDto dto =
            context.getApi().getCloudClient().getVirtualDatacenter(virtualDatacenterId);
        virtualDatacenter = wrap(context, VirtualDatacenter.class, dto);
        return virtualDatacenter;
    }

    /**
     * Get the enterprise where the virtual appliance belongs to.
     * 
     * @return The enterprise where the virtual appliance belongs to.
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-RetrieveaEnterprise">
     *      http://community.abiquo.com/display/ABI20/Enterprise+Resource#EnterpriseResource-RetrieveaEnterprise</a>
     */
    public Enterprise getEnterprise()
    {
        Integer enterpriseId = target.getIdFromLink(ParentLinkName.ENTERPRISE);
        EnterpriseDto dto = context.getApi().getEnterpriseClient().getEnterprise(enterpriseId);
        return wrap(context, Enterprise.class, dto);
    }

    /**
     * Get the current state of the virtual appliance.
     * 
     * @return The current state of the virtual appliance.
     */
    public VirtualApplianceState getState()
    {
        VirtualApplianceStateDto stateDto =
            context.getApi().getCloudClient().getVirtualApplianceState(target);
        return stateDto.getPower();
    }

    // Children access

    /**
     * Get the list of virtual machines in the virtual appliance.
     * 
     * @return The list of virtual machines in the virtual appliance.
     * @see <a
     *      href="http://community.abiquo.com/display/ABI18/Virtual+Machine+Resource#VirtualMachineResource-RetrievethelistofVirtualMachines.">
     *      http://community.abiquo.com/display/ABI18/Virtual+Machine+Resource#VirtualMachineResource-RetrievethelistofVirtualMachines.</a>
     */
    public List<VirtualMachine> listVirtualMachines()
    {
        VirtualMachinesDto vms = context.getApi().getCloudClient().listVirtualMachines(target);
        return wrap(context, VirtualMachine.class, vms.getCollection());
    }

    /**
     * Get the list of virtual machines in the virtual appliance matching the given filter.
     * 
     * @param filter The filter to apply.
     * @return The list of virtual machines in the virtual appliance matching the given filter.
     */
    public List<VirtualMachine> listVirtualMachines(final Predicate<VirtualMachine> filter)
    {
        return Lists.newLinkedList(filter(listVirtualMachines(), filter));
    }

    /**
     * Get a single virtual machine in the virtual appliance matching the given filter.
     * 
     * @param filter The filter to apply.
     * @return The virtual machine or <code>null</code> if none matched the given filter.
     */
    public VirtualMachine findVirtualMachine(final Predicate<VirtualMachine> filter)
    {
        return Iterables.getFirst(filter(listVirtualMachines(), filter), null);
    }

    /**
     * Get a concrete virtual machine in the virtual appliance.
     * 
     * @param id The id of the virtual machine.
     * @return The requested virtual machine.
     */
    public VirtualMachine getVirtualMachine(final Integer id)
    {
        VirtualMachineDto vm = context.getApi().getCloudClient().getVirtualMachine(target, id);
        return wrap(context, VirtualMachine.class, vm);
    }

    // Actions

    /**
     * Deploy the virtual appliance.
     * <p>
     * This method will start the deployment of all the virtual machines in the virtual appliance,
     * and will return an {@link AsyncTask} reference for each deployment operation. The deployment
     * will finish when all individual tasks finish.
     * 
     * @return The list of tasks corresponding to the deploy process of each virtual machine in the
     *         appliance.
     */
    public AsyncTask[] deploy()
    {
        return deploy(false);
    }

    /**
     * Deploy the virtual appliance.
     * <p>
     * This method will start the deployment of all the virtual machines in the virtual appliance,
     * and will return an {@link AsyncTask} reference for each deploy operation. The deployment will
     * finish when all individual tasks finish.
     * 
     * @param forceEnterpriseSoftLimits Boolean indicating if the deployment must be executed even
     *            if the enterprise soft limits are reached.
     * @return The list of tasks corresponding to the deploy process of each virtual machine in the
     *         appliance.
     */
    public AsyncTask[] deploy(final boolean forceEnterpriseSoftLimits)
    {
        VirtualMachineTaskDto force = new VirtualMachineTaskDto();
        force.setForceEnterpriseSoftLimits(forceEnterpriseSoftLimits);

        AcceptedRequestDto<String> response =
            context.getApi().getCloudClient().deployVirtualAppliance(unwrap(), force);

        return getTasks(response);
    }

    /**
     * Undeploy the virtual appliance.
     * <p>
     * This method will start the undeploy of all the virtual machines in the virtual appliance, and
     * will return an {@link AsyncTask} reference for each undeploy operation. The undeploy will
     * finish when all individual tasks finish.
     * 
     * @return The list of tasks corresponding to the undeploy process of each virtual machine in
     *         the appliance.
     */
    public AsyncTask[] undeploy()
    {
        return undeploy(false);
    }

    /**
     * Undeploy the virtual appliance.
     * <p>
     * This method will start the undeploy of all the virtual machines in the virtual appliance, and
     * will return an {@link AsyncTask} reference for each undeploy operation. The undeploy will
     * finish when all individual tasks finish.
     * 
     * @param forceUndeploy Boolean flag to force the undeploy even if the virtual appliance
     *            contains imported virtual machines.
     * @return The list of tasks corresponding to the undeploy process of each virtual machine in
     *         the appliance.
     */
    public AsyncTask[] undeploy(final boolean forceUndeploy)
    {
        VirtualMachineTaskDto force = new VirtualMachineTaskDto();
        force.setForceUndeploy(forceUndeploy);

        AcceptedRequestDto<String> response =
            context.getApi().getCloudClient().undeployVirtualAppliance(unwrap(), force);

        return getTasks(response);
    }

    // Builder

    public static Builder builder(final AbiquoContext context,
        final VirtualDatacenter virtualDatacenter)
    {
        return new Builder(context, virtualDatacenter);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private String name;

        private VirtualDatacenter virtualDatacenter;

        public Builder(final AbiquoContext context, final VirtualDatacenter virtualDatacenter)
        {
            super();
            checkNotNull(virtualDatacenter, ValidationErrors.NULL_RESOURCE
                + VirtualDatacenter.class);
            this.virtualDatacenter = virtualDatacenter;
            this.context = context;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder virtualDatacenter(final VirtualDatacenter virtualDatacenter)
        {
            checkNotNull(virtualDatacenter, ValidationErrors.NULL_RESOURCE
                + VirtualDatacenter.class);
            this.virtualDatacenter = virtualDatacenter;
            return this;
        }

        public VirtualAppliance build()
        {
            VirtualApplianceDto dto = new VirtualApplianceDto();
            dto.setName(name);

            VirtualAppliance virtualAppliance = new VirtualAppliance(context, dto);
            virtualAppliance.virtualDatacenter = virtualDatacenter;

            return virtualAppliance;
        }

        public static Builder fromVirtualAppliance(final VirtualAppliance in)
        {
            return VirtualAppliance.builder(in.context, in.virtualDatacenter).name(in.getName());
        }
    }

    // Delegate methods

    public int getError()
    {
        return target.getError();
    }

    public int getHighDisponibility()
    {
        return target.getHighDisponibility();
    }

    public Integer getId()
    {
        return target.getId();
    }

    public String getName()
    {
        return target.getName();
    }

    public int getPublicApp()
    {
        return target.getPublicApp();
    }

    public void setHighDisponibility(final int highDisponibility)
    {
        target.setHighDisponibility(highDisponibility);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPublicApp(final int publicApp)
    {
        target.setPublicApp(publicApp);
    }

    @Override
    public String toString()
    {
        return "VirtualAppliance [id=" + getId() + ", name=" + getName() + "]";
    }

}
