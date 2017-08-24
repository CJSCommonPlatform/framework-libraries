package uk.gov.justice.generation.pojo.validation;

public interface ValidatingVisitable {

    void accept(final ValidatingVisitor visitor);
}
