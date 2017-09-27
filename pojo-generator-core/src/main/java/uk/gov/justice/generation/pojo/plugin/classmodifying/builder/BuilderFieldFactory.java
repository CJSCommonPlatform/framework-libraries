package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.PRIVATE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.List;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

/**
 * Factory for creating the fields in the Builder class
 */
public class BuilderFieldFactory {

    /**
     * 
     * @param fieldDefinitions The list of {@link Definition}s from which to create fields
     * @param classNameFactory The factory for creating the class name
     *
     * @return A list of {@link FieldSpec}s of the fields of the Builder
     */
    public List<FieldSpec> createFields(
            final List<Definition> fieldDefinitions,
            final ClassNameFactory classNameFactory,
            final PluginContext pluginContext) {
        return fieldDefinitions.stream()
                .map(fieldDefinition -> {
                    final TypeName typeName = classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext);
                    return FieldSpec.builder(typeName, fieldDefinition.getFieldName(), PRIVATE).build();
                })
                .collect(toList());
    }
}
