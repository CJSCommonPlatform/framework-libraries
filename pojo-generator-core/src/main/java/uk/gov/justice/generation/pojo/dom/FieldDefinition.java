package uk.gov.justice.generation.pojo.dom;

public class FieldDefinition implements Definition {

    private final String fieldName;
    private final DefinitionType type;
    private boolean required = true;

    public FieldDefinition(final DefinitionType type, final String fieldName) {
        this.type = type;
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public DefinitionType type() {
        return type;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(final boolean required) {
        this.required = required;
    }
}
