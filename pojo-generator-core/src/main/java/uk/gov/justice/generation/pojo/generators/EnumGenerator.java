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

    private final EnumDefinition enumDefinition;
    private final ClassNameFactory classNameFactory;
    private final PluginContext pluginContext;

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
            final String enumName = constructEnumNameFrom(enumValue);

            enumBuilder.addEnumConstant(enumName, anonymousClassBuilder("$S", enumValue)
                    .build());
        });

        return enumBuilder
                .addField(String.class, VALUE_VARIABLE_NAME, PRIVATE, FINAL)
                .addMethod(constructorBuilder()
                        .addParameter(String.class, VALUE_VARIABLE_NAME)
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
                .addStatement("return $L", VALUE_VARIABLE_NAME)
                .build();
    }

    private MethodSpec buildValueForMethod() {
        final TypeName optionalEnumTypeName = ParameterizedTypeName.get(
                ClassName.get(Optional.class),
                classNameFactory.createTypeNameFrom(enumDefinition, pluginContext));

        final MethodSpec.Builder methodBuilder = methodBuilder("valueFor")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(String.class, VALUE_VARIABLE_NAME, FINAL)
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

    @Override
    public String getSimpleClassName() {
        return getClassName().simpleName();
    }

    @Override
    public String getPackageName() {
        return getClassName().packageName();
    }

    private String constructEnumNameFrom(final String enumValue) {
        return enumValue.isEmpty() ? BLANK_ENUM_NAME : enumValue.toUpperCase().replace(SPACE, UNDERSCORE);
    }

    private ClassName getClassName() {
        return classNameFactory.createClassNameFrom(enumDefinition);
    }
}
