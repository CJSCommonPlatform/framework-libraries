package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

public class PackageAndClassNameParserTest {

    @Test
    public void shouldParsePackageNameFromValidUri() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.schema.json";

        final Optional<String> packageName = new PackageAndClassNameParser().packageNameFrom(uri);

        assertThat(packageName, is(Optional.of("uk.gov.justice.standards.events")));
    }

    @Test
    public void shouldParsePackageNameIfThereIsNoProtocol() throws Exception {
        final Optional<String> packageName = new PackageAndClassNameParser().packageNameFrom("justice.gov.uk/standards/events/address.schema.json");

        assertThat(packageName, is(Optional.of("uk.gov.justice.standards.events")));
    }

    @Test
    public void shouldParsePackageNameIfThereIsNoHost() throws Exception {
        final Optional<String> packageName = new PackageAndClassNameParser().packageNameFrom("http:///standards/events/address.schema.json");

        assertThat(packageName, is(Optional.of("standards.events")));
    }

    @Test
    public void shouldNotParsePackageNameIfThereIsNoProtocolOrHostOrPath() throws Exception {
        final Optional<String> packageName = new PackageAndClassNameParser().packageNameFrom("address");

        assertThat(packageName, is(Optional.of("address")));
    }

    @Test
    public void shouldReturnOptionalEmptyIfHashName() throws Exception {
        final Optional<String> packageName = new PackageAndClassNameParser().packageNameFrom("#address");

        assertThat(packageName, is(Optional.empty()));
    }

    @Test
    public void shouldReturnOptionalEmptyIfBlankUri() throws Exception {
        final Optional<String> packageName = new PackageAndClassNameParser().packageNameFrom("");

        assertThat(packageName, is(Optional.empty()));
    }

    @Test
    public void shouldParseClassNameFromValidUri() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.schema.json";

        final String className = new PackageAndClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("Address"));
    }

    @Test
    public void shouldParseClassNameFromValidUriWithOutSchemaPrefix() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.json";

        final String className = new PackageAndClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("Address"));
    }

    @Test
    public void shouldParseClassNameFromValidUriWithHashName() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.json#post-code";

        final String className = new PackageAndClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("PostCode"));
    }

    @Test
    public void shouldParseClassNameFromHashName() throws Exception {
        final String uri = "#post-code";

        final String className = new PackageAndClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("PostCode"));
    }
}