package uk.gov.justice.domain;

import uk.gov.justice.domain.subscriptiondescriptor.Eventsource;
import uk.gov.justice.domain.subscriptiondescriptor.Location;

public final class EventsourceBuilder {

    private String name;
    private Location location;

    private EventsourceBuilder() {
    }

    public static EventsourceBuilder eventsource() {
        return new EventsourceBuilder();
    }

    public EventsourceBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public EventsourceBuilder withLocation(final Location location) {
        this.location = location;
        return this;
    }

    public Eventsource build() {
        return new Eventsource(name, location);
    }
}
