package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.util.stream.Stream;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

public class FieldGenerator implements ElementGeneratable {

    private final FieldDefinition fieldDefinition;

    private final DefinitionToTypeNameConverter definitionToTypeNameConverter = new DefinitionToTypeNameConverter();

    FieldGenerator(final FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
    }

    @Override
    public FieldSpec generateField() {
        return builder(definitionToTypeNameConverter.getTypeName(fieldDefinition), fieldDefinition.getFieldName(), PRIVATE, FINAL).build();
    }

    @Override
    public Stream<MethodSpec> generateMethods() {
        return Stream.of(getterMethod());
    }

    private MethodSpec getterMethod() {
        return methodBuilder("get" + capitalize(fieldDefinition.getFieldName()))
                .addModifiers(PUBLIC)
                .returns(definitionToTypeNameConverter.getTypeName(fieldDefinition))
                .addCode(CodeBlock.builder().addStatement("return $L", fieldDefinition.getFieldName()).build())
                .build();
    }
}
