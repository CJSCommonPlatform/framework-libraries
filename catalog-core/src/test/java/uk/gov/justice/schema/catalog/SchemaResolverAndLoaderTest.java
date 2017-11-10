package uk.gov.justice.schema.catalog;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.client.LocalFileSystemSchemaClient;

import java.util.Map;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SchemaResolverAndLoaderTest {

    private static final String SCHEMA_1 =
            "{\n" +
                    "  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
                    "  \"id\": \"http://justice.gov.uk/context/person.json\",\n" +
                    "  \"type\": \"object\",\n" +
                    "  \"properties\": {\n" +
                    "    \"name\": {\n" +
                    "      \"type\": \"string\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n";

    private static final String SCHEMA_2 =
            "{\n" +
                    "  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
                    "  \"id\": \"http://justice.gov.uk/context/person.json\",\n" +
                    "  \"type\": \"object\",\n" +
                    "  \"properties\": {\n" +
                    "    \"age\": {\n" +
                    "      \"type\": \"integer\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n";

    @Spy @SuppressWarnings("unused")
    private final JsonStringToSchemaConverter jsonStringToSchemaConverter = new JsonStringToSchemaConverter();

    @InjectMocks
    private SchemaResolverAndLoader schemaResolverAndLoader;

    @Test
    public void shouldLoadSchemasAndMapByTheirIdsIntoTheSchemaDictonary() throws Exception {

        final String id_1 = "id_1";
        final String id_2 = "id_2";
        final Map<String, String> idsToJson = of(id_1, SCHEMA_1, id_2, SCHEMA_2);

        final SchemaClient schemaClient = new LocalFileSystemSchemaClient(idsToJson);

        final Map<String, Schema> idToSchemaMap = schemaResolverAndLoader.loadSchemas(idsToJson, schemaClient);

        assertThat(idToSchemaMap.size(), is(2));

        final Schema schema_1 = idToSchemaMap.get(id_1);
        final Schema schema_2 = idToSchemaMap.get(id_2);

        with(schema_1.toString())
                .assertThat("$.properties.name.type", is("string"))
        ;

        with(schema_2.toString())
                .assertThat("$.properties.age.type", is("integer"))
        ;
    }
}
