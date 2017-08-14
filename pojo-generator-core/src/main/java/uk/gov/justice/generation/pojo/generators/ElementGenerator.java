package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import java.util.stream.Stream;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

public class ElementGenerator implements ElementGeneratable {

    private final ClassDefinition classDefinition;
    private final DefinitionToTypeNameConverter definitionToTypeNameConverter = new DefinitionToTypeNameConverter();

    ElementGenerator(final ClassDefinition classDefinition) {
        this.classDefinition = classDefinition;
    }

    @Override
    public FieldSpec generateField() {
        return builder(definitionToTypeNameConverter.getTypeName(classDefinition), classDefinition.getFieldName(), PRIVATE, FINAL).build();
    }

    @Override
    public Stream<MethodSpec> generateMethods() {
        return Stream.of(methodBuilder(toGetterMethodName())
                .addModifiers(PUBLIC)
                .returns(definitionToTypeNameConverter.getTypeName(classDefinition))
                .addCode(CodeBlock.builder().addStatement("return $L", classDefinition.getFieldName()).build())
                .build());
    }

    private String toGetterMethodName() {
        return "get" + classDefinition.getClassName().getSimpleName();
    }
}
