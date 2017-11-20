package uk.gov.justice.schema.catalog.generation;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.domain.Group;
import uk.gov.justice.schema.catalog.domain.Schema;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class CatalogWriterTest {

    private static final File GENERATED_CATALOG_FILE = new File(new File("target/generated-catalogs", "json/schema/catalog"), "schema_catalog.json");

    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    private CatalogWriter catalogWriter = new CatalogWriter(objectMapper, new CatalogGenerationContext());

    @Before
    public void cleanUpAnyPreviouslyGeneratedCatalog() {
        deleteQuietly(GENERATED_CATALOG_FILE);
    }

    @Test
    public void shouldGenerateACatalogInTheCorrectLocationFromTheCatalogDomainObjects() throws Exception {

        assertThat(GENERATED_CATALOG_FILE.exists(), is(false));

        final Catalog catalog = new Catalog(
                "my catalog",
                asList(new Group("/json/schema/standards", "standards", asList(
                        new Schema(
                                "http://justice.gov.uk/standards/complex_address.json",
                                "complex_address.json"),
                        new Schema(
                                "http://justice.gov.uk/standards/address.json",
                                "address.json")
                        )),
                        new Group(null, "staging interface", singletonList(
                                new Schema(
                                        "http://justice.gov.uk/context/person.json",
                                        "/raml/json/schema/context/person.json")
                        ))
                )
        );

        catalogWriter.write(catalog);

        assertThat(GENERATED_CATALOG_FILE.exists(), is(true));

        final String catalogJson = readFileToString(GENERATED_CATALOG_FILE);

        with(catalogJson)
                .assertThat("$.name", is(catalog.getName()))
                .assertThat("$.group[0].name", is(catalog.getGroup().get(0).getName()))
                .assertThat("$.group[0].baseLocation", is(catalog.getGroup().get(0).getBaseLocation()))
                .assertThat("$.group[0].schema[0].id", is(catalog.getGroup().get(0).getSchema().get(0).getId()))
                .assertThat("$.group[0].schema[0].location", is(catalog.getGroup().get(0).getSchema().get(0).getLocation()))
                .assertThat("$.group[0].schema[1].id", is(catalog.getGroup().get(0).getSchema().get(1).getId()))
                .assertThat("$.group[0].schema[1].location", is(catalog.getGroup().get(0).getSchema().get(1).getLocation()))
                .assertThat("$.group[1].name", is(catalog.getGroup().get(1).getName()))
                .assertNotDefined("$.group[1].baseLocation")
                .assertThat("$.group[1].schema[0].id", is(catalog.getGroup().get(1).getSchema().get(0).getId()))
                .assertThat("$.group[1].schema[0].location", is(catalog.getGroup().get(1).getSchema().get(0).getLocation()))
        ;
    }
}
