package uk.gov.justice.generation.pojo.plugin.typemodifying.custom;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.plugin.PluginConfigurationException;

import com.squareup.javapoet.ClassName;
import org.apache.commons.lang3.StringUtils;

public class FullyQualifiedNameToClassNameConverter {

    public ClassName convert(final String fullyQualifiedClassName) {

        final int lastDot = fullyQualifiedClassName.lastIndexOf('.');

        if (lastDot < 0) {
            final String message = format(
                    "Failed to create class name from fully qualified name '%s'",
                    fullyQualifiedClassName
            );

            throw new PluginConfigurationException(message);
        }

        final String packageName = fullyQualifiedClassName.substring(0, lastDot);
        final String simpleName = fullyQualifiedClassName.substring(lastDot + 1);

        if (StringUtils.isEmpty(packageName)) {
            final String message = format(
                    "Cannot create class name from fully qualified name '%s'. No package name found",
                    fullyQualifiedClassName
            );

            throw new PluginConfigurationException(message);
        }

        if (StringUtils.isEmpty(simpleName)) {
            final String message = format(
                    "Cannot create class name from fully qualified name '%s'. No simple name found",
                    fullyQualifiedClassName
            );

            throw new PluginConfigurationException(message);
        }

        return ClassName.get(packageName, simpleName);
    }
}
