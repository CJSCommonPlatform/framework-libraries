package uk.gov.justice.generation.pojo.visitable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorFactory;

import org.everit.json.schema.ObjectSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class VisitableSchemaFactoryTest {

    @InjectMocks
    private VisitableSchemaFactory visitableSchemaFactory;

    @Test
    public void shouldCreateANewJsonSchemaWrapper() throws Exception {

        final ObjectSchema objectSchema = mock(ObjectSchema.class);

        final VisitableSchema visitableSchema = visitableSchemaFactory.createWith(objectSchema, mock(AcceptorFactory.class));

        assertThat(visitableSchema.getSchema(), is(objectSchema));
    }
}
