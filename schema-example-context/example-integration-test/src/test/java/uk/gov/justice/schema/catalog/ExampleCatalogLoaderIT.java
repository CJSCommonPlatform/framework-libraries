package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;
import uk.gov.justice.schema.client.SchemaClientFactory;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.junit.Test;

public class ExampleCatalogLoaderIT {

    private final FileContentsAsStringLoader fileContentsAsStringLoader = new FileContentsAsStringLoader();
    private final JsonStringToSchemaConverter jsonStringToSchemaConverter = new JsonStringToSchemaConverter();
    private final SchemaResolverAndLoader schemaResolverAndLoader = new SchemaResolverAndLoader(jsonStringToSchemaConverter);
    private final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
    private final UrlConverter urlConverter = new UrlConverter();
    private final UriResolver uriResolver = new UriResolver();
    private final ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();
    private final ClasspathCatalogLoader classpathCatalogLoader = new ClasspathCatalogLoader(objectMapper, classpathResourceLoader, urlConverter);
    private final SchemaResolver schemaResolver = new SchemaResolver(urlConverter, uriResolver);
    private final CatalogToSchemaResolver catalogToSchemaResolver = new CatalogToSchemaResolver(classpathCatalogLoader, schemaResolver);
    private final JsonSchemaLoader jsonSchemaLoader = new JsonSchemaLoader(fileContentsAsStringLoader);
    private final SchemaClientFactory schemaClientFactory = new SchemaClientFactory();

    private final CatalogLoader catalogLoader = new CatalogLoader(
            schemaResolverAndLoader,
            catalogToSchemaResolver,
            jsonSchemaLoader,
            schemaClientFactory);

    @Test
    public void shouldMapSchemasOnClasspathToTheirIds2() throws Exception {

        final Map<String, Schema> idsToSchemaMap = catalogLoader.loadCatalogsFromClasspath();

        assertThat(idsToSchemaMap.size(), is(2));

        final String id_1 = "http://justice.gov.uk/example/standard/ingredient.json";
        final String json_1 = idsToSchemaMap.get(id_1).toString();

        final String id_2 = "http://justice.gov.uk/example/cakeshop/example.add-recipe.json";
        final String json_2 = idsToSchemaMap.get(id_2).toString();
    }
}
