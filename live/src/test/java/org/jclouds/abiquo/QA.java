package org.jclouds.abiquo;

import java.util.Properties;

import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.infrastructure.RemoteService;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.abiquo.model.enumerator.RemoteServiceType;

public class QA
{
    private AbiquoContext context;

    @BeforeMethod
    public void setup()
    {
        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", "http://10.60.21.33/api");
        context = new AbiquoContextFactory().createContext("admin", "xabiquo", props);
    }

    @AfterMethod
    public void tearDown()
    {
        if (context != null)
        {
            context.close();
        }
    }

    @Test
    public void testIsAlive()
    {
        Datacenter dc = null;
        try
        {
            dc = Datacenter.builder(context).name("Ignasi").location("Honolulu").build();
            dc.save();

            RemoteService rs =
                RemoteService.builder(context, dc).type(RemoteServiceType.VIRTUAL_SYSTEM_MONITOR)
                    .ip("10.60.21.34").build();
            rs.save();

            boolean available = rs.isAvailable();

            System.out.println(available);
        }
        finally
        {
            if (dc != null)
            {
                dc.delete();
            }
        }

    }
}
