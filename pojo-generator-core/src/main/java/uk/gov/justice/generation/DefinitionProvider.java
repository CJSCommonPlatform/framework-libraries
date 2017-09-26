package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;

import java.io.File;
import java.util.List;

import org.everit.json.schema.Schema;

public class DefinitionProvider {

    private final SchemaLoader schemaLoader;
    private final VisitableFactory visitableFactory;
    private final DefinitionBuilderVisitor definitionBuilderVisitor;
    private final AcceptorService acceptorService;

    public DefinitionProvider(final SchemaLoader schemaLoader,
                              final VisitableFactory visitableFactory,
                              final DefinitionBuilderVisitor definitionBuilderVisitor,
                              final AcceptorService acceptorService) {
        this.schemaLoader = schemaLoader;
        this.visitableFactory = visitableFactory;
        this.definitionBuilderVisitor = definitionBuilderVisitor;
        this.acceptorService = acceptorService;
    }

    public List<Definition> createDefinitions(final File source, final PluginProvider pluginProvider, final PluginContext pluginContext) {

        final Schema schema = schemaLoader.loadFrom(source);

        final String fieldName = pluginProvider
                .nameGeneratablePlugin()
                .rootFieldNameFrom(schema, source.getName(), pluginContext);

        visitableFactory
                .createWith(fieldName, schema, acceptorService)
                .accept(definitionBuilderVisitor);

        return definitionBuilderVisitor.getDefinitions();
    }
}
