package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeSpec.anonymousClassBuilder;
import static com.squareup.javapoet.TypeSpec.enumBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

/**
 * Generates an {@link EnumDefinition} as a class in it's own *.java file
 */
public class EnumGenerator implements ClassGeneratable {

    private static final String VALUE_VARIABLE_NAME = "value";
    private static final String BLANK_ENUM_NAME = "BLANK";
    private static final String SPACE = " ";
    private static final String UNDERSCORE = "_";
    private static final String NUMBER = "NUMBER";

    private final EnumDefinition enumDefinition;
    private final ClassNameFactory classNameFactory;
    private final PluginContext pluginContext;

    private boolean isNumericEnum;

    public EnumGenerator(final EnumDefinition definition,
                         final ClassNameFactory classNameFactory,
                         final PluginContext pluginContext) {
        this.enumDefinition = definition;
        this.classNameFactory = classNameFactory;
        this.pluginContext = pluginContext;
    }

    @Override
    public TypeSpec generate() {

        final Builder enumBuilder = enumBuilder(getClassName()).addModifiers(PUBLIC);

        enumDefinition.getEnumValues().forEach(enumValue -> {

            isNumericEnum = enumValue instanceof Integer;

            final String enumName = constructEnumNameFrom(enumValue);

            enumBuilder.addEnumConstant(enumName, anonymousClassBuilder(isNumericEnum ? "$L" : "$S", enumValue)
                    .build());
        });

        return enumBuilder
                .addField(getClassType(), VALUE_VARIABLE_NAME, PRIVATE, FINAL)
                .addMethod(constructorBuilder()
                        .addParameter(getClassType(), VALUE_VARIABLE_NAME)
                        .addStatement("this.$L = $L", VALUE_VARIABLE_NAME, VALUE_VARIABLE_NAME)
                        .build())
                .addMethod(buildToStringMethod())
                .addMethod(buildValueForMethod())
                .build();
    }

    private MethodSpec buildToStringMethod() {
        return methodBuilder("toString")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(String.class)
                .addCode(addToStringReturnStatement())
                .build();
    }

    private MethodSpec buildValueForMethod() {
        final TypeName optionalEnumTypeName = ParameterizedTypeName.get(
                ClassName.get(Optional.class),
                classNameFactory.createTypeNameFrom(enumDefinition, pluginContext));

        final MethodSpec.Builder methodBuilder = methodBuilder("valueFor")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(getClassType(), VALUE_VARIABLE_NAME, FINAL)
                .returns(optionalEnumTypeName);

        enumDefinition.getEnumValues().forEach(enumValue -> {
            final String enumName = constructEnumNameFrom(enumValue);

            methodBuilder.addStatement("if($L.value.equals(value)) { return $T.of($L); }",
                    enumName,
                    Optional.class,
                    enumName);
        });

        methodBuilder.addStatement("return $T.empty()", Optional.class);

        return methodBuilder.build();
    }

    private CodeBlock addToStringReturnStatement() {
        return CodeBlock
                .builder()
                .addStatement("return $L", isNumericEnum ? generateStringFromNumber() : VALUE_VARIABLE_NAME)
                .build();
    }

    private String generateStringFromNumber() {
        return "String.valueOf(".concat(VALUE_VARIABLE_NAME).concat(")");
    }

    @Override
    public String getSimpleClassName() {
        return getClassName().simpleName();
    }

    @Override
    public String getPackageName() {
        return getClassName().packageName();
    }


    private String constructEnumNameFrom(final Object enumValue) {
        if (isNumericEnum) {
            return NUMBER.concat(UNDERSCORE).concat(enumValue.toString());
        }
        return enumValue.toString().isEmpty() ? BLANK_ENUM_NAME : enumValue.toString().toUpperCase().replace(SPACE, UNDERSCORE);
    }

    private ClassName getClassName() {
        return classNameFactory.createClassNameFrom(enumDefinition);
    }

    private Class<?> getClassType() {
        return isNumericEnum ? Integer.class : String.class;
    }
}
