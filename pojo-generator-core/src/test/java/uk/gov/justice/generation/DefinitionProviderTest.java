package uk.gov.justice.generation;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.NameGeneratablePlugin;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;

import java.io.File;
import java.util.List;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefinitionProviderTest {

    @Mock
    private SchemaLoader schemaLoader;

    @Mock
    private VisitableFactory visitableFactory;

    @Mock
    private DefinitionBuilderVisitor definitionBuilderVisitor;

    @Mock
    private AcceptorService acceptorService;

    @InjectMocks
    private DefinitionProvider definitionProvider;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCreateDefinitions() throws Exception {
        final String fieldName = "fieldName";
        final String schemaFilename = "schemaFilename";

        final List<Definition> definitions = mock(List.class);
        final File schemaFile = mock(File.class);
        final Schema schema = mock(Schema.class);

        final PluginProvider pluginProvider = mock(PluginProvider.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final NameGeneratablePlugin nameGeneratablePlugin = mock(NameGeneratablePlugin.class);

        when(schemaLoader.loadFrom(schemaFile)).thenReturn(schema);
        when(pluginProvider.nameGeneratablePlugin()).thenReturn(nameGeneratablePlugin);

        when(schemaFile.getName()).thenReturn(schemaFilename);
        when(nameGeneratablePlugin.rootFieldNameFrom(schema, schemaFilename, pluginContext)).thenReturn(fieldName);

        when(visitableFactory.createWith(fieldName, schema, acceptorService)).thenReturn(mock(Visitable.class));
        when(definitionBuilderVisitor.getDefinitions()).thenReturn(definitions);

        final List<Definition> result = definitionProvider.createDefinitions(schemaFile, pluginProvider, pluginContext);

        assertThat(result, is(definitions));
    }
}
