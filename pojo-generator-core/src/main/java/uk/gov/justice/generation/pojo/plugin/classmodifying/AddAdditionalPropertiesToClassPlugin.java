package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static java.util.Arrays.asList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class AddAdditionalPropertiesToClassPlugin implements ClassModifyingPlugin {

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder classBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {

        if (classDefinition.allowAdditionalProperties()) {
            classBuilder
                    .addField(additionalPropertiesMapField())
                    .addMethods(gettersAndSetters());
        }

        return classBuilder;
    }

    private FieldSpec additionalPropertiesMapField() {

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

    private List<MethodSpec> gettersAndSetters() {

        final MethodSpec getter = generateGetter();
        final MethodSpec setter = generateSetter();

        return asList(getter, setter);
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
