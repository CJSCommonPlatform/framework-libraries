package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.stream.Stream;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

/**
 * A generator for creating the containing field and accessor methods for a
 * {@link uk.gov.justice.generation.pojo.dom.EnumDefinition}
 * or {@link uk.gov.justice.generation.pojo.dom.ClassDefinition}
 */
public class ElementGenerator implements ElementGeneratable {

    private final Definition classTypeDefinition;
    private final ClassNameFactory classNameFactory;

    ElementGenerator(final Definition classTypeDefinition, final ClassNameFactory classNameFactory) {
        this.classTypeDefinition = classTypeDefinition;
        this.classNameFactory = classNameFactory;
    }

    @Override
    public FieldSpec generateField() {
        return builder(classNameFactory.createTypeNameFrom(classTypeDefinition), classTypeDefinition.getFieldName(), PRIVATE, FINAL).build();
    }

    @Override
    public Stream<MethodSpec> generateMethods() {
        return Stream.of(methodBuilder(toGetterMethodName())
                .addModifiers(PUBLIC)
                .returns(classNameFactory.createTypeNameFrom(classTypeDefinition))
                .addCode(CodeBlock.builder().addStatement("return $L", classTypeDefinition.getFieldName()).build())
                .build());
    }

    private String toGetterMethodName() {
        return "get" + capitalize(classTypeDefinition.getFieldName());
    }
}
