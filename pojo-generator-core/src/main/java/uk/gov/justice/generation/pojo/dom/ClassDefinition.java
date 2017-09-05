package uk.gov.justice.generation.pojo.dom;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;

public class ClassDefinition extends FieldDefinition {

    private final List<Definition> fieldDefinitions = new ArrayList<>();

    private boolean allowAdditionalProperties = false;

    public ClassDefinition(final DefinitionType type, final String fieldName) {
        super(type, fieldName);
    }

    public ClassDefinition addFieldDefinition(final Definition fieldDefinition) {
        fieldDefinitions.add(fieldDefinition);
        return this;
    }

    public List<Definition> getFieldDefinitions() {

        sortDefinitionsByFieldNameFirst();

        return unmodifiableList(fieldDefinitions);

    }

    public boolean allowAdditionalProperties() {
        return allowAdditionalProperties;
    }

    public void setAllowAdditionalProperties(final boolean allowAdditionalProperties) {
        this.allowAdditionalProperties = allowAdditionalProperties;
    }

    private void sortDefinitionsByFieldNameFirst() {
        fieldDefinitions.sort(comparing(Definition::getFieldName));
    }
}
