package uk.gov.justice.generation.pojo.core;

import static java.util.stream.Collectors.joining;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class ClassNameParser {

    private static final String URI_SEPERATOR = "/";
    private static final char FILE_EXTENSION_SEPERATOR = '.';
    private static final String DASH = "-";
    private static final int BEGIN_INDEX = 0;
    private static final String HASH = "#";

    public String simpleClassNameFrom(final String uri) {
        final String name = getNameFrom(removeFileExtensionOrGetNameAfterHash(removePackagePartFrom(uri)));
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

        return uri.substring(BEGIN_INDEX, uri.lastIndexOf(FILE_EXTENSION_SEPERATOR));
    }

    private String getNameFrom(final String name) {
        final int index = name.indexOf(FILE_EXTENSION_SEPERATOR);
        if (index > -1) {
            return name.substring(BEGIN_INDEX, index);
        }

        return name;
    }
}
