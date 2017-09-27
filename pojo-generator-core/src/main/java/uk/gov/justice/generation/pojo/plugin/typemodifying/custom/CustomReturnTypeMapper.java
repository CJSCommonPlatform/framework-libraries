package uk.gov.justice.generation.pojo.plugin.typemodifying.custom;

import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.visitor.ReferenceValue;

import java.util.Optional;

import com.squareup.javapoet.ClassName;

public class CustomReturnTypeMapper {

    private final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter;

    public CustomReturnTypeMapper(final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter) {
        this.fullyQualifiedNameToClassNameConverter = fullyQualifiedNameToClassNameConverter;
    }

    public Optional<ClassName> customType(final ReferenceValue referenceValue, final PluginContext pluginContext) {

        final String referenceValueName = referenceValue.getName();

        final Optional<String> fullyQualifiedName = pluginContext.typeMappingOf(referenceValueName);

        return fullyQualifiedName.map(fullyQualifiedNameToClassNameConverter::convert);

    }
}
