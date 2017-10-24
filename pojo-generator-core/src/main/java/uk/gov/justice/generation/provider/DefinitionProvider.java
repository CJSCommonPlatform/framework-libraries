package uk.gov.justice.generation.provider;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;

import java.util.List;

import org.everit.json.schema.Schema;

public class DefinitionProvider {

    private final VisitableFactory visitableFactory;
    private final DefinitionBuilderVisitorProvider definitionBuilderVisitorProvider;
    private final AcceptorService acceptorService;

    public DefinitionProvider(final VisitableFactory visitableFactory,
                              final DefinitionBuilderVisitorProvider definitionBuilderVisitorProvider,
                              final AcceptorService acceptorService) {
        this.visitableFactory = visitableFactory;
        this.definitionBuilderVisitorProvider = definitionBuilderVisitorProvider;
        this.acceptorService = acceptorService;
    }

    public List<Definition> createDefinitions(
            final Schema schema,
            final String jsonSchemaFileName,
            final PluginProvider pluginProvider,
            final PluginContext pluginContext) {

        final String fieldName = pluginProvider
                .nameGeneratablePlugin()
                .rootFieldNameFrom(schema, jsonSchemaFileName, pluginContext);

        final DefinitionBuilderVisitor definitionBuilderVisitor = definitionBuilderVisitorProvider.create();

        visitableFactory
                .createWith(fieldName, schema, acceptorService)
                .accept(definitionBuilderVisitor);

        return definitionBuilderVisitor.getDefinitions();
    }
}
