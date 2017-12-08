package uk.gov.justice.services.test.domain.event;

import uk.gov.justice.domain.annotation.Event;

import java.util.UUID;

@Event("context.event-b")
public class InitialEventB {
    private UUID id;
    private String stringField;

    public InitialEventB(final UUID id, final String stringField) {
        this.id = id;
        this.stringField = stringField;
    }

    public UUID getId() {
        return id;
    }

    public String getStringField() {
        return stringField;
    }
}
