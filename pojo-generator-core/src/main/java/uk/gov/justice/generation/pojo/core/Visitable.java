package uk.gov.justice.generation.pojo.core;

public interface Visitable {

    void accept(String fieldName, Visitor visitor);
}
