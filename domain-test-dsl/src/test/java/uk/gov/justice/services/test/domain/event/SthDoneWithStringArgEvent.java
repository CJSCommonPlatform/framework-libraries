package uk.gov.justice.services.test.domain.event;


import uk.gov.justice.domain.annotation.Event;

import com.fasterxml.jackson.annotation.JsonCreator;

@Event("context.sth-done-with-string-arg")
public class SthDoneWithStringArgEvent {

    private final String strArg;

    @JsonCreator
    public SthDoneWithStringArgEvent(final String strArg) {
        this.strArg = strArg;
    }

    public String getStrArg() {
        return strArg;
    }
}
