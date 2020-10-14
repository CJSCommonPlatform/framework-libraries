package uk.gov.justice.schema.catalog.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UriResolverTest {

    @InjectMocks
    private UriResolver uriResolver;

    @Test
    public void shouldResolveANonOpaqueUri() throws Exception {
        final URI baseUri = new URI("file:/src/target/example-standards-1.0.0-SNAPSHOT.jar!/json/schema/schema_catalog.json");
        final URI otherUri = new URI("ingredient.json");

        assertThat(uriResolver.resolve(baseUri, otherUri).toString(), is("file:/src/target/example-standards-1.0.0-SNAPSHOT.jar!/json/schema/ingredient.json"));
    }

    @Test
    public void shouldResolveAnOpaqueUri() throws Exception {

        final URI baseUri = new URI("jar:file:/src/target/example-standards-1.0.0-SNAPSHOT.jar!/json/schema/schema_catalog.json");
        final URI otherUri = new URI("ingredient.json");

        assertThat(uriResolver.resolve(baseUri, otherUri).toString(), is("jar:file:/src/target/example-standards-1.0.0-SNAPSHOT.jar!/json/schema/ingredient.json"));
    }
}
