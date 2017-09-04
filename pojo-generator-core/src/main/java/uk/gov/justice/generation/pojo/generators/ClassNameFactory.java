package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.ClassName.get;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class ClassNameFactory {

    private static final int FIRST_CHILD = 0;
    private final String packageName;

    public ClassNameFactory(final String packageName) {
        this.packageName = packageName;
    }

    public TypeName createClassNameFrom(final Definition definition) {

        switch (definition.type()) {
            case ARRAY:
                return optionalTypeIfNotRequired(definition, arrayTypeName((ClassDefinition) definition));

            case BOOLEAN:
                return optionalTypeIfNotRequired(definition, get(Boolean.class));

            case INTEGER:
                return optionalTypeIfNotRequired(definition, get(Integer.class));

            case NUMBER:
                return optionalTypeIfNotRequired(definition, get(BigDecimal.class));

            case STRING:
                return optionalTypeIfNotRequired(definition, classNameForStringType(((StringDefinition) definition).getDescription(), String.class));

            default:
                return optionalTypeIfNotRequired(definition, get(packageName, capitalize(definition.getFieldName())));
        }
    }

    private TypeName arrayTypeName(final ClassDefinition definition) {

        if (definition.getFieldDefinitions().isEmpty()) {
            throw new GenerationException(format("No definition present for array types. For field: %s", definition.getFieldName()));
        }

        final Definition childDefintion = definition.getFieldDefinitions().get(FIRST_CHILD);
        final TypeName typeName = createClassNameFrom(childDefintion);

        return ParameterizedTypeName.get(get(List.class), typeName);
    }

    private TypeName optionalTypeIfNotRequired(final Definition definition, final TypeName typeName) {

        if (!definition.isRequired()) {
            return ParameterizedTypeName.get(get(Optional.class), typeName);
        }
        return typeName;
    }

    private ClassName classNameForStringType(final String description, final Class<?> defaultClass) {

        if (UUID.class.getSimpleName().equals(description)) {
            return get(UUID.class);
        }

        if (ZonedDateTime.class.getSimpleName().equals(description)) {
            return get(ZonedDateTime.class);
        }

        return get(defaultClass);
   }
}
