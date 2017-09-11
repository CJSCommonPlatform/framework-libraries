package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.ClassName.get;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import java.math.BigDecimal;
import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class TypeNameProvider {

    private static final int FIRST_CHILD = 0;

    private final GenerationContext generationContext;

    public TypeNameProvider(final GenerationContext generationContext) {
        this.generationContext = generationContext;
    }

    public TypeName typeNameForArray(final Definition definition, final ClassNameFactory classNameFactory) {

        final ClassDefinition classDefinition = (ClassDefinition) definition;

        if (classDefinition.getFieldDefinitions().isEmpty()) {
            throw new GenerationException(format("No definition present for array types. For field: %s", classDefinition.getFieldName()));
        }

        final Definition childDefinition = classDefinition.getFieldDefinitions().get(FIRST_CHILD);
        final TypeName typeName = classNameFactory.createTypeNameFrom(childDefinition);

        return ParameterizedTypeName.get(get(List.class), typeName);
    }

    public TypeName typeNameForReference(final Definition definition, final ClassNameFactory classNameFactory) {
        final ReferenceDefinition referenceDefinition = (ReferenceDefinition) definition;

        if (referenceDefinition.getFieldDefinitions().isEmpty()) {
            throw new GenerationException(format("No definition present for reference type. For field: %s", referenceDefinition.getFieldName()));
        }

        final Definition childDefinition = referenceDefinition.getFieldDefinitions().get(FIRST_CHILD);
        return classNameFactory.createTypeNameFrom(childDefinition);
    }

    public ClassName typeNameForString() {
        return get(String.class);
    }

    public ClassName typeNameForClass(final Definition definition) {
        return get(generationContext.getPackageName(), capitalize(definition.getFieldName()));
    }

    public ClassName typeNameForNumber() {
        return get(BigDecimal.class);
    }

    public ClassName typeNameForInteger() {
        return get(Integer.class);
    }

    public ClassName typeNameForBoolean() {
        return get(Boolean.class);
    }
}
