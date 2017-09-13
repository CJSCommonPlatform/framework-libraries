package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.PRIVATE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;

import java.util.List;

import com.squareup.javapoet.FieldSpec;

public class BuilderFieldFactory {

    public List<FieldSpec> createFields(final List<Definition> fieldDefinitions, final ClassNameFactory classNameFactory) {
        return fieldDefinitions.stream()
                .map(fieldDefinition -> FieldSpec.builder(classNameFactory.createTypeNameFrom(fieldDefinition), fieldDefinition.getFieldName(), PRIVATE).build())
                .collect(toList());
    }
}
