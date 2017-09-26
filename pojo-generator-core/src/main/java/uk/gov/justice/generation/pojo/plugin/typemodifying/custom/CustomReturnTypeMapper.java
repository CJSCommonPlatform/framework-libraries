package uk.gov.justice.generation.pojo.plugin.typemodifying.custom;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import uk.gov.justice.generation.pojo.plugin.PluginConfigurationException;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;
import uk.gov.justice.generation.pojo.visitor.ReferenceValue;

import java.util.Optional;
import java.util.Set;

import com.squareup.javapoet.ClassName;

public class CustomReturnTypeMapper {

    private final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter;

    public CustomReturnTypeMapper(final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter) {
        this.fullyQualifiedNameToClassNameConverter = fullyQualifiedNameToClassNameConverter;
    }

    public Optional<ClassName> customType(final ReferenceValue referenceValue, final PluginContext pluginContext) {

        final Set<String> propertyNames = pluginContext.getPropertyNames();
        final Optional<String> property = propertyNames
                .stream()
                .filter(propertyName -> propertyName.startsWith("reference"))
                .filter(propertyName -> propertyName.endsWith(referenceValue.getName()))
                .findFirst();

        if (property.isPresent()) {
            final String propertyName = property.get();
            final Optional<String> fullyQualifiedName = pluginContext.typeMappingOf(propertyName);

            if (fullyQualifiedName.isPresent()) {
                return of(fullyQualifiedNameToClassNameConverter.convert(fullyQualifiedName.get()));
            }

            throw new PluginConfigurationException(format("Failed to get generator property '%s'", propertyName));
        }

        return empty();
    }
}
