package uk.gov.justice.schema.catalog;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.schema.catalog.client.SchemaClientFactory;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;
import uk.gov.justice.schema.catalog.util.UriResolver;
import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.lang.reflect.Field;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogObjectFactoryTest {

    @InjectMocks
    private CatalogObjectFactory catalogObjectFactory;

    @Test
    public void shouldCreateAUrlConverter() throws Exception {
        assertThat(catalogObjectFactory.urlConverter(), is(instanceOf(UrlConverter.class)));
    }

    @Test
    public void shouldCreateAnObjectMapper() throws Exception {
        assertThat(catalogObjectFactory.objectMapper(), is(instanceOf(ObjectMapper.class)));
    }

    @Test
    public void shouldCreateAUriResolver() throws Exception {

        final UriResolver uriResolver = catalogObjectFactory.uriResolver();
        assertThat(uriResolver, is(notNullValue()));
    }

    @Test
    public void shouldCreateAJsonStringToSchemaConverter() throws Exception {

        final JsonToSchemaConverter jsonToSchemaConverter = catalogObjectFactory.jsonToSchemaConverter();
        assertThat(jsonToSchemaConverter, is(notNullValue()));
    }

    @Test
    public void shouldCreateAClasspathResourceLoader() throws Exception {

        final ClasspathResourceLoader classpathResourceLoader = catalogObjectFactory.classpathResourceLoader();
        assertThat(classpathResourceLoader, is(notNullValue()));
    }

    @Test
    public void shouldCreateACatalogGenerationContext() throws Exception {
        assertThat(catalogObjectFactory.catalogContext(), is(instanceOf(CatalogContext.class)));
    }

    @Test
    public void shouldCreateAClasspathCatalogLoader() throws Exception {

        final ClasspathCatalogLoader classpathCatalogLoader = catalogObjectFactory.classpathCatalogLoader();
        assertThat(classpathCatalogLoader, is(notNullValue()));

        final ObjectMapper objectMapper = getPrivateField("objectMapper", classpathCatalogLoader, ObjectMapper.class);
        assertThat(objectMapper, is(notNullValue()));

        final ClasspathResourceLoader classpathResourceLoader = getPrivateField("classpathResourceLoader", classpathCatalogLoader, ClasspathResourceLoader.class);
        assertThat(classpathResourceLoader, is(notNullValue()));

        final CatalogContext catalogContext = getPrivateField("catalogContext", classpathCatalogLoader, CatalogContext.class);
        assertThat(catalogContext, is(notNullValue()));

        final UrlConverter urlConverter = getPrivateField("urlConverter", classpathCatalogLoader, UrlConverter.class);
        assertThat(urlConverter, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaResolver() throws Exception {

        final SchemaResolver schemaResolver = catalogObjectFactory.schemaResolver();
        assertThat(schemaResolver, is(notNullValue()));

        final UrlConverter urlConverter = getPrivateField("urlConverter", schemaResolver, UrlConverter.class);
        assertThat(urlConverter, is(notNullValue()));

        final UriResolver uriResolver = getPrivateField("uriResolver", schemaResolver, UriResolver.class);
        assertThat(uriResolver, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaCatalogResolver() throws Exception {

        final SchemaCatalogResolver schemaCatalogResolver = catalogObjectFactory.schemaCatalogResolver();
        assertThat(schemaCatalogResolver, is(notNullValue()));

        final RawCatalog rawCatalog = getPrivateField("rawCatalog", schemaCatalogResolver, RawCatalog.class);
        assertThat(rawCatalog, is(notNullValue()));

        final SchemaClientFactory schemaClientFactory = getPrivateField("schemaClientFactory", schemaCatalogResolver, SchemaClientFactory.class);
        assertThat(schemaClientFactory, is(notNullValue()));

        final JsonToSchemaConverter jsonToSchemaConverter = getPrivateField("jsonStringToSchemaConverter", schemaCatalogResolver, JsonToSchemaConverter.class);
        assertThat(jsonToSchemaConverter, is(notNullValue()));
    }

    @Test
    public void shouldCreateCatalogToSchemaResolver() throws Exception {

        final CatalogToSchemaResolver catalogToSchemaResolver = catalogObjectFactory.catalogToSchemaResolver();
        assertThat(catalogToSchemaResolver, is(notNullValue()));

        final ClasspathCatalogLoader classpathCatalogLoader = getPrivateField("classpathCatalogLoader", catalogToSchemaResolver, ClasspathCatalogLoader.class);
        assertThat(classpathCatalogLoader, is(notNullValue()));

        final SchemaResolver schemaResolver = getPrivateField("schemaResolver", catalogToSchemaResolver, SchemaResolver.class);
        assertThat(schemaResolver, is(notNullValue()));
    }

    @Test
    public void shouldCreateAFileContentsAsStringLoader() throws Exception {

        final FileContentsAsStringLoader fileContentsAsStringLoader = catalogObjectFactory.fileContentsAsStringLoader();
        assertThat(fileContentsAsStringLoader, is(notNullValue()));
    }

    @Test
    public void shouldCreateAJsonSchemaFileLoader() throws Exception {

        final JsonSchemaFileLoader jsonSchemaFileLoader = catalogObjectFactory.jsonSchemaFileLoader();
        assertThat(jsonSchemaFileLoader, is(notNullValue()));

        final FileContentsAsStringLoader fileContentsAsStringLoader = getPrivateField("fileContentsAsStringLoader", jsonSchemaFileLoader, FileContentsAsStringLoader.class);
        assertThat(fileContentsAsStringLoader, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaClientFactory() throws Exception {

        final SchemaClientFactory schemaClientFactory = catalogObjectFactory.schemaClientFactory();
        assertThat(schemaClientFactory, is(notNullValue()));
    }

    @Test
    public void shouldCreateRawCatalog() throws Exception {
        final RawCatalog rawCatalog = catalogObjectFactory.rawCatalog();

        assertThat(rawCatalog, is(notNullValue()));

        final JsonSchemaFileLoader jsonSchemaFileLoader = getPrivateField("jsonSchemaFileLoader", rawCatalog, JsonSchemaFileLoader.class);
        assertThat(jsonSchemaFileLoader, is(notNullValue()));

        final Map schemaIdsToRawJsonSchemaCache = getPrivateField("schemaIdsToRawJsonSchemaCache", rawCatalog, Map.class);
        assertThat(schemaIdsToRawJsonSchemaCache.size(), is(3));
    }

    @Test
    public void shouldCreateCatalog() throws Exception {
        final Catalog catalog = catalogObjectFactory.catalog();

        assertThat(catalog, is(notNullValue()));

        final RawCatalog rawCatalog = getPrivateField("rawCatalog", catalog, RawCatalog.class);
        assertThat(rawCatalog, is(notNullValue()));

        final SchemaClientFactory schemaClientFactory = getPrivateField("schemaClientFactory", catalog, SchemaClientFactory.class);
        assertThat(schemaClientFactory, is(notNullValue()));

        final JsonToSchemaConverter jsonToSchemaConverter = getPrivateField("jsonToSchemaConverter", catalog, JsonToSchemaConverter.class);
        assertThat(jsonToSchemaConverter, is(notNullValue()));
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(final String fieldName, final Object fromThisObject, @SuppressWarnings("unused") final Class<T> clazz) throws Exception {

        final Field field = fromThisObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        return (T) field.get(fromThisObject);
    }
}
