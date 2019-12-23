package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin.ADDITIONAL_PROPERTIES_FIELD_NAME;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * Factory for creating the 'with' and 'build' methods of the POJO's static inner Builder
 */
public class BuilderMethodFactory {

    private final ClassNameFactory classNameFactory;
    private final OptionalTypeNameUtil optionalTypeNameUtil;

    public BuilderMethodFactory(final ClassNameFactory classNameFactory,
                                final OptionalTypeNameUtil optionalTypeNameUtil) {
        this.classNameFactory = classNameFactory;
        this.optionalTypeNameUtil = optionalTypeNameUtil;
    }

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

    public List<MethodSpec> createTheWithMethodsWithAdditionalProperties(
            final List<Definition> fieldDefinitions,
            final ClassNameFactory classNameFactory,
            final ClassName builderClassName,
            final PluginContext pluginContext) {

        final List<MethodSpec> methods = fieldDefinitions.stream()
                .map(fieldDefinition -> generateWithMethod(
                        fieldDefinition,
                        builderClassName,
                        classNameFactory,
                        pluginContext))
                .collect(toList());

        methods.add(generateWithMethodForAdditionalProperties(builderClassName));

        return methods;
    }

    public MethodSpec createTheBuildMethod(
            final List<Definition> fieldDefinitions,
            final ClassName pojoClassName,
            final PluginContext pluginContext) {

        final String constructorArguments = fieldDefinitions.stream()
                .map(definition -> createConstructorArgumentFor(definition, pluginContext))
                .collect(joining(", "));

        return MethodSpec.methodBuilder("build")
                .addModifiers(PUBLIC)
                .addStatement("return new $L(" + constructorArguments + ")", pojoClassName)
                .returns(pojoClassName)
                .build();
    }

    public MethodSpec createTheBuildMethodWithAdditionalProperties(
            final List<Definition> fieldDefinitions,
            final ClassName pojoClassName) {

        final String params = fieldDefinitions.stream()
                .map(Definition::getFieldName)
                .collect(joining(", "));

        final String statementFormat = createStatementFormatWith(params);

        return MethodSpec.methodBuilder("build")
                .addModifiers(PUBLIC)
                .addStatement(statementFormat, pojoClassName, ADDITIONAL_PROPERTIES_FIELD_NAME)
                .returns(pojoClassName)
                .build();
    }

    private String createConstructorArgumentFor(final Definition definition, final PluginContext pluginContext) {
        final TypeName typeName = classNameFactory.createTypeNameFrom(definition, pluginContext);

        if (optionalTypeNameUtil.isOptionalType(typeName)) {
            return "Optional.ofNullable(" + definition.getFieldName() + ")";
        }

        return definition.getFieldName();
    }

    private String createStatementFormatWith(final String params) {
        if (params.isEmpty()) {
            return "return new $L($N)";
        }

        return "return new $L(" + params + ", $N)";
    }

    private MethodSpec generateWithMethod(final Definition fieldDefinition,
                                          final ClassName builderClassName,
                                          final ClassNameFactory classNameFactory,
                                          final PluginContext pluginContext) {

        final String fieldName = fieldDefinition.getFieldName();
        final TypeName typeName = getParameterTypeName(fieldDefinition, classNameFactory, pluginContext);

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

    private TypeName getParameterTypeName(final Definition fieldDefinition,
                                          final ClassNameFactory classNameFactory,
                                          final PluginContext pluginContext) {

        final TypeName typeName = classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext);

        if (optionalTypeNameUtil.isOptionalType(typeName)) {
            return optionalTypeNameUtil.getOptionalTypeFrom((ParameterizedTypeName) typeName);
        }

        return typeName;
    }

    private MethodSpec generateWithMethodForAdditionalProperties(final ClassName builderClassName) {

        return methodBuilder("withAdditionalProperty")
                .addModifiers(PUBLIC)
                .addParameter(ClassName.get(String.class), "name", FINAL)
                .addParameter(ClassName.get(Object.class), "value", FINAL)
                .returns(builderClassName)
                .addCode(CodeBlock.builder()
                        .addStatement("$N.put($N, $N)", ADDITIONAL_PROPERTIES_FIELD_NAME, "name", "value")
                        .addStatement("return this")
                        .build())
                .build();
    }
}
