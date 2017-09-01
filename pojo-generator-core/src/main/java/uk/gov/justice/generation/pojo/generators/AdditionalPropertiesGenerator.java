package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class AdditionalPropertiesGenerator implements ElementGeneratable {

    @Override
    public FieldSpec generateField() {

        final ParameterizedTypeName map = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                TypeName.get(String.class),
                TypeName.get(Object.class));

        final ClassName hashMap = ClassName.get(HashMap.class);
        return builder(map, "additionalProperties")
                .addModifiers(PRIVATE, FINAL)
                .initializer("new $T<>()", hashMap)
                .build();
    }

    @Override
    public Stream<MethodSpec> generateMethods() {

        final MethodSpec getter = generateGetter();
        final MethodSpec setter = generateSetter();

        return Stream.of(getter, setter);
    }

    private MethodSpec generateGetter() {
        final ParameterizedTypeName map = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                TypeName.get(String.class),
                TypeName.get(Object.class));

        return methodBuilder("getAdditionalProperties")
                .addModifiers(PUBLIC)
                .addAnnotation(JsonAnyGetter.class)
                .returns(map)
                .addCode(CodeBlock.builder().addStatement("return $L", "additionalProperties").build())
                .build();
    }

    private MethodSpec generateSetter() {
        return methodBuilder("setAdditionalProperty")
                .addModifiers(PUBLIC)
                .addAnnotation(JsonAnySetter.class)
                .addParameter(String.class, "name", FINAL)
                .addParameter(Object.class, "value", FINAL)
                .addCode(CodeBlock.builder().addStatement("additionalProperties.put($L, $L)", "name", "value").build())
                .build();
    }
}
