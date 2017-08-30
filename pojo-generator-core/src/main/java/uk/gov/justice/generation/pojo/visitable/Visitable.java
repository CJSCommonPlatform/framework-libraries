package uk.gov.justice.generation.pojo.visitable;

import uk.gov.justice.generation.pojo.visitor.Visitor;

public interface Visitable {

    void accept(final String fieldName, final Visitor visitor);
}
