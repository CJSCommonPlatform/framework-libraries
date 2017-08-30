package uk.gov.justice.generation.pojo.core;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.io.File;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class NameGenerator {

    public String rootFieldNameFrom(final File jsonSchemaFile) {

        final String fileName = getValidFileNameWithNoExtension(jsonSchemaFile);

        final String name = getNameFrom(fileName);

        if (name.contains(".") || name.isEmpty()) {
            throw new SchemaLoadingException(format("Failed to load json schema file '%s'. File name is invalid", jsonSchemaFile.getAbsolutePath()));
        }

        final String className = Stream.of(name.split("-")).map(StringUtils::capitalize).collect(joining());

        return uncapitalize(className);
    }

    public String eventNameFrom(final File jsonSchemaFile) {
        return getValidFileNameWithNoExtension(jsonSchemaFile);
    }

    private String getValidFileNameWithNoExtension(final File jsonSchemaFile) {
        final String fileName = jsonSchemaFile.getName();

        if (!fileName.endsWith(".json")) {
            throw new SchemaLoadingException(format("Failed to load json schema file '%s'. File does not have a '.json' extension", jsonSchemaFile.getAbsolutePath()));
        }

        return removeFileExtensionFrom(fileName);
    }

    private String removeFileExtensionFrom(final String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    private String getNameFrom(final String fileName) {
        final int index = fileName.lastIndexOf('.');
        if (index > -1) {
            return fileName.substring(index + 1);
        }

        return fileName;
    }
}
