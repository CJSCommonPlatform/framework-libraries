package uk.gov.justice.generation.pojo.dom;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

public class CombinedDefinition extends ClassDefinition {

    public CombinedDefinition(final String fieldName, final ClassName className) {
        super(fieldName, className);
    }

    public CombinedDefinition(final String fieldName, final ClassName className, final String eventName) {
        super(fieldName, className, eventName);
    }

    @Override
    public List<Definition> getFieldDefinitions() {
        return super.getFieldDefinitions().stream()
                .flatMap(this::getStream).collect(toList());
    }

    private Stream<Definition> getStream(final Definition definition) {
        if (definition instanceof ClassDefinition) {
            return ((ClassDefinition) definition).getFieldDefinitions().stream();
        } else {
            return Stream.of(definition);
        }
    }
}
