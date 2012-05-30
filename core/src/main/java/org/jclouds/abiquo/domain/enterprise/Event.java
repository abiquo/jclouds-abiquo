package org.jclouds.abiquo.domain.enterprise;

import java.util.Date;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.rest.RestContext;

import com.abiquo.server.core.event.EventDto;

public class Event extends DomainWrapper<EventDto>
{
    /**
     * Constructor to be used only by the builder.
     */
    protected Event(final RestContext<AbiquoClient, AbiquoAsyncClient> context,
        final EventDto target)
    {
        super(context, target);
    }

    // Builder

    public static Builder builder(final RestContext<AbiquoClient, AbiquoAsyncClient> context)
    {
        return new Builder(context);
    }

    public static class Builder
    {
        private RestContext<AbiquoClient, AbiquoAsyncClient> context;

        private String user;

        private String stacktrace;

        private String component;

        private String performedBy;

        private Integer idNetwork;

        private String idVolume;

        private String storagePool;

        private Date timestamp;

        private String virtualApp;

        private String datacenter;

        private String actionPerformed;

        private Integer idVirtualMachine;

        private String virtualDatacenter;

        private String enterprise;

        private String storageSystem;

        private Integer idPhysicalMachine;

        private String severity;

        private Integer idStorageSystem;

        private Integer idDatacenter;

        private String network;

        private String physicalMachine;

        private String rack;

        private Integer idVirtualDatacenter;

        private Integer idSubnet;

        private String volume;

        private String subnet;

        private Integer idUser;

        private String idStoragePool;

        private Integer idRack;

        private String virtualMachine;

        private Integer idVirtualApp;

        private Integer idEnterprise;

        public Builder(final RestContext<AbiquoClient, AbiquoAsyncClient> context)
        {
            super();
            this.context = context;
        }

        public Builder user(final String user)
        {
            this.user = user;
            return this;
        }

        public Builder virtualApp(final String virtualApp)
        {
            this.virtualApp = virtualApp;
            return this;
        }

        public Builder virtualDatacenter(final String virtualDatacenter)
        {
            this.virtualDatacenter = virtualDatacenter;
            return this;
        }

        public Builder virtualMachine(final String virtualMachine)
        {
            this.virtualMachine = virtualMachine;
            return this;
        }

        public Builder timestamp(final Date timestamp)
        {
            this.timestamp = timestamp;
            return this;
        }

        public Builder subnet(final String subnet)
        {
            this.subnet = subnet;
            return this;
        }

        public Builder storageSystem(final String storageSystem)
        {
            this.storageSystem = storageSystem;
            return this;
        }

        public Builder storagePool(final String storagePool)
        {
            this.storagePool = storagePool;
            return this;
        }

        public Builder stacktrace(final String stacktrace)
        {
            this.stacktrace = stacktrace;
            return this;
        }

        public Builder severity(final String severity)
        {
            this.severity = severity;
            return this;
        }

        public Builder rack(final String rack)
        {
            this.rack = rack;
            return this;
        }

        public Builder physicalMachine(final String physicalMachine)
        {
            this.physicalMachine = physicalMachine;
            return this;
        }

        public Builder performedBy(final String performedBy)
        {
            this.performedBy = performedBy;
            return this;
        }

        public Builder network(final String network)
        {
            this.network = network;
            return this;
        }

        public Builder enterprise(final String enterprise)
        {
            this.enterprise = enterprise;
            return this;
        }

        public Builder datacenter(final String datacenter)
        {
            this.datacenter = datacenter;
            return this;
        }

        public Builder component(final String component)
        {
            this.component = component;
            return this;
        }

        public Builder actionPerformed(final String actionPerformed)
        {
            this.actionPerformed = actionPerformed;
            return this;
        }

        public Builder idDatacenter(final Integer idDatacenter)
        {
            this.idDatacenter = idDatacenter;
            return this;
        }

        public Builder idEnterprise(final Integer idEnterprise)
        {
            this.idEnterprise = idEnterprise;
            return this;
        }

        public Builder idNetwork(final Integer idNetwork)
        {
            this.idNetwork = idNetwork;
            return this;
        }

        public Builder idPhysicalMachine(final Integer idPhysicalMachine)
        {
            this.idPhysicalMachine = idPhysicalMachine;
            return this;
        }

        public Builder idRack(final Integer idRack)
        {
            this.idRack = idRack;
            return this;
        }

        public Builder idStoragePool(final String idStoragePool)
        {
            this.idStoragePool = idStoragePool;
            return this;
        }

        public Builder idStorageSystem(final Integer idStorageSystem)
        {
            this.idStorageSystem = idStorageSystem;
            return this;
        }

        public Builder idSubnet(final Integer idSubnet)
        {
            this.idSubnet = idSubnet;
            return this;
        }

        public Builder idUser(final Integer idUser)
        {
            this.idUser = idUser;
            return this;
        }

        public Builder idVirtualApp(final Integer idVirtualApp)
        {
            this.idVirtualApp = idVirtualApp;
            return this;
        }

        public Builder idVirtualMachine(final Integer idVirtualMachine)
        {
            this.idVirtualMachine = idVirtualMachine;
            return this;
        }

        public Builder idVolume(final String idVolume)
        {
            this.idVolume = idVolume;
            return this;
        }

        public Event build()
        {
            EventDto dto = new EventDto();
            dto.setUser(user);
            dto.setActionPerformed(actionPerformed);
            dto.setComponent(component);
            dto.setDatacenter(datacenter);
            dto.setEnterprise(enterprise);
            dto.setIdDatacenter(idDatacenter);
            dto.setIdEnterprise(idEnterprise);
            dto.setIdNetwork(idNetwork);
            dto.setIdPhysicalMachine(idPhysicalMachine);
            dto.setIdRack(idRack);
            dto.setIdStoragePool(idStoragePool);
            dto.setIdStorageSystem(idStorageSystem);
            dto.setIdSubnet(idSubnet);
            dto.setIdUser(idUser);
            dto.setIdVirtualApp(idVirtualApp);
            dto.setIdVirtualDatacenter(idVirtualDatacenter);
            dto.setIdVirtualMachine(idVirtualMachine);
            dto.setIdVolume(idVolume);
            dto.setNetwork(network);
            dto.setPerformedBy(performedBy);
            dto.setPhysicalMachine(physicalMachine);
            dto.setRack(rack);
            dto.setSeverity(severity);
            dto.setStacktrace(stacktrace);
            dto.setStoragePool(storagePool);
            dto.setStorageSystem(storageSystem);
            dto.setSubnet(subnet);
            dto.setTimestamp(timestamp);
            dto.setUser(user);
            dto.setVirtualApp(virtualApp);
            dto.setVirtualDatacenter(virtualDatacenter);
            dto.setVirtualMachine(virtualMachine);
            dto.setVolume(volume);

            Event event = new Event(context, dto);

            return event;
        }

        public static Builder fromEvent(final Event in)
        {
            return Event.builder(in.context).user(in.getUser());
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getUser()
    {
        return target.getUser();
    }

    public void setUser(final String user)
    {
        target.setUser(user);
    }

    public String getStacktrace()
    {
        return target.getStacktrace();
    }

    public void setStacktrace(final String stacktrace)
    {
        target.setStacktrace(stacktrace);
    }

    public String getComponent()
    {
        return target.getComponent();
    }

    public void setComponent(final String component)
    {
        target.setComponent(component);
    }

    public String getPerformedBy()
    {
        return target.getPerformedBy();
    }

    public void setPerformedBy(final String performedBy)
    {
        target.setPerformedBy(performedBy);
    }

    public Integer getIdNetwork()
    {
        return target.getIdNetwork();
    }

    public void setIdNetwork(final Integer idNetwork)
    {
        target.setIdNetwork(idNetwork);
    }

    public String getIdVolume()
    {
        return target.getIdVolume();
    }

    public void setIdVolume(final String idVolume)
    {
        target.setIdVolume(idVolume);
    }

    public String getStoragePool()
    {
        return target.getStoragePool();
    }

    public void setStoragePool(final String storagePool)
    {
        target.setStoragePool(storagePool);
    }

    public Date getTimestamp()
    {
        return target.getTimestamp();
    }

    public void setTimestamp(final Date timestamp)
    {
        target.setTimestamp(timestamp);
    }

    public String getVirtualApp()
    {
        return target.getVirtualApp();
    }

    public void setVirtualApp(final String virtualApp)
    {
        target.setVirtualApp(virtualApp);
    }

    public String getDatacenter()
    {
        return target.getDatacenter();
    }

    public void setDatacenter(final String datacenter)
    {
        target.setDatacenter(datacenter);
    }

    public String getActionPerformed()
    {
        return target.getActionPerformed();
    }

    public void setActionPerformed(final String actionPerformed)
    {
        target.setActionPerformed(actionPerformed);
    }

    public Integer getIdVirtualMachine()
    {
        return target.getIdVirtualMachine();
    }

    public void setIdVirtualMachine(final Integer idVirtualMachine)
    {
        target.setIdVirtualMachine(idVirtualMachine);
    }

    public String getVirtualDatacenter()
    {
        return target.getVirtualDatacenter();
    }

    public void setVirtualDatacenter(final String virtualDatacenter)
    {
        target.setVirtualDatacenter(virtualDatacenter);
    }

    public String getEnterprise()
    {
        return target.getEnterprise();
    }

    public void setEnterprise(final String enterprise)
    {
        target.setEnterprise(enterprise);
    }

    public String getStorageSystem()
    {
        return target.getStorageSystem();
    }

    public void setStorageSystem(final String storageSystem)
    {
        target.setStorageSystem(storageSystem);
    }

    public Integer getIdPhysicalMachine()
    {
        return target.getIdPhysicalMachine();
    }

    public void setIdPhysicalMachine(final Integer idPhysicalMachine)
    {
        target.setIdPhysicalMachine(idPhysicalMachine);
    }

    public String getSeverity()
    {
        return target.getSeverity();
    }

    public void setSeverity(final String severity)
    {
        target.setSeverity(severity);
    }

    public Integer getIdStorageSystem()
    {
        return target.getIdStorageSystem();
    }

    public void setIdStorageSystem(final Integer idStorageSystem)
    {
        target.setIdStorageSystem(idStorageSystem);
    }

    public Integer getIdDatacenter()
    {
        return target.getIdDatacenter();
    }

    public void setIdDatacenter(final Integer idDatacenter)
    {
        target.setIdDatacenter(idDatacenter);
    }

    public String getNetwork()
    {
        return target.getNetwork();
    }

    public void setNetwork(final String network)
    {
        target.setNetwork(network);
    }

    public String getPhysicalMachine()
    {
        return target.getPhysicalMachine();
    }

    public void setPhysicalMachine(final String physicalMachine)
    {
        target.setPhysicalMachine(physicalMachine);
    }

    public String getRack()
    {
        return target.getRack();
    }

    public void setRack(final String rack)
    {
        target.setRack(rack);
    }

    public Integer getIdVirtualDatacenter()
    {
        return target.getIdVirtualDatacenter();
    }

    public void setIdVirtualDatacenter(final Integer idVirtualDatacenter)
    {
        target.setIdVirtualDatacenter(idVirtualDatacenter);
    }

    public Integer getIdSubnet()
    {
        return target.getIdSubnet();
    }

    public void setIdSubnet(final Integer idSubnet)
    {
        target.setIdSubnet(idSubnet);
    }

    public String getVolume()
    {
        return target.getVolume();
    }

    public void setVolume(final String volume)
    {
        target.setVolume(volume);
    }

    public String getSubnet()
    {
        return target.getSubnet();
    }

    public void setSubnet(final String subnet)
    {
        target.setSubnet(subnet);
    }

    public Integer getIdUser()
    {
        return target.getIdUser();
    }

    public void setIdUser(final Integer idUser)
    {
        target.setIdUser(idUser);
    }

    public String getIdStoragePool()
    {
        return target.getIdStoragePool();
    }

    public void setIdStoragePool(final String idStoragePool)
    {
        target.setIdStoragePool(idStoragePool);
    }

    public Integer getIdRack()
    {
        return target.getIdRack();
    }

    public void setIdRack(final Integer idRack)
    {
        target.setIdRack(idRack);
    }

    public String getVirtualMachine()
    {
        return target.getVirtualMachine();
    }

    public void setVirtualMachine(final String virtualMachine)
    {
        target.setVirtualMachine(virtualMachine);
    }

    public Integer getIdVirtualApp()
    {
        return target.getIdVirtualApp();
    }

    public void setIdVirtualApp(final Integer idVirtualApp)
    {
        target.setIdVirtualApp(idVirtualApp);
    }

    public Integer getIdEnterprise()
    {
        return target.getIdEnterprise();
    }

    public void setIdEnterprise(final Integer idEnterprise)
    {
        target.setIdEnterprise(idEnterprise);
    }

    @Override
    public String toString()
    {
        return "Event [id=" + getId() + ", idUser=" + getIdUser() + ", user=" + getUser()
            + ", idEnterprise=" + getIdEnterprise() + ", enterprise=" + getEnterprise()
            + ", actionPerformed=" + getActionPerformed() + ", component=" + getComponent()
            + ", idDatacenter=" + getIdDatacenter() + ", datacenter=" + getDatacenter()
            + ", idStoragePool=" + getIdStoragePool() + ", storagePool=" + getStoragePool()
            + ", idVolume=" + getIdVolume() + ", volume=" + getVolume() + ", idNetwork="
            + getIdNetwork() + ", network=" + getNetwork() + ", idPhysicalMachine="
            + getIdPhysicalMachine() + ", physicalMachine=" + getPhysicalMachine() + ", idRack="
            + getIdRack() + ", rack=" + getRack() + ", idStorageSystem=" + getIdStorageSystem()
            + ", storageSystem=" + getStorageSystem() + ", idSubnet=" + getIdSubnet() + ", subnet="
            + getSubnet() + ", idVirtualApp=" + getIdVirtualApp() + ", virtualApp="
            + getVirtualApp() + ", idVirtualDatacenter=" + getIdVirtualDatacenter()
            + ", virtualDatacenter=" + getVirtualDatacenter() + ", idVirtualMachine="
            + getIdVirtualMachine() + ", virtualMachine=" + getVirtualMachine() + ", stackstrace="
            + getStacktrace() + ", performedBy=" + getPerformedBy() + ", severity=" + getSeverity()
            + "]";
    }
}
