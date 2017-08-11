package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.List;
import java.util.stream.Stream;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

public class ClassGenerator implements SourceCodeGenerator {

    private final ClassDefinition classDefinition;
    private final SourceCodeGeneratorFactory sourceCodeGeneratorFactory = new SourceCodeGeneratorFactory();
    private final DefinitionToTypeNameConverter definitionToTypeNameConverter = new DefinitionToTypeNameConverter();

    public ClassGenerator(final ClassDefinition classDefinition) {
        this.classDefinition = classDefinition;
    }

    public TypeSpec generate() {

        final String className = classDefinition.getClassName().getSimpleName();
        final List<Definition> definitions = classDefinition.getFieldDefinitions();

        final List<SourceCodeGenerator> generators = definitions
                .stream()
                .map(sourceCodeGeneratorFactory::createFor)
                .collect(toList());

        final List<FieldSpec> fields = generators
                .stream()
                .map(SourceCodeGenerator::generateField)
                .collect(toList());

        final List<MethodSpec> methods = generators
                .stream()
                .flatMap(SourceCodeGenerator::generateMethods)
                .collect(toList());

        return classBuilder(className)
                .addModifiers(PUBLIC)
                .addMethod(buildConstructor(definitions))
                .addFields(fields)
                .addMethods(methods)
                .build();
    }

    public ClassDefinition getClassDefinition() {
        return classDefinition;
    }

    private MethodSpec buildConstructor(final List<Definition> definitions) {
        final List<String> fieldNames = definitions.stream().map(Definition::getFieldName).collect(toList());

        return constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameters(constructorParameters(definitions))
                .addCode(constructorStatements(fieldNames))
                .build();
    }

    private CodeBlock constructorStatements(final List<String> names) {
        final CodeBlock.Builder builder = CodeBlock.builder();

        names.forEach(fieldName -> builder.addStatement("this.$N = $N", fieldName, fieldName));

        return builder.build();
    }

    private List<ParameterSpec> constructorParameters(final List<Definition> definitions) {
        return definitions.stream()
                .map(definition -> ParameterSpec.builder(definitionToTypeNameConverter.getTypeName(definition), definition.getFieldName(), FINAL).build())
                .collect(toList());
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
