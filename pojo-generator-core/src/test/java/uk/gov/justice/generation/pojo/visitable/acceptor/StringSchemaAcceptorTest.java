package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.StringSchema;
import org.junit.Test;

public class StringSchemaAcceptorTest {

    @Test
    public void shouldVisitStringSchema() throws Exception {
        final String fieldName = "fieldName";
        final StringSchema stringSchema = mock(StringSchema.class);
        final Visitor visitor = mock(Visitor.class);
        final StringSchemaAcceptor stringSchemaAcceptor = new StringSchemaAcceptor();

        stringSchemaAcceptor.accept(fieldName, visitor, stringSchema);

        verify(visitor).visit(fieldName, stringSchema);
    }
}
