package uk.gov.justice.schema.catalog;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UrlConverter;
import uk.gov.justice.schema.client.SchemaClientFactory;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.junit.Test;

public class CatalogLoaderIT {

    private final FileContentsAsStringLoader fileContentsAsStringLoader = new FileContentsAsStringLoader();
    private final JsonStringToSchemaConverter jsonStringToSchemaConverter = new JsonStringToSchemaConverter();
    private final SchemaResolverAndLoader schemaResolverAndLoader = new SchemaResolverAndLoader(jsonStringToSchemaConverter);
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final UrlConverter urlConverter = new UrlConverter();
    private final ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();
    private final ClasspathCatalogLoader classpathCatalogLoader = new ClasspathCatalogLoader(objectMapper, classpathResourceLoader, urlConverter);
    private final SchemaResolver schemaResolver = new SchemaResolver(urlConverter);
    private final CatalogToSchemaResolver catalogToSchemaResolver = new CatalogToSchemaResolver(classpathCatalogLoader, schemaResolver);
    private final JsonSchemaLoader jsonSchemaLoader = new JsonSchemaLoader(fileContentsAsStringLoader);
    private final SchemaClientFactory schemaClientFactory = new SchemaClientFactory();

    private final CatalogLoader catalogLoader = new CatalogLoader(
            schemaResolverAndLoader,
            catalogToSchemaResolver,
            jsonSchemaLoader,
            schemaClientFactory);

    @Test
    public void shouldMapSchemasOnClasspathToTheirIds() throws Exception {

        final Map<String, Schema> idsToSchemaMap = catalogLoader.loadCatalogsFromClasspath();

        assertThat(idsToSchemaMap.size(), is(3));

        final String id_1 = "http://justice.gov.uk/standards/address.json";
        final String json_1 = idsToSchemaMap.get(id_1).toString();

        with(json_1)
                .assertThat("$.id", is(id_1))
                .assertThat("$.type", is("object"))
                .assertThat("$.properties.city.type", is("string"))
                .assertThat("$.properties.postcode.type", is("string"))
                .assertThat("$.properties.addressline1.type", is("string"))
                .assertThat("$.properties.addressline2.type", is("string"))
        ;

        final String id_2 = "http://justice.gov.uk/context/person.json";
        final String json_2 = idsToSchemaMap.get(id_2).toString();

        with(json_2)
                .assertThat("$.id", is(id_2))
                .assertThat("$.type", is("object"))
                .assertThat("$.properties.correspondence_address.$ref", is("http://justice.gov.uk/standards/complex_address.json#/definitions/complex_address2"))
                .assertThat("$.properties.name.type", is("string"))
                .assertThat("$.properties.nino.type", is("string"))
        ;

        final String id_3 = "http://justice.gov.uk/standards/complex_address.json";
        final String json_3 = idsToSchemaMap.get(id_3).toString();

        with(json_3)
                .assertThat("$.id", is(id_3))
                .assertThat("$.allOf[0].type", is("object"))
                .assertThat("$.allOf[1].allOf[0].$ref", is("#/definitions/complex_address"))
        ;
    }
}
