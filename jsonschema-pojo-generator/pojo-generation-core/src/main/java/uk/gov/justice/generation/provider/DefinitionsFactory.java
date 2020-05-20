package uk.gov.justice.generation.provider;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;

import java.util.List;

public class DefinitionsFactory {

    private final DefinitionBuilderVisitorProvider definitionBuilderVisitorProvider;

    public DefinitionsFactory(final DefinitionBuilderVisitorProvider definitionBuilderVisitorProvider) {
        this.definitionBuilderVisitorProvider = definitionBuilderVisitorProvider;
    }

    public List<Definition> createDefinitions(final Visitable visitable) {

        final DefinitionBuilderVisitor definitionBuilderVisitor = definitionBuilderVisitorProvider.create();

        visitable.accept(definitionBuilderVisitor);

        return definitionBuilderVisitor.getDefinitions();
    }
}
