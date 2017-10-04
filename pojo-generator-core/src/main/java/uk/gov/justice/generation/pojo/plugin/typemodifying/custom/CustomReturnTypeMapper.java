package uk.gov.justice.generation.pojo.plugin.typemodifying.custom;

import uk.gov.justice.generation.pojo.core.TypeMapping;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.Optional;
import java.util.function.Predicate;

import com.squareup.javapoet.ClassName;

public class CustomReturnTypeMapper {

    private final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter;

    public CustomReturnTypeMapper(final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter) {
        this.fullyQualifiedNameToClassNameConverter = fullyQualifiedNameToClassNameConverter;
    }

    public Optional<ClassName> customTypeFor(final Predicate<TypeMapping> mappingType,
                                             final String propertyName,
                                             final PluginContext pluginContext) {

        final Optional<String> fullyQualifiedName = pluginContext.typeMappingsFilteredBy(mappingType, propertyName);

        return fullyQualifiedName.map(fullyQualifiedNameToClassNameConverter::convert);
    }
}
