package uk.gov.justice.services.yaml.subscriptiondescriptor;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Event {

    private final String name;
    private final String schemaUri;

    @JsonCreator
    public Event(final String name, final String schemaUri) {
        this.name = name;
        this.schemaUri = schemaUri;
    }

    public String getName() {
        return name;
    }

    public String getSchemaUri() {
        return schemaUri;
    }
}
