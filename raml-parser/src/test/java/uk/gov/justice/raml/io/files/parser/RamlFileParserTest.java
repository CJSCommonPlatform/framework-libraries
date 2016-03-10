package uk.gov.justice.raml.io.files.parser;

import org.junit.Test;
import org.raml.model.Raml;

import java.util.Collection;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

/**
 * Unit tests for the {@link RamlFileParser} class.
 */
public class RamlFileParserTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldParseRamlFiles() throws Exception {

        RamlFileParser parser = new RamlFileParser();

        Collection<Raml> ramls = parser.parse(
                get("src/test/resources/raml/"),
                asList(
                        get("example-1.raml"),
                        get("example-2.raml")));

        assertThat(ramls, hasSize(2));
        assertThat(ramls, containsInAnyOrder(
                allOf(
                        hasProperty("baseUri", equalTo("http://localhost:8080/")),
                        hasProperty("mediaType", equalTo("application/json")),
                        hasProperty("resources", hasEntry(
                                equalTo("/example1"),
                                hasProperty("uri", equalTo("/example1"))
                                )
                        )
                ),
                allOf(
                        hasProperty("baseUri", equalTo("http://localhost:8080/")),
                        hasProperty("mediaType", equalTo("application/json")),
                        hasProperty("resources", hasEntry(
                                equalTo("/example2"),
                                hasProperty("uri", equalTo("/example2"))
                                )
                        )
                )
        ));
    }
}
