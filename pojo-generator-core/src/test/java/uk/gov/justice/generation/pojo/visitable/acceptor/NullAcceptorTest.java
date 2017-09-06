package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.junit.Assert.*;

import org.everit.json.schema.NullSchema;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import uk.gov.justice.generation.pojo.visitor.Visitor;


@RunWith(MockitoJUnitRunner.class)
public class NullAcceptorTest {

    @InjectMocks
    private NullAcceptor nullAcceptor;

    @Test
    public void shouldAcceptNullSchemas() throws Exception {

        final String fieldName = "fieldName";
        final NullSchema nullSchema = NullSchema.builder().build();

        final Visitor visitor = mock(Visitor.class);

        nullAcceptor.accept(fieldName, visitor, nullSchema);

        verify(visitor).visit(fieldName, nullSchema);
    }
}
