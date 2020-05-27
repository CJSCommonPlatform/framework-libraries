package uk.gov.justice.generation.pojo.core;

import static java.util.stream.Collectors.joining;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class ClassNameParser {

    private static final String URI_SEPERATOR = "/";
    private static final String EXTENSION_SEPERATOR = ".";
    private static final String DASH = "-";
    private static final int BEGIN_INDEX = 0;
    private static final String HASH = "#";
    private static final String JSON_FILE_EXTENSION = ".json";
    private static final String SCHEMA_EXTENSION = ".schema";

    public String simpleClassNameFrom(final String uri) {
        final String name = removeNamePrefix(
                removeSchemaExtension(
                        removeFileExtensionOrGetNameAfterHash(
                                removePackagePartFrom(uri)
                        )
                )
        );

        return Stream.of(name.split(DASH))
                .map(StringUtils::capitalize)
                .collect(joining());
    }

    private String removePackagePartFrom(final String uri) {
        if (uri.contains(URI_SEPERATOR)) {
            return uri.substring(uri.lastIndexOf(URI_SEPERATOR) + 1);
        }

        return uri;
    }

    private String removeFileExtensionOrGetNameAfterHash(final String uri) {
        if (uri.contains(HASH)) {
            return uri.substring(uri.indexOf(HASH) + 1);
        }

        if (uri.contains(JSON_FILE_EXTENSION)) {
            return uri.substring(BEGIN_INDEX, uri.lastIndexOf(JSON_FILE_EXTENSION));
        }

        return uri;
    }

    private String removeNamePrefix(final String name) {
        if (name.contains(EXTENSION_SEPERATOR)) {
            return name.substring(name.lastIndexOf(EXTENSION_SEPERATOR) + 1);
        }

        return name;
    }

    private String removeSchemaExtension(final String name) {
        if (name.contains(SCHEMA_EXTENSION)) {
            return name.substring(BEGIN_INDEX, name.lastIndexOf(SCHEMA_EXTENSION));
        }

        return name;
    }
}
