package uk.gov.justice.generation.pojo.core;

import static java.util.stream.Collectors.joining;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class PackageAndClassNameParser {

    private static final String URI_SEPERATOR = "/";
    private static final String PROTOCOL_SEPERATOR = "://";
    private static final String HOST_SEPERATOR_REGEX = "\\.";
    private static final String PACKAGE_SEPERATOR = ".";
    private static final char FILE_EXTENSION_SEPERATOR = '.';
    private static final String DASH = "-";
    private static final int BEGIN_INDEX = 0;
    private static final String HASH = "#";

    public Optional<String> packageNameFrom(final String uri) {
        if (validateUri(uri)) return Optional.empty();

        if(uri.isEmpty() || uri.indexOf(HASH) == BEGIN_INDEX) {
            return Optional.empty();
        }

        return createPackageNameFrom(removeNamePartFrom(removeProtocolFrom(uri)));
    }

    public String simpleClassNameFrom(final String uri) {
        final String name = getNameFrom(removeFileExtensionOrGetNameAfterHash(removePackagePartFrom(uri)));
        return Stream.of(name.split(DASH))
                .map(StringUtils::capitalize)
                .collect(joining());
    }

    private boolean validateUri(final String uri) {
        try {
            new URI(uri);
        } catch (URISyntaxException e) {
            return true;
        }

        return false;
    }

    private String removeProtocolFrom(final String uri) {
        if (uri.contains(PROTOCOL_SEPERATOR)) {
            final String[] splitFromProtocol = uri.split(PROTOCOL_SEPERATOR);
            return splitFromProtocol[1];
        }

        return uri;
    }

    private String removeNamePartFrom(final String uri) {
        if (uri.contains(URI_SEPERATOR)) {
            return uri.substring(BEGIN_INDEX, uri.lastIndexOf(URI_SEPERATOR));
        }

        return uri;
    }

    private Optional<String> createPackageNameFrom(final String uri) {
        final String[] parts = uri.split(URI_SEPERATOR);
        final StringBuilder builder = new StringBuilder();

        if (parts.length > 0) {

            if (!parts[0].isEmpty()) {
                builder.append(reverseHost(parts[0]));
            }

            if (parts.length > 1) {
                for (int index = 1; index < parts.length; index++) {

                    if (builder.length() > 0) {
                        builder.append(PACKAGE_SEPERATOR);
                    }

                    builder.append(parts[index]);
                }
            }

            return Optional.of(builder.toString());
        }

        return Optional.empty();
    }

    private String reverseHost(final String host) {
        final String[] parts = host.split(HOST_SEPERATOR_REGEX);

        if (parts.length > 0) {
            final StringBuilder builder = new StringBuilder();

            for (int index = parts.length - 1; index >= BEGIN_INDEX; index--) {
                builder.append(parts[index]);

                if (index > 0) {
                    builder.append(PACKAGE_SEPERATOR);
                }
            }

            return builder.toString();
        }

        return host;
    }

    private String removePackagePartFrom(final String uri) {
        if (uri.contains(URI_SEPERATOR)) {
            return uri.substring(uri.lastIndexOf(URI_SEPERATOR) + 1);
        }

        return uri;
    }

    private String removeFileExtensionOrGetNameAfterHash(final String uri) {
        if(uri.contains(HASH)) {
            return uri.substring(uri.indexOf(HASH) + 1);
        }

        return uri.substring(BEGIN_INDEX, uri.lastIndexOf(FILE_EXTENSION_SEPERATOR));
    }

    private String getNameFrom(final String name) {
        final int index = name.indexOf('.');
        if (index > -1) {
            return name.substring(BEGIN_INDEX, index);
        }

        return name;
    }
}
