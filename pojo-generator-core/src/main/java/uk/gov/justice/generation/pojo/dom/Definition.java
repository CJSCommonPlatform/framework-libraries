package uk.gov.justice.generation.pojo.dom;

public interface Definition {

    String getFieldName();
    DefinitionType type();
    boolean isRequired();
    void setRequired(final boolean required);
}
