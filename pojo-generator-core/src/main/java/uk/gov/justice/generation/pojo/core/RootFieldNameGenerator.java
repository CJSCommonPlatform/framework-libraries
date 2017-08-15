package uk.gov.justice.generation.pojo.core;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.io.File;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class RootFieldNameGenerator {

    public String generateNameFrom(final File jsonSchemaFile) {

        final String fileName = jsonSchemaFile.getName();

        if (!fileName.endsWith(".json")) {
            throw new JsonSchemaParseException(format("Failed to load json schema file '%s'. File does not have a '.json' extension", jsonSchemaFile.getAbsolutePath()));
        }

        final String name = getNameFrom(fileName);

        if (name.contains(".") || name.isEmpty()) {
            throw new JsonSchemaParseException(format("Failed to load json schema file '%s'. File name is invalid", jsonSchemaFile.getAbsolutePath()));
        }

        final String className = Stream.of(name.split("-")).map(StringUtils::capitalize).collect(joining());

        return uncapitalize(className);
    }

    private String getNameFrom(final String fileName) {
        final String name = fileName.substring(0, fileName.lastIndexOf('.'));

        final int index = name.lastIndexOf('.');
        if (index > -1) {
            return name.substring(index + 1);
        }

        return name;
    }
}
