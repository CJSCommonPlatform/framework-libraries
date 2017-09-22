package uk.gov.justice.generation.pojo.plugin.classmodifying;

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
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * Plugin that adds fields, a constructor and getter methods to a class's
 * Type Specification.
 *
 * Without this plugin each class will be generated with
 * no fields nor getter methods and with an empty constructor. For this reason
 * this class is added by default; although it is possible to override it should
 * you need to change it's behaviour.
 */
public class AddFieldsAndMethodsToClassPlugin implements ClassModifyingPlugin {

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder classBuilder,
                                         final ClassDefinition classDefinition,
                                         final PluginContext pluginContext) {

        final List<Definition> fieldDefinitions = classDefinition.getFieldDefinitions();
        final JavaGeneratorFactory javaGeneratorFactory = pluginContext.getJavaGeneratorFactory();

        final List<ElementGeneratable> fieldGenerators = fieldDefinitions
                .stream()
                .map((Definition definition) -> javaGeneratorFactory.createGeneratorFor(definition, pluginContext))
                .collect(toList());

        final List<FieldSpec> fields = fieldGenerators
                .stream()
                .map(ElementGeneratable::generateField)
                .collect(toList());

        final List<MethodSpec> methods = fieldGenerators
                .stream()
                .flatMap(ElementGeneratable::generateMethods)
                .collect(toList());

        final MethodSpec constructor = buildConstructor(
                fieldDefinitions,
                pluginContext.getClassNameFactory(),
                pluginContext);
        classBuilder.addMethod(constructor)
                .addFields(fields)
                .addMethods(methods);

        return classBuilder;
    }

    private MethodSpec buildConstructor(
            final List<Definition> definitions,
            final ClassNameFactory classNameFactory,
            final PluginContext pluginContext) {
        final List<String> fieldNames = definitions.stream().map(Definition::getFieldName).collect(toList());

        final List<ParameterSpec> constructorParameters = constructorParameters(
                definitions,
                classNameFactory,
                pluginContext);
        return constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameters(constructorParameters)
                .addCode(constructorStatements(fieldNames))
                .build();
    }

    private CodeBlock constructorStatements(final List<String> names) {
        final CodeBlock.Builder builder = CodeBlock.builder();

        names.forEach(fieldName -> builder.addStatement("this.$N = $N", fieldName, fieldName));

        return builder.build();
    }

    private List<ParameterSpec> constructorParameters(
            final List<Definition> definitions,
            final ClassNameFactory classNameFactory,
            final PluginContext pluginContext) {
        return definitions.stream()
                .map(definition -> {
                    final TypeName typeName = classNameFactory.createTypeNameFrom(definition, pluginContext);
                    return ParameterSpec.builder(typeName, definition.getFieldName(), FINAL).build();
                })
                .collect(toList());
    }
}
