package uk.gov.justice.schema.catalog.generation;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.CatalogLoader;
import uk.gov.justice.schema.catalog.CatalogToSchemaResolver;
import uk.gov.justice.schema.catalog.ClasspathCatalogLoader;
import uk.gov.justice.schema.catalog.FileContentsAsStringLoader;
import uk.gov.justice.schema.catalog.JsonSchemaFileLoader;
import uk.gov.justice.schema.catalog.JsonStringToSchemaConverter;
import uk.gov.justice.schema.catalog.SchemaResolver;
import uk.gov.justice.schema.catalog.SchemaResolverAndLoader;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;
import uk.gov.justice.schema.client.SchemaClientFactory;

import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectFactoryTest {

    @InjectMocks
    private ObjectFactory objectFactory;

    @Test
    public void shouldCreateACatalogGenerationContext() throws Exception {
        assertThat(objectFactory.catalogGenerationConstants(), is(instanceOf(CatalogGenerationContext.class)));
    }

    @Test
    public void shouldCreateAUrlConverter() throws Exception {
        assertThat(objectFactory.urlConverter(), is(instanceOf(UrlConverter.class)));
    }

    @Test
    public void shouldCreateAnObjectMapper() throws Exception {
        assertThat(objectFactory.objectMapper(), is(instanceOf(ObjectMapper.class)));
    }

    @Test
    public void shouldCreateACatalogWriter() throws Exception {

        final CatalogWriter catalogWriter = objectFactory.catalogWriter();
        assertThat(catalogWriter, is(notNullValue()));

        final ObjectMapper objectMapper = getPrivateField("objectMapper", catalogWriter, ObjectMapper.class);
        assertThat(objectMapper, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaIdParser() throws Exception {

        final SchemaIdParser schemaIdParser = objectFactory.schemaIdParser();
        assertThat(schemaIdParser, is(notNullValue()));

        final UrlConverter urlConverter = getPrivateField("urlConverter", schemaIdParser, UrlConverter.class);
        assertThat(urlConverter, is(notNullValue()));
    }

    @Test
    public void shouldCreateACatalogObjectGenerator() throws Exception {

        final CatalogObjectGenerator catalogObjectGenerator = objectFactory.catalogObjectGenerator();
        assertThat(catalogObjectGenerator, is(notNullValue()));

        final SchemaDefParser schemaDefParser = getPrivateField("schemaDefParser", catalogObjectGenerator, SchemaDefParser.class);
        assertThat(schemaDefParser, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaDefParser() throws Exception {

        final SchemaDefParser schemaDefParser = objectFactory.schemaDefParser();
        assertThat(schemaDefParser, is(notNullValue()));

        final SchemaIdParser schemaIdParser = getPrivateField("schemaIdParser", schemaDefParser, SchemaIdParser.class);
        assertThat(schemaIdParser, is(notNullValue()));
    }

    @Test
    public void shouldCreateACatalogGenerationRunner() throws Exception {

        final CatalogGenerationRunner catalogGenerationRunner = objectFactory.catalogGenerationRunner();
        assertThat(catalogGenerationRunner, is(notNullValue()));

        final CatalogObjectGenerator catalogObjectGenerator = getPrivateField("catalogObjectGenerator", catalogGenerationRunner, CatalogObjectGenerator.class);
        assertThat(catalogObjectGenerator, is(notNullValue()));

        final CatalogWriter catalogWriter = getPrivateField("catalogWriter", catalogGenerationRunner, CatalogWriter.class);
        assertThat(catalogWriter, is(notNullValue()));

        final UrlConverter urlConverter = getPrivateField("urlConverter", catalogGenerationRunner, UrlConverter.class);
        assertThat(urlConverter, is(notNullValue()));
    }

    @Test
    public void shouldCreateAUriResolver() throws Exception {

        final UriResolver uriResolver = objectFactory.uriResolver();
        assertThat(uriResolver, is(notNullValue()));
    }

    @Test
    public void shouldCreateAJsonStringToSchemaConverter() throws Exception {

        final JsonStringToSchemaConverter jsonStringToSchemaConverter = objectFactory.jsonStringToSchemaConverter();
        assertThat(jsonStringToSchemaConverter, is(notNullValue()));
    }

    @Test
    public void shouldCreateAClasspathResourceLoader() throws Exception {

        final ClasspathResourceLoader classpathResourceLoader = objectFactory.classpathResourceLoader();
        assertThat(classpathResourceLoader, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaResolverAndLoader() throws Exception {

        final SchemaResolverAndLoader schemaResolverAndLoader = objectFactory.schemaResolverAndLoader();
        assertThat(schemaResolverAndLoader, is(notNullValue()));

        final JsonStringToSchemaConverter jsonStringToSchemaConverter = getPrivateField("jsonStringToSchemaConverter", schemaResolverAndLoader, JsonStringToSchemaConverter.class);
        assertThat(jsonStringToSchemaConverter, is(notNullValue()));
    }

    @Test
    public void shouldCreateAClasspathCatalogLoader() throws Exception {

        final ClasspathCatalogLoader classpathCatalogLoader = objectFactory.classpathCatalogLoader();
        assertThat(classpathCatalogLoader, is(notNullValue()));

        final ObjectMapper objectMapper = getPrivateField("objectMapper", classpathCatalogLoader, ObjectMapper.class);
        assertThat(objectMapper, is(notNullValue()));

        final ClasspathResourceLoader classpathResourceLoader = getPrivateField("classpathResourceLoader", classpathCatalogLoader, ClasspathResourceLoader.class);
        assertThat(classpathResourceLoader, is(notNullValue()));

        final UrlConverter urlConverter = getPrivateField("urlConverter", classpathCatalogLoader, UrlConverter.class);
        assertThat(urlConverter, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaResolver() throws Exception {

        final SchemaResolver schemaResolver = objectFactory.schemaResolver();
        assertThat(schemaResolver, is(notNullValue()));

        final UrlConverter urlConverter = getPrivateField("urlConverter", schemaResolver, UrlConverter.class);
        assertThat(urlConverter, is(notNullValue()));

        final UriResolver uriResolver = getPrivateField("uriResolver", schemaResolver, UriResolver.class);
        assertThat(uriResolver, is(notNullValue()));
    }

    @Test
    public void shouldCreateCatalogToSchemaResolver() throws Exception {

        final CatalogToSchemaResolver catalogToSchemaResolver = objectFactory.catalogToSchemaResolver();
        assertThat(catalogToSchemaResolver, is(notNullValue()));

        final ClasspathCatalogLoader classpathCatalogLoader = getPrivateField("classpathCatalogLoader", catalogToSchemaResolver, ClasspathCatalogLoader.class);
        assertThat(classpathCatalogLoader, is(notNullValue()));

        final SchemaResolver schemaResolver = getPrivateField("schemaResolver", catalogToSchemaResolver, SchemaResolver.class);
        assertThat(schemaResolver, is(notNullValue()));
    }

    @Test
    public void shouldCreateAFileContentsAsStringLoader() throws Exception {

        final FileContentsAsStringLoader fileContentsAsStringLoader = objectFactory.fileContentsAsStringLoader();
        assertThat(fileContentsAsStringLoader, is(notNullValue()));
    }

    @Test
    public void shouldCreateAJsonSchemaFileLoader() throws Exception {

        final JsonSchemaFileLoader jsonSchemaFileLoader = objectFactory.jsonSchemaFileLoader();
        assertThat(jsonSchemaFileLoader, is(notNullValue()));

        final FileContentsAsStringLoader fileContentsAsStringLoader = getPrivateField("fileContentsAsStringLoader", jsonSchemaFileLoader, FileContentsAsStringLoader.class);
        assertThat(fileContentsAsStringLoader, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaClientFactory() throws Exception {

        final SchemaClientFactory schemaClientFactory = objectFactory.schemaClientFactory();
        assertThat(schemaClientFactory, is(notNullValue()));
    }

    @Test
    public void shouldCreateACatalogLoader() throws Exception {

        final CatalogLoader catalogLoader = objectFactory.catalogLoader();
        assertThat(catalogLoader, is(notNullValue()));

        final SchemaResolverAndLoader schemaResolverAndLoader = getPrivateField("schemaResolverAndLoader", catalogLoader, SchemaResolverAndLoader.class);
        assertThat(schemaResolverAndLoader, is(notNullValue()));

        final CatalogToSchemaResolver catalogToSchemaResolver = getPrivateField("catalogToSchemaResolver", catalogLoader, CatalogToSchemaResolver.class);
        assertThat(catalogToSchemaResolver, is(notNullValue()));

        final JsonSchemaFileLoader jsonSchemaFileLoader = getPrivateField("jsonSchemaFileLoader", catalogLoader, JsonSchemaFileLoader.class);
        assertThat(jsonSchemaFileLoader, is(notNullValue()));

        final SchemaClientFactory schemaClientFactory = getPrivateField("schemaClientFactory", catalogLoader, SchemaClientFactory.class);
        assertThat(schemaClientFactory, is(notNullValue()));
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(final String fieldName, final Object fromThisObject, @SuppressWarnings("unused") final Class<T> clazz) throws Exception {

        final Field field = fromThisObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        return (T) field.get(fromThisObject);
    }
}
