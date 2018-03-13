package uk.gov.justice.domain;

import uk.gov.justice.domain.subscriptiondescriptor.Event;
import uk.gov.justice.domain.subscriptiondescriptor.Eventsource;
import uk.gov.justice.domain.subscriptiondescriptor.Subscription;

import java.util.ArrayList;
import java.util.List;

public final class SubscriptionBuilder {

    private String name;
    private final List<Event> events = new ArrayList<>();
    private Eventsource eventsource;

    private SubscriptionBuilder() {
    }

    public static SubscriptionBuilder subscription() {
        return new SubscriptionBuilder();
    }

    public SubscriptionBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public SubscriptionBuilder withEvent(final Event event) {
        this.events.add(event);
        return this;
    }

    public SubscriptionBuilder withEvents(final List<Event> events) {
        this.events.addAll(events);
        return this;
    }

    public SubscriptionBuilder withEventsource(final Eventsource eventsource) {
        this.eventsource = eventsource;
        return this;
    }

    public Subscription build() {
        return new Subscription(name, events, eventsource);
    }
}
