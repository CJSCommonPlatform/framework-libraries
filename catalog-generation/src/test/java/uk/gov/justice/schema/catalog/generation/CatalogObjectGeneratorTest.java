package uk.gov.justice.schema.catalog.generation;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URL;
import java.util.List;

import org.junit.Test;

public class CatalogObjectGeneratorTest {

    private final UrlConverter urlConverter = new UrlConverter();
    private final SchemaIdParser schemaIdParser = new SchemaIdParser(urlConverter);
    private final SchemaDefParser schemaDefParser = new SchemaDefParser(schemaIdParser, new CatalogGenerationContext());

    private final CatalogObjectGenerator catalogObjectGenerator = new CatalogObjectGenerator(schemaDefParser);

    @Test
    public void shouldParseAListOfSchemaFilesIntoACatalogObject() throws Exception {

        final String catalogName = "my catalog";

        final ClassLoader classLoader = getClass().getClassLoader();
        final URL personSchemaFile = classLoader.getResource("raml/json/schema/context/person.json");
        final URL addressSchemaFile = classLoader.getResource("raml/json/schema/standards/address.json");
        final URL complexAddressSchemaFile = classLoader.getResource("raml/json/schema/standards/complex_address.json");
        final URL defendantSchemaFile = classLoader.getResource("raml/json/schema/standards/defendant.json");

        final List<URL> schemaFiles = asList(
                personSchemaFile,
                addressSchemaFile,
                complexAddressSchemaFile,
                defendantSchemaFile);


        final Catalog catalog = catalogObjectGenerator.generate(catalogName, schemaFiles);

        assertThat(catalog.getName(), is(catalogName));
        assertThat(catalog.getGroup().size(), is(2));

        assertThat(catalog.getGroup().get(0).getName(), is("standards"));
        assertThat(catalog.getGroup().get(0).getBaseLocation(), is("standards/"));
        assertThat(catalog.getGroup().get(0).getSchema().size(), is(3));
        assertThat(catalog.getGroup().get(0).getSchema().get(0).getId(), is("http://justice.gov.uk/standards/address.json"));
        assertThat(catalog.getGroup().get(0).getSchema().get(0).getLocation(), is("address.json"));
        assertThat(catalog.getGroup().get(0).getSchema().get(1).getId(), is("http://justice.gov.uk/standards/complex_address.json"));
        assertThat(catalog.getGroup().get(0).getSchema().get(1).getLocation(), is("complex_address.json"));
        assertThat(catalog.getGroup().get(0).getSchema().get(2).getId(), is("http://justice.gov.uk/standards/defendant.json"));
        assertThat(catalog.getGroup().get(0).getSchema().get(2).getLocation(), is("defendant.json"));

        assertThat(catalog.getGroup().get(1).getName(), is("context"));
        assertThat(catalog.getGroup().get(1).getBaseLocation(), is("context/"));
        assertThat(catalog.getGroup().get(1).getSchema().size(), is(1));
        assertThat(catalog.getGroup().get(1).getSchema().get(0).getId(), is("http://justice.gov.uk/context/person.json"));
        assertThat(catalog.getGroup().get(1).getSchema().get(0).getLocation(), is("person.json"));
    }
}
