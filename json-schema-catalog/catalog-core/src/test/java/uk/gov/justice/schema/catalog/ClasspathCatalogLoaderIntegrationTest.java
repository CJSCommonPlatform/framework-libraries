package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.domain.Catalog;

import java.net.URI;
import java.util.Map;

import org.junit.Test;

public class ClasspathCatalogLoaderIntegrationTest {

    private final ClasspathCatalogLoader classpathCatalogLoader = new CatalogObjectFactory().classpathCatalogLoader();

    @Test
    public void shouldLoadCatalogsFromTheClasspath() throws Exception {

        final Map<URI, Catalog> catalogs = classpathCatalogLoader.getCatalogs();

        assertThat(catalogs.size(), is(1));

        final URI uri = catalogs.keySet().iterator().next();

        assertThat(uri.toString(), startsWith("file:/"));
        assertThat(uri.toString(), endsWith("/json-schema-catalog/catalog-core/target/test-classes/META-INF/schema_catalog.json"));

        final Catalog catalog = catalogs.get(uri);

        assertThat(catalog.getName(), is("my catalog"));
        assertThat(catalog.getGroups().size(), is(2));

        assertThat(catalog.getGroups().get(0).getName(), is("standards"));
        assertThat(catalog.getGroups().get(0).getBaseLocation(), is("json/schema/standards/"));

        assertThat(catalog.getGroups().get(0).getSchemas().size(), is(2));
        assertThat(catalog.getGroups().get(0).getSchemas().get(0).getId(), is("http://justice.gov.uk/standards/complex_address.json"));
        assertThat(catalog.getGroups().get(0).getSchemas().get(0).getLocation(), is("complex_address.json"));
        assertThat(catalog.getGroups().get(0).getSchemas().get(1).getId(), is("http://justice.gov.uk/standards/address.json"));
        assertThat(catalog.getGroups().get(0).getSchemas().get(1).getLocation(), is("address.json"));

        assertThat(catalog.getGroups().get(1).getName(), is("staging interface"));
        assertThat(catalog.getGroups().get(1).getBaseLocation(), is("json/schema/context/"));

        assertThat(catalog.getGroups().get(1).getSchemas().size(), is(1));
        assertThat(catalog.getGroups().get(1).getSchemas().get(0).getId(), is("http://justice.gov.uk/context/person.json"));
        assertThat(catalog.getGroups().get(1).getSchemas().get(0).getLocation(), is("person.json"));
    }
}
