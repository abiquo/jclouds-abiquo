package org.jclouds.abiquo.strategy.admin.internal;

import static com.google.common.collect.Iterables.filter;
import static org.jclouds.abiquo.domain.DomainWrapper.wrap;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.enterprise.Event;
import org.jclouds.abiquo.strategy.admin.ListEvents;
import org.jclouds.rest.RestContext;

import com.abiquo.server.core.event.EventsDto;
import com.google.common.base.Predicate;
import com.google.inject.Inject;

public class ListEventsImpl implements ListEvents
{
    // This strategy does not have still an Executor instance because the current methods call
    // single client methods

    protected final RestContext<AbiquoClient, AbiquoAsyncClient> context;

    @Inject
    ListEventsImpl(final RestContext<AbiquoClient, AbiquoAsyncClient> context)
    {
        this.context = context;
    }

    @Override
    public Iterable<Event> execute()
    {
        EventsDto result = context.getApi().getAdminClient().listEvents();
        return wrap(context, Event.class, result.getCollection());
    }

    @Override
    public Iterable<Event> execute(final Predicate<Event> selector)
    {
        return filter(execute(), selector);
    }

}
