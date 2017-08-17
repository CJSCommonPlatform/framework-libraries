package uk.gov.justice.generation.pojo.dom;

import java.util.List;

public class EnumDefinition extends FieldDefinition {

    private final List<String> enumValues;

    public EnumDefinition(final String fieldName, final ClassName className, final List<String> enumValues) {
        super(fieldName, className);
        this.enumValues = enumValues;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }
}
