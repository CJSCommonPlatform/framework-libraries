package uk.gov.justice.generation.pojo.dom;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class ClassDefinition extends FieldDefinition {

    private final List<Definition> fieldDefinitions = new ArrayList<>();

    public ClassDefinition(final String fieldName, final ClassName className) {
        super(fieldName, className);
    }

    public ClassDefinition addFieldDefinition(final Definition fieldDefinition) {
        fieldDefinitions.add(fieldDefinition);
        return this;
    }

    public List<Definition> getFieldDefinitions() {
        return unmodifiableList(fieldDefinitions);
    }
}
