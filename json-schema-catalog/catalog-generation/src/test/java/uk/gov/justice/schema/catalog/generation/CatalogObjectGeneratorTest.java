package uk.gov.justice.schema.catalog.generation;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.domain.Catalog;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

public class CatalogObjectGeneratorTest {

    private final CatalogObjectGenerator catalogObjectGenerator = new GenerationObjectFactory().catalogObjectGenerator();

    @Test
    public void shouldParseAListOfSchemaFilesIntoACatalogObject() throws Exception {

        final String catalogName = "my catalog";
        final Path jsonSchemaPath = Paths.get("raml/json/schema/");

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

        final Catalog catalog = catalogObjectGenerator.generate(catalogName, schemaFiles, jsonSchemaPath);

        assertThat(catalog.getName(), is(catalogName));
        assertThat(catalog.getGroups().size(), is(2));

        assertThat(catalog.getGroups().get(0).getName(), is("standards"));
        assertThat(catalog.getGroups().get(0).getBaseLocation(), is("raml/json/schema/standards/"));
        assertThat(catalog.getGroups().get(0).getSchemas().size(), is(3));
        assertThat(catalog.getGroups().get(0).getSchemas().get(0).getId(), is("http://justice.gov.uk/standards/address.json"));
        assertThat(catalog.getGroups().get(0).getSchemas().get(0).getLocation(), is("address.json"));
        assertThat(catalog.getGroups().get(0).getSchemas().get(1).getId(), is("http://justice.gov.uk/standards/complex_address.json"));
        assertThat(catalog.getGroups().get(0).getSchemas().get(1).getLocation(), is("complex_address.json"));
        assertThat(catalog.getGroups().get(0).getSchemas().get(2).getId(), is("http://justice.gov.uk/standards/defendant.json"));
        assertThat(catalog.getGroups().get(0).getSchemas().get(2).getLocation(), is("defendant.json"));

        assertThat(catalog.getGroups().get(1).getName(), is("context"));
        assertThat(catalog.getGroups().get(1).getBaseLocation(), is("raml/json/schema/context/"));
        assertThat(catalog.getGroups().get(1).getSchemas().size(), is(1));
        assertThat(catalog.getGroups().get(1).getSchemas().get(0).getId(), is("http://justice.gov.uk/context/person.json"));
        assertThat(catalog.getGroups().get(1).getSchemas().get(0).getLocation(), is("person.json"));
    }
}
