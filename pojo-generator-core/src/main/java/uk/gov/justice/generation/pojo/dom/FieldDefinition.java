package uk.gov.justice.generation.pojo.dom;

/**
 * Base class of all our Definitions. Implicitly, apart from the root object of a json
 * schema, all definitions are fields, as a property defined in a json document will
 * always have a name and a value. A field, therefore, can be a string, class, number, integer
 * enum, boolean or array.
 */
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
