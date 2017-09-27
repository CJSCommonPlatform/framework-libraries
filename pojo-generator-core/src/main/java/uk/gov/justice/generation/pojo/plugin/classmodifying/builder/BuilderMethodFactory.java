package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

/**
 * Factory for creating the 'with' and 'build' methods of the POJO's static inner Builder
 */
public class BuilderMethodFactory {

    public List<MethodSpec> createTheWithMethods(
            final List<Definition> fieldDefinitions,
            final ClassNameFactory classNameFactory,
            final ClassName builderClassName,
            final PluginContext pluginContext) {

        return fieldDefinitions.stream()
                .map(fieldDefinition -> generateWithMethod(
                        fieldDefinition,
                        builderClassName,
                        classNameFactory,
                        pluginContext))
                .collect(toList());
    }

    public MethodSpec createTheBuildMethod(
            final List<Definition> fieldDefinitions,
            final ClassName pojoClassName) {
        final String params = fieldDefinitions.stream()
                .map(Definition::getFieldName)
                .collect(joining(", "));

        return MethodSpec.methodBuilder("build")
                .addModifiers(PUBLIC)
                .addStatement("return new $L(" + params + ")", pojoClassName)
                .returns(pojoClassName)
                .build();
    }

    private MethodSpec generateWithMethod(
            final Definition fieldDefinition,
            final ClassName builderClassName,
            final ClassNameFactory classNameFactory,
            final PluginContext pluginContext) {

        final String fieldName = fieldDefinition.getFieldName();
        final TypeName typeName = classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext);

        return methodBuilder("with" + capitalize(fieldName))
                .addModifiers(PUBLIC)
                .addParameter(typeName, fieldName, FINAL)
                .returns(builderClassName)
                .addCode(CodeBlock.builder()
                        .addStatement("this.$L = $L", fieldName, fieldName)
                        .addStatement("return this")
                        .build())
                .build();
    }
}
