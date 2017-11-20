package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UrlConverter;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.net.URI;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClasspathCatalogLoaderIntegrationTest {

    @Spy
    @SuppressWarnings("unused")
    private ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    @Spy
    @SuppressWarnings("unused")
    private ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();

    @Spy
    @SuppressWarnings("unused")
    private UrlConverter urlConverter = new UrlConverter();

    @InjectMocks
    private ClasspathCatalogLoader classpathCatalogLoader;

    @Test
    public void shouldLoadCatalogsFromTheClasspath() throws Exception {

        final Map<URI, Catalog> catalogs = classpathCatalogLoader.getCatalogs();

        assertThat(catalogs.size(), is(1));

        final URI uri = catalogs.keySet().iterator().next();

        assertThat(uri.toString(), startsWith("file:/"));
        assertThat(uri.toString(), endsWith("/json-schema-catalog/catalog-core/target/test-classes/json/schema/schema_catalog.json"));

        final Catalog catalog = catalogs.get(uri);

        assertThat(catalog.getName(), is("my catalog"));
        assertThat(catalog.getGroup().size(), is(2));

        assertThat(catalog.getGroup().get(0).getName(), is("standards"));
        assertThat(catalog.getGroup().get(0).getBaseLocation(), is("standards/"));

        assertThat(catalog.getGroup().get(0).getSchema().size(), is(2));
        assertThat(catalog.getGroup().get(0).getSchema().get(0).getId(), is("http://justice.gov.uk/standards/complex_address.json"));
        assertThat(catalog.getGroup().get(0).getSchema().get(0).getLocation(), is("complex_address.json"));
        assertThat(catalog.getGroup().get(0).getSchema().get(1).getId(), is("http://justice.gov.uk/standards/address.json"));
        assertThat(catalog.getGroup().get(0).getSchema().get(1).getLocation(), is("address.json"));

        assertThat(catalog.getGroup().get(1).getName(), is("staging interface"));
        assertThat(catalog.getGroup().get(1).getBaseLocation(), is(CoreMatchers.nullValue()));

        assertThat(catalog.getGroup().get(1).getSchema().size(), is(1));
        assertThat(catalog.getGroup().get(1).getSchema().get(0).getId(), is("http://justice.gov.uk/context/person.json"));
        assertThat(catalog.getGroup().get(1).getSchema().get(0).getLocation(), is("context/person.json"));
    }
}
