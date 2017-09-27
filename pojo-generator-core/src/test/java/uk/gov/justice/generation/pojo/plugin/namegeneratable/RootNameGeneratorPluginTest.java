package uk.gov.justice.generation.pojo.plugin.namegeneratable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.Optional;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RootNameGeneratorPluginTest {

    @Mock
    private Schema schema;

    @Mock
    private PluginContext pluginContext;

    @InjectMocks
    private RootNameGeneratorPlugin nameGenerator;

    @Test
    public void shouldParseTheFileNameIntoAValidJavaClassName() throws Exception {
        final String schemaFilename = "object-property-schema.json";

        when(pluginContext.getRootClassName()).thenReturn(Optional.empty());

        assertThat(nameGenerator.rootFieldNameFrom(schema, schemaFilename, pluginContext), is("objectPropertySchema"));
    }

    @Test
    public void shouldRemoveAnyPrependingDotsFromTheFileName() throws Exception {
        final String schemaFilename = "context.command.do-something-or-other.json";

        when(pluginContext.getRootClassName()).thenReturn(Optional.empty());

        assertThat(nameGenerator.rootFieldNameFrom(schema, schemaFilename, pluginContext), is("doSomethingOrOther"));
    }

    @Test
    public void shouldFailIfTheFileNameDoesNotHaveTheExtensionJson() throws Exception {
        final String schemaFilename = "object-property-schema";

        when(pluginContext.getRootClassName()).thenReturn(Optional.empty());

        try {
            nameGenerator.rootFieldNameFrom(schema, schemaFilename, pluginContext);
            fail();
        } catch (final NameGenerationException expected) {
            assertThat(expected.getMessage(), is("Failed to load json schema file '" + schemaFilename + "'. File does not have a '.json' extension"));
        }
    }

    @Test
    public void shouldFailIfTheFileNameIsOnlyTheExtension() throws Exception {
        final String schemaFilename = ".json";

        when(pluginContext.getRootClassName()).thenReturn(Optional.empty());

        try {
            nameGenerator.rootFieldNameFrom(schema, schemaFilename, pluginContext);
            fail();
        } catch (final NameGenerationException expected) {
            assertThat(expected.getMessage(), is("Failed to load json schema file '" + schemaFilename + "'. File name is invalid"));
        }
    }

    @Test
    public void shouldFailIfTheFileNameIsOnlyADot() throws Exception {
        final String schemaFilename = "..json";

        when(pluginContext.getRootClassName()).thenReturn(Optional.empty());

        try {
            nameGenerator.rootFieldNameFrom(schema, schemaFilename, pluginContext);
            fail();
        } catch (final NameGenerationException expected) {
            assertThat(expected.getMessage(), is("Failed to load json schema file '" + schemaFilename + "'. File name is invalid"));
        }
    }

    @Test
    public void shouldSetRootNameFromGeneratorProperties() throws Exception {
        final String schemaFilename = "object-property-schema.json";

        when(pluginContext.getRootClassName()).thenReturn(Optional.of("objectGeneratorProperty"));

        assertThat(nameGenerator.rootFieldNameFrom(schema, schemaFilename, pluginContext), is("objectGeneratorProperty"));
    }

    @Test
    public void shouldLowercaseFirstCharacterOfRootNameFromGeneratorProperties() throws Exception {
        final String schemaFilename = "object-property-schema.json";

        when(pluginContext.getRootClassName()).thenReturn(Optional.of("ObjectGeneratorProperty"));

        assertThat(nameGenerator.rootFieldNameFrom(schema, schemaFilename, pluginContext), is("objectGeneratorProperty"));
    }
}
