package uk.gov.justice.generation.pojo.plugin.namegeneratable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RootFieldNameGeneratorPluginTest {

    @Mock
    private Schema schema;

    @InjectMocks
    private RootFieldNameGeneratorPlugin nameGenerator;

    @Test
    public void shouldParseTheFileNameIntoAValidJavaClassName() throws Exception {
        final String schemaFilename = "object-property-schema.json";

        assertThat(nameGenerator.rootFieldNameFrom(schema, schemaFilename), is("objectPropertySchema"));
    }

    @Test
    public void shouldRemoveAnyPrependingDotsFromTheFileName() throws Exception {
        final String schemaFilename = "context.command.do-something-or-other.json";

        assertThat(nameGenerator.rootFieldNameFrom(schema, schemaFilename), is("doSomethingOrOther"));
    }

    @Test
    public void shouldFailIfTheFileNameDoesNotHaveTheExtensionJson() throws Exception {
        final String schemaFilename = "object-property-schema";

        try {
            nameGenerator.rootFieldNameFrom(schema, schemaFilename);
            fail();
        } catch (final NameGenerationException expected) {
            assertThat(expected.getMessage(), is("Failed to load json schema file '" + schemaFilename + "'. File does not have a '.json' extension"));
        }
    }

    @Test
    public void shouldFailIfTheFileNameIsOnlyTheExtension() throws Exception {
        final String schemaFilename = ".json";

        try {
            nameGenerator.rootFieldNameFrom(schema, schemaFilename);
            fail();
        } catch (final NameGenerationException expected) {
            assertThat(expected.getMessage(), is("Failed to load json schema file '" + schemaFilename + "'. File name is invalid"));
        }
    }

    @Test
    public void shouldFailIfTheFileNameIsOnlyADot() throws Exception {
        final String schemaFilename = "..json";

        try {
            nameGenerator.rootFieldNameFrom(schema, schemaFilename);
            fail();
        } catch (final NameGenerationException expected) {
            assertThat(expected.getMessage(), is("Failed to load json schema file '" + schemaFilename + "'. File name is invalid"));
        }
    }
}