package uk.gov.justice.domain;

import uk.gov.justice.domain.subscriptiondescriptor.Event;

public class EventBuilder {

    private String name;
    private String schemaUri;

    private EventBuilder() {}


    public static EventBuilder event() {
        return new EventBuilder();
    }

    public EventBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public EventBuilder withSchemaUri(final String schemaUri) {
        this.schemaUri = schemaUri;
        return this;
    }

    public Event build() {
        return new Event(name, schemaUri);
    }
}
