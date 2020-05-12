package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.NullSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class NullAcceptorTest {

    @InjectMocks
    private NullAcceptor nullAcceptor;

    @Test
    public void shouldAcceptNullSchemas() throws Exception {

        final String fieldName = "fieldName";
        final NullSchema nullSchema = NullSchema.builder().build();

        final Visitor visitor = mock(Visitor.class);

        nullAcceptor.accept(fieldName, nullSchema, visitor);

        verify(visitor).visit(fieldName, nullSchema);
    }
}
