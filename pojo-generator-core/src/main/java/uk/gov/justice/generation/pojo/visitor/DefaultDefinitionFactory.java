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
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

/**
 * The default implementation of the {@link DefinitionFactory}.  Converts the given field name and
 * {@link Schema} to a {@link Definition}.
 */
public class DefaultDefinitionFactory implements DefinitionFactory {

    private static final String EXCEPTION_FORMAT_MESSAGE = "Schema of type: %s is not supported.";

    private final ReferenceValueParser referenceValueParser;
    private final StringFormatValueParser stringFormatValueParser;

    public DefaultDefinitionFactory(final ReferenceValueParser referenceValueParser,
                                    final StringFormatValueParser stringFormatValueParser) {
        this.referenceValueParser = referenceValueParser;
        this.stringFormatValueParser = stringFormatValueParser;
    }

    @Override
    public Definition constructRootDefinitionFor(final String fieldName, final Schema schema) {

        final String id = schema.getId();

        final ClassDefinition definition;
        if (schema instanceof CombinedSchema) {
            definition = new CombinedDefinition(fieldName, id);
        } else {
            definition = new ClassDefinition(CLASS, fieldName, id);
        }

        definition.setRoot(true);

        return definition;
    }

    @Override
    public Definition constructDefinitionFor(final String fieldName, final Schema schema) {
        if (schema instanceof ReferenceSchema) {
            final ReferenceSchema referenceSchema = (ReferenceSchema) schema;
            return new ReferenceDefinition(fieldName, parseReferenceValueFrom(fieldName, referenceSchema));
        }

        if (schema instanceof CombinedSchema) {
            return new CombinedDefinition(fieldName, schema.getId());
        }

        if (schema instanceof ObjectSchema) {
            return new ClassDefinition(CLASS, fieldName, schema.getId());
        }

        if (schema instanceof ArraySchema) {
            return new ClassDefinition(ARRAY, fieldName, schema.getId());
        }

        if (schema instanceof EnumSchema) {
            final Set<Object> possibleValues = ((EnumSchema) schema).getPossibleValues();
            final List<String> enumValues = possibleValues.stream().map(Object::toString).collect(toList());

            return new EnumDefinition(fieldName, enumValues, schema.getId());
        }

        if (schema instanceof StringSchema) {
            final StringSchema stringSchema = (StringSchema) schema;
            final Optional<String> formatValue = parseFormatValueFrom(fieldName, stringSchema);

            return formatValue
                    .map(value -> new StringDefinition(fieldName, value))
                    .orElse(new StringDefinition(fieldName));
        }

        if (schema instanceof BooleanSchema) {
            return new FieldDefinition(BOOLEAN, fieldName);
        }

        if (schema instanceof NumberSchema) {
            final DefinitionType type = ((NumberSchema) schema).requiresInteger() ? INTEGER : NUMBER;
            return new FieldDefinition(type, fieldName);
        }

        if (schema instanceof EmptySchema) {
            return new ClassDefinition(CLASS, fieldName, schema.getId());
        }

        throw new UnsupportedSchemaException(format(EXCEPTION_FORMAT_MESSAGE, schema.getClass().getSimpleName()));
    }

    private ReferenceValue parseReferenceValueFrom(final String fieldName, final ReferenceSchema referenceSchema) {
        final StringReader stringReader = new StringReader(referenceSchema.toString());
        return referenceValueParser.parseFrom(stringReader, fieldName);
    }

    private Optional<String> parseFormatValueFrom(final String fieldName, final StringSchema stringSchema) {
        final StringReader stringReader = new StringReader(stringSchema.toString());
        return stringFormatValueParser.parseFrom(stringReader, fieldName);
    }
}
