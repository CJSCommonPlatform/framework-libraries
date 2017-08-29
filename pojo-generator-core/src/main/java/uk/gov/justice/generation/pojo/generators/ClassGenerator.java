package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.TypeName.LONG;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.Definition;

import java.io.Serializable;
import java.util.List;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

public class ClassGenerator implements ClassGeneratable {

    private static final String SERIAL_VERSION_FIELD_NAME = "serialVersionUID";
    private static final String SERIAL_VERSION_VALUE = "1L";

    private final ClassDefinition classDefinition;
    private final JavaGeneratorFactory javaGeneratorFactory;
    private final DefinitionToTypeNameConverter definitionToTypeNameConverter = new DefinitionToTypeNameConverter();

    public ClassGenerator(final ClassDefinition classDefinition, final JavaGeneratorFactory javaGeneratorFactory) {
        this.classDefinition = classDefinition;
        this.javaGeneratorFactory = javaGeneratorFactory;
    }

    @Override
    public TypeSpec generate() {

        final String className = classDefinition.getClassName().getSimpleName();
        final List<Definition> definitions = classDefinition.getFieldDefinitions();

        final List<ElementGeneratable> generators = definitions
                .stream()
                .map(javaGeneratorFactory::createGeneratorFor)
                .collect(toList());

        final List<FieldSpec> fields = generators
                .stream()
                .map(ElementGeneratable::generateField)
                .collect(toList());

        final List<MethodSpec> methods = generators
                .stream()
                .flatMap(ElementGeneratable::generateMethods)
                .collect(toList());

        final TypeSpec.Builder typeSpecBuilder = classBuilder(className)
                .addModifiers(PUBLIC)
                .addSuperinterface(Serializable.class)
                .addField(FieldSpec
                        .builder(LONG, SERIAL_VERSION_FIELD_NAME, PRIVATE, STATIC, FINAL)
                        .initializer(SERIAL_VERSION_VALUE)
                        .build());

        classDefinition.getEventName().ifPresent(eventName ->
                typeSpecBuilder.addAnnotation(AnnotationSpec.builder(Event.class)
                        .addMember("value", "$S", eventName)
                        .build()));

        return typeSpecBuilder.addMethod(buildConstructor(definitions))
                .addFields(fields)
                .addMethods(methods)
                .build();
    }

    @Override
    public ClassName getClassName() {
        return classDefinition.getClassName();
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

}
