package uk.gov.justice.generation.pojo.dom;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDefinition extends FieldDefinition {

    private final List<Definition> fieldDefinitions = new ArrayList<>();

    private final Optional<String> eventName;

    public ClassDefinition(final String fieldName, final ClassName className) {
        super(fieldName, className);
        this.eventName = Optional.empty();
    }

    public ClassDefinition(final String fieldName, final ClassName className, final String eventName) {
        super(fieldName, className);
        this.eventName = Optional.ofNullable(eventName);
    }

    public ClassDefinition addFieldDefinition(final Definition fieldDefinition) {
        fieldDefinitions.add(fieldDefinition);
        return this;
    }

    public List<Definition> getFieldDefinitions() {

        sortDefinitionsByFieldNameFirst();

        return unmodifiableList(fieldDefinitions);

    }

    public Optional<String> getEventName() {
        return eventName;
    }

    private void sortDefinitionsByFieldNameFirst() {
        fieldDefinitions.sort(comparing(Definition::getFieldName));
    }
}
