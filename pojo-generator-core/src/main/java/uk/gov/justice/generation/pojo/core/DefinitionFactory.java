package uk.gov.justice.generation.pojo.core;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.CombinedDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.math.BigDecimal;
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

public class DefinitionFactory {

    private static final String EXCEPTION_FORMAT_MESSAGE = "Schema of type: %s is not supported.";
    private final ClassNameProvider classNameProvider;
    private final Optional<String> eventName;

    public DefinitionFactory(final ClassNameProvider classNameProvider) {
        this.classNameProvider = classNameProvider;
        this.eventName = Optional.empty();
    }

    public DefinitionFactory(final ClassNameProvider classNameProvider, final String eventName) {
        this.eventName = Optional.ofNullable(eventName);
        this.classNameProvider = classNameProvider;
    }

    Definition constructDefinitionWithEventFor(final String fieldName, final String packageName, final Schema schema) {
        final ClassName className = new ClassName(packageName, capitalize(fieldName));

        if (schema instanceof CombinedSchema) {
            return eventName
                    .map(eventNameValue -> new CombinedDefinition(fieldName, className, eventNameValue))
                    .orElse(new CombinedDefinition(fieldName, className));
        } else if (schema instanceof ObjectSchema) {
            return eventName
                    .map(eventNameValue -> new ClassDefinition(fieldName, className, eventNameValue))
                    .orElse(new ClassDefinition(fieldName, className));
        }

        throw new UnsupportedSchemaException(format(EXCEPTION_FORMAT_MESSAGE, schema.getClass().getSimpleName()));
    }

    Definition constructDefinitionFor(final String fieldName, final String packageName, final Schema schema) {
        if (schema instanceof CombinedSchema) {
            return new CombinedDefinition(fieldName, new ClassName(packageName, capitalize(fieldName)));
        } else if (schema instanceof ArraySchema) {
            return new FieldDefinition(fieldName,
                    new ClassName(List.class),
                    new ClassName(packageName, capitalize(fieldName)));

        } else if (schema instanceof EnumSchema) {
            final Set<Object> possibleValues = ((EnumSchema) schema).getPossibleValues();
            final List<String> enumValues = possibleValues.stream().map(Object::toString).collect(toList());

            return new EnumDefinition(fieldName,
                    new ClassName(packageName, capitalize(fieldName)),
                    enumValues);
        } else if (schema instanceof ObjectSchema) {
            return new ClassDefinition(fieldName, new ClassName(packageName, capitalize(fieldName)));
        }

        throw new UnsupportedSchemaException(format(EXCEPTION_FORMAT_MESSAGE, schema.getClass().getSimpleName()));
    }

    Definition constructFieldDefinition(final String fieldName, final Schema schema) {
        if (schema instanceof StringSchema) {
            return new FieldDefinition(fieldName, classNameProvider.classNameFor(schema.getDescription()));
        } else if (schema instanceof BooleanSchema) {
            return new FieldDefinition(fieldName, new ClassName(Boolean.class));
        } else if (schema instanceof NumberSchema) {
            final ClassName numberClassName = ((NumberSchema) schema).requiresInteger() ? new ClassName(Integer.class) : new ClassName(BigDecimal.class);
            return new FieldDefinition(fieldName, numberClassName);
        }

        throw new UnsupportedSchemaException(format(EXCEPTION_FORMAT_MESSAGE, schema.getClass().getSimpleName()));
    }
}
