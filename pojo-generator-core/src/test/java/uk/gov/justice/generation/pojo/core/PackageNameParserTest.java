package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

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
    public void shouldNotParsePackageNameIfThereIsNoProtocolOrHostOrPath() throws Exception {
        final Optional<String> packageName = new PackageNameParser().packageNameFrom("address");

        assertThat(packageName, is(Optional.empty()));
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
}