package uk.gov.justice.services.test.domain.event;

import uk.gov.justice.domain.annotation.Event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

@Event("something-else-happened")
public class SomethingElseHappened {

    private final UUID id;

    @JsonCreator
    public SomethingElseHappened(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
