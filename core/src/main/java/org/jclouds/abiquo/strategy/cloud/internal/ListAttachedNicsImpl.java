package org.jclouds.abiquo.strategy.cloud.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static org.jclouds.abiquo.domain.DomainWrapper.wrap;

import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.network.ExternalIp;
import org.jclouds.abiquo.domain.network.Ip;
import org.jclouds.abiquo.domain.network.PrivateIp;
import org.jclouds.abiquo.domain.network.PublicIp;
import org.jclouds.abiquo.domain.network.UnmanagedIp;
import org.jclouds.abiquo.rest.internal.ExtendedUtils;
import org.jclouds.abiquo.strategy.cloud.ListAttachedNics;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.rest.RestContext;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.infrastructure.network.ExternalIpDto;
import com.abiquo.server.core.infrastructure.network.PrivateIpDto;
import com.abiquo.server.core.infrastructure.network.PublicIpDto;
import com.abiquo.server.core.infrastructure.network.UnmanagedIpDto;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;

/**
 * List all NICs attached to a given virtual machine.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class ListAttachedNicsImpl implements ListAttachedNics
{
    protected final RestContext<AbiquoClient, AbiquoAsyncClient> context;

    protected final ExtendedUtils extendedUtils;

    @Inject
    public ListAttachedNicsImpl(final RestContext<AbiquoClient, AbiquoAsyncClient> context,
        final ExtendedUtils extendedUtils)
    {
        this.context = checkNotNull(context, "context");
        this.extendedUtils = checkNotNull(extendedUtils, "extendedUtils");
    }

    @Override
    public Iterable<Ip< ? , ? >> execute(final VirtualMachine parent)
    {
        Iterable<RESTLink> nicLinks = getNicLinks(parent);
        return listIps(nicLinks);
    }

    @Override
    public Iterable<Ip< ? , ? >> execute(final VirtualMachine parent,
        final Predicate<Ip< ? , ? >> selector)
    {
        return filter(execute(parent), selector);
    }

    private Iterable<Ip< ? , ? >> listIps(final Iterable<RESTLink> nicLinks)
    {
        return transform(nicLinks, new Function<RESTLink, Ip< ? , ? >>()
        {
            @Override
            public Ip< ? , ? > apply(final RESTLink input)
            {
                HttpResponse response = extendedUtils.getAbiquoHttpClient().get(input);

                if (input.getType().equals(PrivateIpDto.BASE_MEDIA_TYPE))
                {
                    ParseXMLWithJAXB<PrivateIpDto> parser =
                        new ParseXMLWithJAXB<PrivateIpDto>(extendedUtils.getXml(), TypeLiteral
                            .get(PrivateIpDto.class));

                    return wrap(context, PrivateIp.class, parser.apply(response));
                }
                else if (input.getType().equals(PublicIpDto.BASE_MEDIA_TYPE))
                {
                    ParseXMLWithJAXB<PublicIpDto> parser =
                        new ParseXMLWithJAXB<PublicIpDto>(extendedUtils.getXml(), TypeLiteral
                            .get(PublicIpDto.class));

                    return wrap(context, PublicIp.class, parser.apply(response));
                }
                else if (input.getType().equals(ExternalIpDto.BASE_MEDIA_TYPE))
                {
                    ParseXMLWithJAXB<ExternalIpDto> parser =
                        new ParseXMLWithJAXB<ExternalIpDto>(extendedUtils.getXml(), TypeLiteral
                            .get(ExternalIpDto.class));

                    return wrap(context, ExternalIp.class, parser.apply(response));
                }
                else if (input.getType().equals(UnmanagedIpDto.BASE_MEDIA_TYPE))
                {
                    ParseXMLWithJAXB<UnmanagedIpDto> parser =
                        new ParseXMLWithJAXB<UnmanagedIpDto>(extendedUtils.getXml(), TypeLiteral
                            .get(UnmanagedIpDto.class));

                    return wrap(context, UnmanagedIp.class, parser.apply(response));
                }
                else
                {
                    throw new IllegalArgumentException("Unsupported media type: " + input.getType());
                }
            }
        });
    }

    private Iterable<RESTLink> getNicLinks(final VirtualMachine vm)
    {
        return filter(vm.unwrap().getLinks(), new Predicate<RESTLink>()
        {
            @Override
            public boolean apply(final RESTLink input)
            {
                return input.getRel().startsWith("nic");
            }
        });
    }

}
