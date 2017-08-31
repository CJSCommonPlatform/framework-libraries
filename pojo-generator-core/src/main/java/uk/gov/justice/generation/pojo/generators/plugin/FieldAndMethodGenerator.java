package uk.gov.justice.generation.pojo.generators.plugin;

import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.ElementGeneratable;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import java.util.List;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

public class FieldAndMethodGenerator implements PluginClassGeneratable {

    private final ClassNameFactory classNameFactory = new ClassNameFactory();

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder typeSpecBuilder,
                                         final ClassDefinition classDefinition,
                                         final JavaGeneratorFactory javaGeneratorFactory) {

        final List<Definition> fieldDefinitions = classDefinition.getFieldDefinitions();

        final List<ElementGeneratable> fieldGenerators = fieldDefinitions
                .stream()
                .map(javaGeneratorFactory::createGeneratorFor)
                .collect(toList());

        final List<FieldSpec> fields = fieldGenerators
                .stream()
                .map(ElementGeneratable::generateField)
                .collect(toList());

        final List<MethodSpec> methods = fieldGenerators
                .stream()
                .flatMap(ElementGeneratable::generateMethods)
                .collect(toList());

        typeSpecBuilder.addMethod(buildConstructor(fieldDefinitions))
                .addFields(fields)
                .addMethods(methods);

        return typeSpecBuilder;
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
                .map(definition -> ParameterSpec.builder(classNameFactory.createClassNameFrom(definition), definition.getFieldName(), FINAL).build())
                .collect(toList());
    }
}
