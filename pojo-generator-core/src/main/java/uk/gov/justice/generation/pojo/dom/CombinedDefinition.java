package uk.gov.justice.generation.pojo.dom;

import static java.util.stream.Collectors.toList;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.COMBINED;

import java.util.List;
import java.util.stream.Stream;


/**
 * This defines the combined schemas of {@code oneOf}, {@code anyOf} and {@code allOf}.
 *
 * Please note: as the resulting generated classes are Plain Old Java Objects, they contain
 * no logic - just getters and setters, all of the combined schemas are generated to include
 * all of the possible properties defined in the json schema. It is then up to the developer
 * to ensure that prohibited combinations of properties is maintained
 */
public class CombinedDefinition extends ClassDefinition {

    public CombinedDefinition(final String fieldName) {
        super(COMBINED, fieldName);
    }

    /**
     * Gets the list of Fields used in the generated class. As this is a combined schema,
     * the list of fields is flattened from each of the sub schemas before the list is
     * returned.
     *
     * @return The list of fields defined for generation into the class. The list
     * will be in alphabetical order according to the field names
     */
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
