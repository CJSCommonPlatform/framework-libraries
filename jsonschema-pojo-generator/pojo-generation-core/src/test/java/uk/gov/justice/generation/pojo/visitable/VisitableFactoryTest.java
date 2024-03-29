package uk.gov.justice.generation.pojo.visitable;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;

import org.everit.json.schema.ObjectSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class VisitableFactoryTest {

    @InjectMocks
    private VisitableFactory visitableFactory;

    @Test
    public void shouldCreateVisitableSchema() throws Exception {

        final String fieldName = "fieldName";
        final ObjectSchema objectSchema = mock(ObjectSchema.class);
        final AcceptorService acceptorService = mock(AcceptorService.class);

        final Visitable visitableSchema = visitableFactory.createWith(fieldName, objectSchema, acceptorService);

        assertThat(visitableSchema, notNullValue());
        assertThat(visitableSchema, is(instanceOf(VisitableSchema.class)));
    }
}
