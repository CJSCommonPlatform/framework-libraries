package uk.gov.justice.schema.catalog.generation;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.schema.catalog.CatalogContext;
import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.domain.Group;
import uk.gov.justice.schema.catalog.domain.Schema;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class CatalogWriterTest {

    private final CatalogContext catalogContext = new CatalogContext();
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final CatalogWriter catalogWriter = new CatalogWriter(objectMapper, catalogContext, LoggerFactory.getLogger(CatalogWriter.class));

    private final File catalogGenerationPath = new File("target/test-output-directory");
    private final File generatedCatalogFile = new File(catalogGenerationPath + "/" + catalogContext.getCatalogLocation(), catalogContext.getCatalogFilename());

    @BeforeEach
    public void cleanUpAnyPreviouslyGeneratedCatalog() {
        deleteQuietly(generatedCatalogFile);
    }

    @Test
    public void shouldGenerateACatalogInTheCorrectLocationFromTheCatalogDomainObjects() throws Exception {

        assertThat(generatedCatalogFile.exists(), is(false));

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

        catalogWriter.write(catalog, catalogGenerationPath.toPath());

        assertThat(generatedCatalogFile.exists(), is(true));

        final String catalogJson = readFileToString(generatedCatalogFile);

        with(catalogJson)
                .assertThat("$.name", is(catalog.getName()))
                .assertThat("$.groups[0].name", is(catalog.getGroups().get(0).getName()))
                .assertThat("$.groups[0].baseLocation", is(catalog.getGroups().get(0).getBaseLocation()))
                .assertThat("$.groups[0].schemas[0].id", is(catalog.getGroups().get(0).getSchemas().get(0).getId()))
                .assertThat("$.groups[0].schemas[0].location", is(catalog.getGroups().get(0).getSchemas().get(0).getLocation()))
                .assertThat("$.groups[0].schemas[1].id", is(catalog.getGroups().get(0).getSchemas().get(1).getId()))
                .assertThat("$.groups[0].schemas[1].location", is(catalog.getGroups().get(0).getSchemas().get(1).getLocation()))
                .assertThat("$.groups[1].name", is(catalog.getGroups().get(1).getName()))
                .assertNotDefined("$.group[1].baseLocation")
                .assertThat("$.groups[1].schemas[0].id", is(catalog.getGroups().get(1).getSchemas().get(0).getId()))
                .assertThat("$.groups[1].schemas[0].location", is(catalog.getGroups().get(1).getSchemas().get(0).getLocation()))
        ;
    }
}
