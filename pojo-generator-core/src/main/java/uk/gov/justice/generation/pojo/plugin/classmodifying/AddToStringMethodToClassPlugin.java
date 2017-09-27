package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.CodeBlock.of;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Plugin to add a {@code toString()} method to the generated class.
 */
public class AddToStringMethodToClassPlugin implements ClassModifyingPlugin {

    private final AdditionalPropertiesDeterminer additionalPropertiesDeterminer;

    public AddToStringMethodToClassPlugin(final AdditionalPropertiesDeterminer additionalPropertiesDeterminer) {
        this.additionalPropertiesDeterminer = additionalPropertiesDeterminer;
    }

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder classBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {


        final ClassName className = pluginContext
                .getClassNameFactory()
                .createClassNameFrom(classDefinition);

        final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("toString")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(get(String.class));


        methodBuilder
                .addCode(of("return \"" + className.simpleName() + "{\" +\n"));

        final List<String> fieldNames = classDefinition.getFieldDefinitions()
                .stream()
                .map(Definition::getFieldName)
                .collect(toList());

        if(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)) {
            fieldNames.add("additionalProperties");
        }

        for (int index = 0; index < fieldNames.size(); index++) {
            final String fieldName = fieldNames.get(index);

            final String maybeComma = getComma(fieldNames, index);

            methodBuilder.addCode(of("\t\"$L='\" + $L + \"'$L\" +\n", fieldName, fieldName, maybeComma));
        }

        methodBuilder.addCode("\"}\";\n");

        return classBuilder.addMethod(methodBuilder.build());
    }

    private String getComma(final List<String> fieldNames, final int index) {

        if (index < fieldNames.size() - 1) {
            return  ",";
        }
        
        return "";
    }
}
