package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class PackageNameParserTest {

    @Test
    public void shouldParsePackageNameFromValidUriWithSchemaSuffix() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.schema.json";

        final Optional<String> packageName = new PackageNameParser().packageNameFrom(uri);

        assertThat(packageName, is(Optional.of("uk.gov.justice.standards.events")));
    }

    @Test
    public void shouldParsePackageNameFromValidUri() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.json";

        final Optional<String> packageName = new PackageNameParser().packageNameFrom(uri);

        assertThat(packageName, is(Optional.of("uk.gov.justice.standards.events")));
    }

    @Test
    public void shouldParsePackageNameIfThereIsNoHost() throws Exception {
        final Optional<String> packageName = new PackageNameParser().packageNameFrom("/standards/events/address.schema.json");

        assertThat(packageName, is(Optional.of("standards.events")));
    }

    @Test
    public void shouldReturnOptionalEmptyIfHashName() throws Exception {
        final Optional<String> packageName = new PackageNameParser().packageNameFrom("#address");

        assertThat(packageName, is(Optional.empty()));
    }

    @Test
    public void shouldReturnOptionalEmptyIfBlankUri() throws Exception {
        final Optional<String> packageName = new PackageNameParser().packageNameFrom("");

        assertThat(packageName, is(Optional.empty()));
    }

    @Test
    public void shouldReturnOptionalEmptyIfInvalidHost() throws Exception {
        final Optional<String> packageName = new PackageNameParser().packageNameFrom("http:,//justice.gov.uk/standards");

        assertThat(packageName, is(Optional.empty()));
    }

    @Test
    public void shouldReturnOptionalEmptyIfNoHost() throws Exception {
        final Optional<String> packageName = new PackageNameParser().packageNameFrom("http://./address.json");

        assertThat(packageName, is(Optional.empty()));
    }

    @Test
    public void shouldReturnOptionalEmptyIfInvalidUri() throws Exception {
        final Optional<String> packageName = new PackageNameParser().packageNameFrom("://justice.gov.uk");

        assertThat(packageName, is(Optional.empty()));
    }

    @Test
    public void shouldParsePackageNameAndNotAppendToBasePackageIfAbsoluteUri() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.schema.json";

        final String packageName = new PackageNameParser().appendToBasePackage(uri, "uk.gov.something.else");

        assertThat(packageName, is("uk.gov.justice.standards.events"));
    }

    @Test
    public void shouldParsePackageNameAndAppendToBasePackage() throws Exception {
        final String packageName = new PackageNameParser().appendToBasePackage("events/address.schema.json", "uk.gov.justice.standards");

        assertThat(packageName, is("uk.gov.justice.standards.events"));
    }

    @Test
    public void shouldParsePackageNameIfEmptyReturnBasePackage() throws Exception {
        final String packageName = new PackageNameParser().appendToBasePackage("address.schema.json", "uk.gov.justice.standards");

        assertThat(packageName, is("uk.gov.justice.standards"));
    }

    @Test
    public void shouldReturnBasePackageNameIfInvalidUri() throws Exception {
        final String packageName = new PackageNameParser().appendToBasePackage("://justice.gov.uk", "uk.gov.justice.standards");

        assertThat(packageName, is("uk.gov.justice.standards"));
    }
}