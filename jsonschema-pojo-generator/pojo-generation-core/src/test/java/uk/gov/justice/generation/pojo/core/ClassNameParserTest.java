package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ClassNameParserTest {

    @Test
    public void shouldParseClassNameFromValidUri() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.json";

        final String className = new ClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("Address"));
    }

    @Test
    public void shouldParseClassNameFromValidUriWithSchemaExtension() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.schema.json";

        final String className = new ClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("Address"));
    }

    @Test
    public void shouldParseClassNameFromValidUriWithHashName() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/address.json#post-code";

        final String className = new ClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("PostCode"));
    }

    @Test
    public void shouldParseClassNameFromValidUriWithPrefix() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/example.events.address.json";

        final String className = new ClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("Address"));
    }

    @Test
    public void shouldParseClassNameFromValidUriWithPrefixAndSchemaExtension() throws Exception {
        final String uri = "http://justice.gov.uk/standards/events/example.events.address.schema.json";

        final String className = new ClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("Address"));
    }

    @Test
    public void shouldParseClassNameFromHashName() throws Exception {
        final String uri = "#post-code";

        final String className = new ClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("PostCode"));
    }

    @Test
    public void shouldParseClassNameWithNoHashOrJsonExtension() throws Exception {
        final String uri = "t/address";

        final String className = new ClassNameParser().simpleClassNameFrom(uri);

        assertThat(className, is("Address"));
    }
}