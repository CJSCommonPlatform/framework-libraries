package uk.gov.justice.generation.pojo.dom;

import static java.util.stream.Collectors.toList;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.COMBINED;

import java.util.List;
import java.util.stream.Stream;

public class CombinedDefinition extends ClassDefinition {

    public CombinedDefinition(final String fieldName) {
        super(COMBINED, fieldName);
    }

    @Override
    public List<Definition> getFieldDefinitions() {
        return super.getFieldDefinitions().stream()
                .flatMap(this::asStreamOfDefinitions).collect(toList());
    }

    private Stream<Definition> asStreamOfDefinitions(final Definition definition) {
        if (definition instanceof ClassDefinition) {
            return ((ClassDefinition) definition).getFieldDefinitions().stream();
        } else {
            return Stream.of(definition);
        }
    }
}
