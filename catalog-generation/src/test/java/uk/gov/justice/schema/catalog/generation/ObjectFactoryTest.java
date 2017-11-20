package uk.gov.justice.schema.catalog.generation;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.util.UrlConverter;

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

        final Object objectMapper = getPrivateField("objectMapper", catalogWriter);
        assertThat(objectMapper, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaIdParser() throws Exception {

        final SchemaIdParser schemaIdParser = objectFactory.schemaIdParser();
        assertThat(schemaIdParser, is(notNullValue()));

        final Object urlConverter = getPrivateField("urlConverter", schemaIdParser);
        assertThat(urlConverter, is(notNullValue()));
    }

    @Test
    public void shouldCreateACatalogObjectGenerator() throws Exception {

        final CatalogObjectGenerator catalogObjectGenerator = objectFactory.catalogObjectGenerator();
        assertThat(catalogObjectGenerator, is(notNullValue()));

        final Object schemaDefParser = getPrivateField("schemaDefParser", catalogObjectGenerator);
        assertThat(schemaDefParser, is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaDefParser() throws Exception {

        final SchemaDefParser schemaDefParser = objectFactory.schemaDefParser();
        assertThat(schemaDefParser, is(notNullValue()));

        final Object schemaIdParser = getPrivateField("schemaIdParser", schemaDefParser);
        assertThat(schemaIdParser, is(notNullValue()));
    }

    @Test
    public void shouldCreateACatalogGenerationRunner() throws Exception {

        final CatalogGenerationRunner catalogGenerationRunner = objectFactory.catalogGenerationRunner();
        assertThat(catalogGenerationRunner, is(notNullValue()));

        final Object catalogObjectGenerator = getPrivateField("catalogObjectGenerator", catalogGenerationRunner);
        assertThat(catalogObjectGenerator, is(notNullValue()));

        final Object catalogWriter = getPrivateField("catalogWriter", catalogGenerationRunner);
        assertThat(catalogWriter, is(notNullValue()));

        final Object urlConverter = getPrivateField("urlConverter", catalogGenerationRunner);
        assertThat(urlConverter, is(notNullValue()));
    }

    private Object getPrivateField(final String fieldName, final Object fromThisObject) throws Exception {

        final Field field = fromThisObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        return field.get(fromThisObject);
    }
}
