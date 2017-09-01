package uk.gov.justice.generation.pojo.dom;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.ENUM;

import java.util.List;

public class EnumDefinition extends FieldDefinition {

    private final List<String> enumValues;

    public EnumDefinition(final String fieldName, final List<String> enumValues) {
        super(ENUM, fieldName);
        this.enumValues = enumValues;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }
}
