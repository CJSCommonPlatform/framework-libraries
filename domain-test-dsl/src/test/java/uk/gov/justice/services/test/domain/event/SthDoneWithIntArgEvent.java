package uk.gov.justice.services.test.domain.event;


import uk.gov.justice.domain.annotation.Event;

import com.fasterxml.jackson.annotation.JsonCreator;

@Event("context.sth-done-with-int-arg")
public class SthDoneWithIntArgEvent {

    private final Integer intArg;

    @JsonCreator
    public SthDoneWithIntArgEvent(final Integer intArg) {
        this.intArg = intArg;
    }

    public Integer getIntArg() {
        return intArg;
    }
}
