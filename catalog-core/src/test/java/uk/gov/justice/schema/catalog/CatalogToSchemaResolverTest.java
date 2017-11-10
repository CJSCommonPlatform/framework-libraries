package uk.gov.justice.schema.catalog;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.domain.Group;
import uk.gov.justice.schema.catalog.domain.Schema;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URL;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class CatalogToSchemaResolverTest {

    @Mock
    private ClasspathCatalogLoader classpathCatalogLoader;

    @Spy
    @SuppressWarnings("unused")
    private final SchemaResolver schemaResolver = new SchemaResolver(new UrlConverter());

    @InjectMocks
    private CatalogToSchemaResolver catalogToSchemaResolver;

    @Test
    public void shouldMapSchemasFoundOnTheClasspathToTheirIds() throws Exception {

        final Catalog catalog = mock(Catalog.class);
        final Group group = mock(Group.class);
        final Schema schema = mock(Schema.class);

        final URL url = new URL("file:/src/main/schema.json");

        final Map<URL, Catalog> catalogPojoMap = ImmutableMap.of(url, catalog);

        when(classpathCatalogLoader.getCatalogs()).thenReturn(catalogPojoMap);
        when(catalog.getGroup()).thenReturn(singletonList(group));
        when(group.getSchema()).thenReturn(singletonList(schema));

        when(schema.getLocation()).thenReturn("some/path/to.json");
        when(schema.getId()).thenReturn("schemaId");

        final Map<String, URL> schemaLocations = catalogToSchemaResolver.resolveSchemaLocations();

        assertThat(schemaLocations.size(), is(1));

        assertThat(schemaLocations.containsKey("schemaId"), is(true));
        assertThat(schemaLocations.get("schemaId").toString(), is("file:/src/main/some/path/to.json"));
    }
}
