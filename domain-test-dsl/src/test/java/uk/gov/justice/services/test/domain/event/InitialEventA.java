package uk.gov.justice.services.test.domain.event;

import uk.gov.justice.domain.annotation.Event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

@Event("context.event-a")
public class InitialEventA {
    private UUID id;
    private String stringField;

    @JsonCreator
    public InitialEventA(final UUID id, final String stringField) {
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
