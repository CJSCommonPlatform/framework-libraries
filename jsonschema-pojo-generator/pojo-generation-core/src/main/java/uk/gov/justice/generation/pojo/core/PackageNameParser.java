package uk.gov.justice.generation.pojo.core;

import static java.util.stream.Collectors.joining;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class PackageNameParser {

    private static final String URI_SEPERATOR = "/";
    private static final String HOST_SEPERATOR_REGEX = "\\.";
    private static final String PACKAGE_SEPERATOR = ".";
    private static final int BEGIN_INDEX = 0;
    private static final String HASH = "#";

    public String appendToBasePackage(final String uri, final String basePackage) {
        final Optional<URI> validUri = validUri(uri);
        final Optional<String> packageName = packageNameFrom(uri);

        if (validUri.isPresent() && validUri.get().isAbsolute() && packageName.isPresent()) {
            return packageName.get();
        }

        return packageName
                .map(packageNameValue -> basePackage + PACKAGE_SEPERATOR + packageNameValue)
                .orElse(basePackage);
    }

    public Optional<String> packageNameFrom(final String uri) {
        final Optional<URI> validUri = validUri(uri);

        if (!validUri.isPresent() || uri.isEmpty() || uri.indexOf(HASH) == BEGIN_INDEX) {
            return Optional.empty();
        }

        return createPackageNameFrom(validUri.get());
    }

    private Optional<URI> validUri(final String uri) {
        try {
            return Optional.of(new URI(uri));
        } catch (URISyntaxException e) {
            //Do Nothing
        }

        return Optional.empty();
    }

    private Optional<String> createPackageNameFrom(final URI uri) {
        final String packageName = Stream.of(convertHostToPackage(uri), convertPathToPackage(uri))
                .flatMap(filterOptionalEmpty())
                .collect(joining(PACKAGE_SEPERATOR));

        return Optional.of(packageName).filter(packageNameValue -> !packageNameValue.isEmpty());
    }

    private Optional<String> convertPathToPackage(final URI uri) {
        final String path = uri.getPath();

        if (null != path) {

            final String[] parts = removeNamePartFrom(path).split(URI_SEPERATOR);

            if (parts.length > 0) {
                final StringBuilder builder = new StringBuilder();

                for (int index = 0; index < parts.length; index++) {

                    if (builder.length() > 0) {
                        builder.append(PACKAGE_SEPERATOR);
                    }

                    builder.append(parts[index]);
                }

                return Optional.of(builder.toString());
            }
        }

        return Optional.empty();
    }

    private Optional<String> convertHostToPackage(final URI uri) {
        final String host = uri.getHost();

        if (null != host) {

            final String[] parts = host.split(HOST_SEPERATOR_REGEX);

            if (parts.length > 0) {
                final StringBuilder builder = new StringBuilder();

                for (int index = parts.length - 1; index >= BEGIN_INDEX; index--) {
                    builder.append(parts[index]);

                    if (index > 0) {
                        builder.append(PACKAGE_SEPERATOR);
                    }
                }

                return Optional.of(builder.toString());
            }
        }

        return Optional.empty();
    }

    private String removeNamePartFrom(final String uri) {
        if (uri.contains(URI_SEPERATOR)) {
            return uri.substring(BEGIN_INDEX, uri.lastIndexOf(URI_SEPERATOR));
        }

        return "";
    }

    private Function<Optional<String>, Stream<? extends String>> filterOptionalEmpty() {
        return packagePart -> packagePart
                .map(Stream::of)
                .orElseGet(Stream::empty);
    }
}
