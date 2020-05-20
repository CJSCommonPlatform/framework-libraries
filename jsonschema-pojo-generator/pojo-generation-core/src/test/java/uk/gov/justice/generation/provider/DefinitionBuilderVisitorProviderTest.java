package uk.gov.justice.generation.provider;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.visitor.DefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.ReferenceValueParser;
import uk.gov.justice.generation.pojo.visitor.StringFormatValueParser;

import org.junit.Test;

public class DefinitionBuilderVisitorProviderTest {

    @Test
    public void shouldProvideNewInstanceOfDefinitionBuilderVisitor() throws Exception {
        final DefinitionBuilderVisitor definitionBuilderVisitor_1 = new DefinitionBuilderVisitorProvider().create();
        final DefinitionBuilderVisitor definitionBuilderVisitor_2 = new DefinitionBuilderVisitorProvider().create();

        assertThat(definitionBuilderVisitor_1, notNullValue());
        assertThat(definitionBuilderVisitor_2, notNullValue());
        assertThat(definitionBuilderVisitor_1, not(definitionBuilderVisitor_2));
    }

    @Test
    public void shouldCreateInjectedDependencies() throws Exception {
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitorProvider().create();

        final DefinitionFactory definitionFactory = getValueOfField(definitionBuilderVisitor, "definitionFactory", DefinitionFactory.class);

        assertThat(definitionFactory, notNullValue());
        assertThat(getValueOfField(definitionFactory, "referenceValueParser", ReferenceValueParser.class), notNullValue());
        assertThat(getValueOfField(definitionFactory, "stringFormatValueParser", StringFormatValueParser.class), notNullValue());
    }
}
