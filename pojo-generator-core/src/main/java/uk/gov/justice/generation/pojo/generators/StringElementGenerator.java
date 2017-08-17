package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.stream.Stream;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

public class StringElementGenerator implements ElementGeneratable {

    private final Definition classTypeDefinition;
    private final DefinitionToTypeNameConverter definitionToTypeNameConverter = new DefinitionToTypeNameConverter();

    StringElementGenerator(final Definition classTypeDefinition) {
        this.classTypeDefinition = classTypeDefinition;
    }

    @Override
    public FieldSpec generateField() {
        return builder(definitionToTypeNameConverter.getTypeName(classTypeDefinition), classTypeDefinition.getFieldName(), PRIVATE, FINAL).build();
    }

    @Override
    public Stream<MethodSpec> generateMethods() {
        return Stream.of(methodBuilder(toGetterMethodName())
                .addModifiers(PUBLIC)
                .returns(String.class)
                .addCode(CodeBlock.builder().addStatement("return $L.toString()", classTypeDefinition.getFieldName()).build())
                .build());
    }

    private String toGetterMethodName() {
        return "get" + classTypeDefinition.getClassName().getSimpleName();
    }
}