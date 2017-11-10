package uk.gov.justice.schema.client;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Map;

import org.everit.json.schema.loader.SchemaClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaClientFactoryTest {

    @InjectMocks
    private SchemaClientFactory schemaClientFactory;

    @Test
    public void shouldCreateANewLocalFileSystemSchemaClient() throws Exception {

        final Map<String, String> urlsToJson = of("id", "{\"some\": \"json\"}");

        final SchemaClient schemaClient = schemaClientFactory.create(urlsToJson);

        assertThat(schemaClient, is(instanceOf(LocalFileSystemSchemaClient.class)));

        final Field idsToJsonField = schemaClient.getClass().getDeclaredField("urlsToJson");
        idsToJsonField.setAccessible(true);

        assertThat(idsToJsonField.get(schemaClient), is(urlsToJson));
    }
}
