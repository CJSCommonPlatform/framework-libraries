package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.OptionalTypeNameUtil;

import java.util.Optional;
import java.util.stream.Stream;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

/**
 * Generator for creating a field in the POJO and any corresponding 'getter' method
 */
public class FieldGenerator implements ElementGeneratable {

    private final FieldDefinition fieldDefinition;
    private final ClassNameFactory classNameFactory;
    private final PluginContext pluginContext;
    private final OptionalTypeNameUtil optionalTypeNameUtil;

    FieldGenerator(
            final FieldDefinition fieldDefinition,
            final ClassNameFactory classNameFactory,
            final PluginContext pluginContext,
            final OptionalTypeNameUtil optionalTypeNameUtil) {
        this.fieldDefinition = fieldDefinition;
        this.classNameFactory = classNameFactory;
        this.pluginContext = pluginContext;
        this.optionalTypeNameUtil = optionalTypeNameUtil;
    }

    @Override
    public FieldSpec generateField() {
        final TypeName typeName = classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext);
        return builder(typeName, fieldDefinition.getFieldName(), PRIVATE, FINAL).build();
    }

    @Override
    public Stream<MethodSpec> generateMethods() {
        return Stream.of(getterMethod());
    }

    private MethodSpec getterMethod() {
        final TypeName returnType = classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext);
        final String fieldName = fieldDefinition.getFieldName();

        final MethodSpec.Builder methodSpecBuilder = methodBuilder("get" + capitalize(fieldName))
                .addModifiers(PUBLIC)
                .returns(returnType);

        if (optionalTypeNameUtil.isOptionalType(returnType)) {
            methodSpecBuilder.addCode(CodeBlock.builder()
                    .beginControlFlow("if ($L != null)", fieldName)
                    .addStatement("return $L", fieldName)
                    .endControlFlow()
                    .addStatement("return $T.empty()", ClassName.get(Optional.class))
                    .build());

        } else {
            methodSpecBuilder.addCode(CodeBlock.builder()
                    .addStatement("return $L", fieldName)
                    .build());
        }


        return methodSpecBuilder
                .build();
    }
}
