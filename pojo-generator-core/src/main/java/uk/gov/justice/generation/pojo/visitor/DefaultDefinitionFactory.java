package uk.gov.justice.generation.pojo.visitor;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.BOOLEAN;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.NUMBER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ROOT;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.CombinedDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.DefinitionType;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import java.io.StringReader;
import java.util.List;
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

public class DefaultDefinitionFactory implements DefinitionFactory {

    private static final String EXCEPTION_FORMAT_MESSAGE = "Schema of type: %s is not supported.";

    private final ReferenceValueParser referenceValueParser = new ReferenceValueParser();

    @Override
    public Definition constructRootClassDefinition(final String fieldName) {
        return new ClassDefinition(ROOT, fieldName);
    }

    @Override
    public Definition constructDefinitionFor(final String fieldName, final Schema schema) {
        if (schema instanceof ReferenceSchema) {
            final ReferenceSchema referenceSchema = (ReferenceSchema) schema;
            return new ReferenceDefinition(fieldName, parseReferenceValueFrom(fieldName, referenceSchema));
        }

        if (schema instanceof CombinedSchema) {
            return new CombinedDefinition(fieldName);
        }

        if (schema instanceof ArraySchema) {
            return new ClassDefinition(ARRAY, fieldName);
        }

        if (schema instanceof EnumSchema) {
            final Set<Object> possibleValues = ((EnumSchema) schema).getPossibleValues();
            final List<String> enumValues = possibleValues.stream().map(Object::toString).collect(toList());

            return new EnumDefinition(fieldName, enumValues);
        }

        if (schema instanceof ObjectSchema) {
            return new ClassDefinition(CLASS, fieldName);
        }

        if (schema instanceof StringSchema) {
            return new FieldDefinition(STRING, fieldName);
        }

        if (schema instanceof BooleanSchema) {
            return new FieldDefinition(BOOLEAN, fieldName);
        }

        if (schema instanceof NumberSchema) {
            final DefinitionType type = ((NumberSchema) schema).requiresInteger() ? INTEGER : NUMBER;
            return new FieldDefinition(type, fieldName);
        }

        if (schema instanceof EmptySchema) {
            return new ClassDefinition(CLASS, fieldName);
        }

        throw new UnsupportedSchemaException(format(EXCEPTION_FORMAT_MESSAGE, schema.getClass().getSimpleName()));
    }

    private String parseReferenceValueFrom(final String fieldName, final ReferenceSchema referenceSchema) {
        final StringReader stringReader = new StringReader(referenceSchema.toString());
        return referenceValueParser.parseFrom(stringReader, fieldName);
    }
}
