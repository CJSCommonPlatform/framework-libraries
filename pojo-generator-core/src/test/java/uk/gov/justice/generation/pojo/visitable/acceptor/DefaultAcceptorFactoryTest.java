package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.visitable.VisitableSchema;
import uk.gov.justice.generation.pojo.visitable.VisitableSchemaFactory;
import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAcceptorFactoryTest {

    @Mock
    private VisitableSchemaFactory visitableSchemaFactory;

    @InjectMocks
    private DefaultAcceptorFactory jsonSchemaAcceptorFactory;

    @Test
    public void shouldReturnAcceptorMap() throws Exception {
        final Map<Class<? extends Schema>, Acceptable> acceptorMap = jsonSchemaAcceptorFactory.acceptorMap();

        assertThat(acceptorMap.size(), is(10));
        assertThat(acceptorMap.get(ArraySchema.class), is(instanceOf(ArrayAcceptor.class)));
        assertThat(acceptorMap.get(CombinedSchema.class), is(instanceOf(CombinedAcceptor.class)));
        assertThat(acceptorMap.get(ObjectSchema.class), is(instanceOf(ObjectAcceptor.class)));
        assertThat(acceptorMap.get(ReferenceSchema.class), is(instanceOf(ReferenceAcceptor.class)));
        assertThat(acceptorMap.get(StringSchema.class), is(instanceOf(StringAcceptor.class)));
        assertThat(acceptorMap.get(BooleanSchema.class), is(instanceOf(BooleanAcceptor.class)));
        assertThat(acceptorMap.get(NumberSchema.class), is(instanceOf(NumberAcceptor.class)));
        assertThat(acceptorMap.get(EnumSchema.class), is(instanceOf(EnumAcceptor.class)));
        assertThat(acceptorMap.get(NullSchema.class), is(instanceOf(NullAcceptor.class)));
        assertThat(acceptorMap.get(EmptySchema.class), is(instanceOf(EmptyAcceptor.class)));
    }

    @Test
    public void shouldWrapAndAcceptSchema() throws Exception {
        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final Schema schema = mock(Schema.class);
        final VisitableSchema visitableSchema = mock(VisitableSchema.class);

        when(visitableSchemaFactory.createWith(schema, jsonSchemaAcceptorFactory)).thenReturn(visitableSchema);

        jsonSchemaAcceptorFactory.visitSchema(fieldName, visitor, schema);

        verify(visitableSchemaFactory).createWith(schema, jsonSchemaAcceptorFactory);
        verify(visitableSchema).accept(fieldName, visitor);
    }
}
