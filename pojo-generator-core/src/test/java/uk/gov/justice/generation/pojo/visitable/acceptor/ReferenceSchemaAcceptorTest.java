package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceSchemaAcceptorTest {

    @Mock
    private JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory;

    @InjectMocks
    private ReferenceSchemaAcceptor referenceSchemaAcceptor;

    @Test
    public void shouldAcceptAReferenceSchema() throws Exception {
        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ReferenceSchema referenceSchema = mock(ReferenceSchema.class);
        final Schema referredSchema = mock(Schema.class);

        when(referenceSchema.getReferredSchema()).thenReturn(referredSchema);

        referenceSchemaAcceptor.accept(fieldName, visitor, referenceSchema);

        verify(jsonSchemaAcceptorFactory).visitSchema(fieldName, visitor, referredSchema);
    }
}
