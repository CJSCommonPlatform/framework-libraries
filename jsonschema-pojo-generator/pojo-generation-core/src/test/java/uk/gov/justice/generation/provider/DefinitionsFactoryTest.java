package uk.gov.justice.generation.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.visitable.Visitable;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefinitionsFactoryTest {


    @Mock
    private DefinitionBuilderVisitorProvider definitionBuilderVisitorProvider;

    @InjectMocks
    private DefinitionsFactory definitionsFactory;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCreateDefinitions() throws Exception {

        final List<Definition> definitions = mock(List.class);
        final DefinitionBuilderVisitor definitionBuilderVisitor = mock(DefinitionBuilderVisitor.class);
        final Visitable visitable = mock(Visitable.class);

        when(definitionBuilderVisitorProvider.create()).thenReturn(definitionBuilderVisitor);
        when(definitionBuilderVisitor.getDefinitions()).thenReturn(definitions);

        assertThat(definitionsFactory.createDefinitions(visitable), is(definitions));

        verify(visitable).accept(definitionBuilderVisitor);
    }
}
