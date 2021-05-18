package uk.gov.justice.services.test.domain.event;


import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.services.test.domain.arg.ComplexArgument;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

@Event("context.sth-done-with-complex-args-list")
public class SthDoneWithComplexArgumentListEvent {
    private final List<ComplexArgument> complexArgumentList;

    @JsonCreator
    public SthDoneWithComplexArgumentListEvent(final List<ComplexArgument> complexArgumentList) {
        this.complexArgumentList = complexArgumentList;
    }

    public List<ComplexArgument> getComplexArgsList() {
        return complexArgumentList;
    }
}
