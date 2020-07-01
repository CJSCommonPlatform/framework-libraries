package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class WithMethodGenerator {

    private final OptionalTypeNameUtil optionalTypeNameUtil;

    public WithMethodGenerator(final OptionalTypeNameUtil optionalTypeNameUtil) {
        this.optionalTypeNameUtil = optionalTypeNameUtil;
    }

    public List<MethodSpec> generateWithMethods(final Definition fieldDefinition,
                                                final ClassName builderClassName,
                                                final ClassNameFactory classNameFactory,
                                                final PluginContext pluginContext) {

        final String fieldName = fieldDefinition.getFieldName();
        final TypeName typeName = classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext);
        final boolean isOptionalType = optionalTypeNameUtil.isOptionalType(typeName);

        final TypeName rawTypeName;
        final TypeName optionalTypeName;
        if (isOptionalType) {
            rawTypeName = ((ParameterizedTypeName) typeName).typeArguments.get(0);
            optionalTypeName = typeName;
        } else {
            rawTypeName = typeName;
            optionalTypeName = null;
        }

        final List<MethodSpec> methodSpecs = new ArrayList<>();

        final MethodSpec methodSpec = generateWithMethods(
                builderClassName,
                fieldName,
                rawTypeName);

        methodSpecs.add(methodSpec);

        if (isOptionalType) {

            final MethodSpec overloadedMethodSpec = generateOverloadedOptionalWithMethod(
                    builderClassName,
                    fieldName,
                    optionalTypeName);

            methodSpecs.add(overloadedMethodSpec);
        }

        return methodSpecs;
    }

    private MethodSpec generateOverloadedOptionalWithMethod(final ClassName builderClassName, final String fieldName, final TypeName optionalTypeName) {
        return methodBuilder("with" + capitalize(fieldName))
                .addModifiers(PUBLIC)
                .addParameter(optionalTypeName, fieldName, FINAL)
                .returns(builderClassName)
                .addCode(CodeBlock.builder()
                        .beginControlFlow("if ($L != null)", fieldName)
                        .addStatement("this.$L = $L.orElse(null)", fieldName, fieldName)
                        .endControlFlow()
                        .addStatement("return this")
                        .build())
                .build();
    }

    private MethodSpec generateWithMethods(final ClassName builderClassName, final String fieldName, final TypeName rawTypeName) {
        return methodBuilder("with" + capitalize(fieldName))
                .addModifiers(PUBLIC)
                .addParameter(rawTypeName, fieldName, FINAL)
                .returns(builderClassName)
                .addCode(CodeBlock.builder()
                        .addStatement("this.$L = $L", fieldName, fieldName)
                        .addStatement("return this")
                        .build())
                .build();
    }
}
