package uk.gov.justice.generation.pojo.visitor;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.BOOLEAN;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.NUMBER;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.CombinedDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.DefinitionType;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class DefaultDefinitionFactory implements DefinitionFactory {

    private static final String EXCEPTION_FORMAT_MESSAGE = "Schema of type: %s is not supported.";
    private final Optional<String> eventName;

    public DefaultDefinitionFactory() {
        this.eventName = Optional.empty();
    }

    public DefaultDefinitionFactory(final String eventName) {
        this.eventName = Optional.ofNullable(eventName);
    }

    @Override
    public Definition constructDefinitionWithEventFor(final String fieldName, final Schema schema) {
        if (schema instanceof CombinedSchema) {

            return eventName
                    .map(eventNameValue -> new CombinedDefinition(fieldName, eventNameValue))
                    .orElse(new CombinedDefinition(fieldName));

        } else if (schema instanceof ObjectSchema) {

            return eventName
                    .map(eventNameValue -> new ClassDefinition(CLASS, fieldName, eventNameValue))
                    .orElse(new ClassDefinition(CLASS, fieldName));

        }

        throw new UnsupportedSchemaException(format(EXCEPTION_FORMAT_MESSAGE, schema.getClass().getSimpleName()));
    }

    @Override
    public Definition constructDefinitionFor(final String fieldName, final Schema schema) {
        if (schema instanceof CombinedSchema) {

            return new CombinedDefinition(fieldName);

        } else if (schema instanceof ArraySchema) {

            return new ClassDefinition(ARRAY, fieldName);

        } else if (schema instanceof EnumSchema) {

            final Set<Object> possibleValues = ((EnumSchema) schema).getPossibleValues();
            final List<String> enumValues = possibleValues.stream().map(Object::toString).collect(toList());

            return new EnumDefinition(fieldName, enumValues);

        } else if (schema instanceof ObjectSchema) {

            return new ClassDefinition(CLASS, fieldName);

        }

        throw new UnsupportedSchemaException(format(EXCEPTION_FORMAT_MESSAGE, schema.getClass().getSimpleName()));
    }

    @Override
    public Definition constructFieldDefinition(final String fieldName, final Schema schema) {
        if (schema instanceof StringSchema) {

            return new StringDefinition(fieldName, schema.getDescription());

        } else if (schema instanceof BooleanSchema) {

            return new FieldDefinition(BOOLEAN, fieldName);

        } else if (schema instanceof NumberSchema) {

            final DefinitionType type = ((NumberSchema) schema).requiresInteger() ? INTEGER : NUMBER;
            return new FieldDefinition(type, fieldName);

        }

        throw new UnsupportedSchemaException(format(EXCEPTION_FORMAT_MESSAGE, schema.getClass().getSimpleName()));
    }
}
