package uk.gov.justice.schema.catalog;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.domain.Group;
import uk.gov.justice.schema.catalog.domain.Schema;
import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URI;
import java.net.URL;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;


@RunWith(MockitoJUnitRunner.class)
public class CatalogToSchemaResolverTest {

    @Mock
    private ClasspathCatalogLoader classpathCatalogLoader;

    @Spy
    @SuppressWarnings("unused")
    private final SchemaResolver schemaResolver = new SchemaResolver(new UrlConverter(), new UriResolver());

    @Mock
    private Logger logger;

    @InjectMocks
    private CatalogToSchemaResolver catalogToSchemaResolver;

    @Test
    public void shouldMapSchemasFoundOnTheClasspathToTheirIds() throws Exception {

        final Catalog catalog = mock(Catalog.class);
        final Group group = mock(Group.class);
        final Schema schema = mock(Schema.class);

        final URI uri = new URI("file:/src/main/schema.json");

        final Map<URI, Catalog> catalogPojoMap = ImmutableMap.of(uri, catalog);

        when(classpathCatalogLoader.getCatalogs()).thenReturn(catalogPojoMap);
        when(catalog.getGroups()).thenReturn(singletonList(group));
        when(group.getSchemas()).thenReturn(singletonList(schema));

        when(schema.getLocation()).thenReturn("some/path/to.json");
        when(schema.getId()).thenReturn("schemaId");

        final Map<String, URL> schemaLocations = catalogToSchemaResolver.resolveSchemaLocations();

        assertThat(schemaLocations.size(), is(1));

        assertThat(schemaLocations.containsKey("schemaId"), is(true));
        assertThat(schemaLocations.get("schemaId").toString(), is("file:/src/main/some/path/to.json"));
    }

    @Test
    public void shouldHandleDuplicates() throws Exception {

        final Catalog catalog_1 = mock(Catalog.class);
        final Catalog catalog_2 = mock(Catalog.class);
        final Group group_1 = mock(Group.class);
        final Group group_2 = mock(Group.class);
        final Schema schema_1 = mock(Schema.class);
        final Schema schema_2 = mock(Schema.class);

        final URI catalogLocation_1 = new URI("file:/src/main/catalog.json");
        final URI catalogLocation_2 = new URI("file:/src/main/another-catalog.json");

        final Map<URI, Catalog> catalogPojoMap = ImmutableMap.of(catalogLocation_1, catalog_1, catalogLocation_2, catalog_2);

        when(classpathCatalogLoader.getCatalogs()).thenReturn(catalogPojoMap);
        when(catalog_1.getGroups()).thenReturn(singletonList(group_1));
        when(catalog_2.getGroups()).thenReturn(singletonList(group_2));
        when(group_1.getSchemas()).thenReturn(singletonList(schema_1));
        when(group_2.getSchemas()).thenReturn(singletonList(schema_2));

        when(schema_1.getLocation()).thenReturn("some/path/to.json");
        when(schema_2.getLocation()).thenReturn("some/other/path/to.json");
        when(schema_1.getId()).thenReturn("schemaId");
        when(schema_2.getId()).thenReturn("schemaId");

        final Map<String, URL> schemaLocations = catalogToSchemaResolver.resolveSchemaLocations();

        assertThat(schemaLocations.size(), is(1));

        assertThat(schemaLocations.containsKey("schemaId"), is(true));
        assertThat(schemaLocations.get("schemaId").toString(), is("file:/src/main/some/path/to.json"));

        verify(logger).warn("Found duplicate schema id 'schemaId' for schemaLocations 'file:/src/main/some/path/to.json' and 'file:/src/main/some/other/path/to.json'");
    }
}
