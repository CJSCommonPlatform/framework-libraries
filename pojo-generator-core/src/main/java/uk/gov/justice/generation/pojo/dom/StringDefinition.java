package uk.gov.justice.generation.pojo.dom;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

public class StringDefinition extends FieldDefinition {

    private final String description;

    public StringDefinition(final String fieldName, final String description) {
        super(STRING, fieldName);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
