package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeSpec.anonymousClassBuilder;
import static com.squareup.javapoet.TypeSpec.enumBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;

import com.squareup.javapoet.TypeSpec;

public class EnumGenerator implements ClassGeneratable {

    private static final String VALUE_VARIABLE_NAME = "value";
    private static final String BLANK_ENUM_NAME = "BLANK";

    private final EnumDefinition enumDefinition;

    public EnumGenerator(EnumDefinition enumDefinition) {
        this.enumDefinition = enumDefinition;
    }

    @Override
    public TypeSpec generate() {
        final String className = enumDefinition.getClassName().getSimpleName();

        TypeSpec.Builder enumBuilder = enumBuilder(className).addModifiers(PUBLIC);

        enumDefinition.getEnumValues().forEach(enumValue -> {
            final String enumName = enumValue.isEmpty() ? BLANK_ENUM_NAME : enumValue.toUpperCase();

            enumBuilder.addEnumConstant(enumName, anonymousClassBuilder("$S", enumValue)
                    .build());
        });

        return enumBuilder
                .addField(String.class, VALUE_VARIABLE_NAME, PRIVATE, FINAL)
                .addMethod(constructorBuilder()
                        .addParameter(String.class, VALUE_VARIABLE_NAME)
                        .addStatement("this.$L = $L", VALUE_VARIABLE_NAME, VALUE_VARIABLE_NAME)
                        .build())
                .addMethod(methodBuilder("toString")
                        .addAnnotation(Override.class)
                        .addModifiers(PUBLIC)
                        .returns(String.class)
                        .addStatement("return $L", VALUE_VARIABLE_NAME)
                        .build())
                .build();
    }

    @Override
    public ClassName getClassName() {
        return enumDefinition.getClassName();
    }
}
