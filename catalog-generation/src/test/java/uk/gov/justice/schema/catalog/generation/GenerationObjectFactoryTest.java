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
public class GenerationObjectFactoryTest {


    @InjectMocks
    private GenerationObjectFactory generationObjectFactory;

    @Test
    public void shouldCreateACatalogGenerationContext() throws Exception {
        assertThat(generationObjectFactory.catalogGenerationContext(), is(instanceOf(CatalogGenerationContext.class)));
    }

    @Test
    public void shouldCreateACatalogWriter() throws Exception {
        final CatalogWriter catalogWriter = generationObjectFactory.catalogWriter();
        assertThat(catalogWriter, is(instanceOf(CatalogWriter.class)));

        assertThat(getPrivateField("objectMapper", catalogWriter, ObjectMapper.class), is(notNullValue()));
        assertThat(getPrivateField("catalogGenerationContext", catalogWriter, CatalogGenerationContext.class), is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaIdParser() throws Exception {
        final SchemaIdParser schemaIdParser = generationObjectFactory.schemaIdParser();
        assertThat(schemaIdParser, is(instanceOf(SchemaIdParser.class)));

        assertThat(getPrivateField("urlConverter", schemaIdParser, UrlConverter.class), is(notNullValue()));
    }

    @Test
    public void shouldCreateACatalogObjectGenerator() throws Exception {
        final CatalogObjectGenerator catalogObjectGenerator = generationObjectFactory.catalogObjectGenerator();
        assertThat(catalogObjectGenerator, is(instanceOf(CatalogObjectGenerator.class)));

        assertThat(getPrivateField("schemaDefParser", catalogObjectGenerator, SchemaDefParser.class), is(notNullValue()));
    }

    @Test
    public void shouldCreateASchemaDefParser() throws Exception {
        final SchemaDefParser schemaDefParser = generationObjectFactory.schemaDefParser();
        assertThat(schemaDefParser, is(instanceOf(SchemaDefParser.class)));

        assertThat(getPrivateField("schemaIdParser", schemaDefParser, SchemaIdParser.class), is(notNullValue()));
    }

    @Test
    public void shouldCreateACatalogGenerationRunner() throws Exception {
        final CatalogGenerationRunner catalogGenerationRunner = generationObjectFactory.catalogGenerationRunner();
        assertThat(catalogGenerationRunner, is(instanceOf(CatalogGenerationRunner.class)));

        assertThat(getPrivateField("catalogObjectGenerator", catalogGenerationRunner, CatalogObjectGenerator.class), is(notNullValue()));
        assertThat(getPrivateField("catalogWriter", catalogGenerationRunner, CatalogWriter.class), is(notNullValue()));
        assertThat(getPrivateField("urlConverter", catalogGenerationRunner, UrlConverter.class), is(notNullValue()));
    }





    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(final String fieldName, final Object fromThisObject, @SuppressWarnings("unused") final Class<T> clazz) throws Exception {

        final Field field = fromThisObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        return (T) field.get(fromThisObject);
    }
}
