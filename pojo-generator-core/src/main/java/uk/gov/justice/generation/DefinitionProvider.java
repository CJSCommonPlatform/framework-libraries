package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.visitor.ReferenceValueParser;

import java.io.File;
import java.util.List;

public class DefinitionProvider {

    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final VisitableFactory visitableFactory = new VisitableFactory();
    private DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(new DefaultDefinitionFactory(new ReferenceValueParser()));

    public List<Definition> createDefinitions(final File source, final PluginProvider pluginProvider, final PluginContext pluginContext) {

        final String fieldName = pluginProvider
                .nameGeneratablePlugin()
                .rootFieldNameFrom(schemaLoader.loadFrom(source), source.getName(), pluginContext);

        visitableFactory.createWith(
                fieldName,
                schemaLoader.loadFrom(source),
                new DefaultAcceptorService(visitableFactory))
                .accept(definitionBuilderVisitor);

        return definitionBuilderVisitor.getDefinitions();
    }
}
