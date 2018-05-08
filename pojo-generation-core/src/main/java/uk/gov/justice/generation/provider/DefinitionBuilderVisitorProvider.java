package uk.gov.justice.generation.provider;

import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.visitor.DefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.ReferenceValueParser;
import uk.gov.justice.generation.pojo.visitor.StringFormatValueParser;

public class DefinitionBuilderVisitorProvider {

    public DefinitionBuilderVisitor create() {
        final DefinitionFactory definitionFactory = new DefaultDefinitionFactory(new ReferenceValueParser(), new StringFormatValueParser());
        return new DefinitionBuilderVisitor(definitionFactory);
    }
}
