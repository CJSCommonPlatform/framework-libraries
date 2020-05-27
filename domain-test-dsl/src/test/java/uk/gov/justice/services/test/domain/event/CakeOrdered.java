package uk.gov.justice.services.test.domain.event;

import uk.gov.justice.domain.annotation.Event;

import java.util.UUID;

@Event("cake-ordered")
public class CakeOrdered {

    private final UUID id;

    public CakeOrdered(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
