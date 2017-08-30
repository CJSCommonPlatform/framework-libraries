package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.visitable.JsonSchemaWrapper;
import uk.gov.justice.generation.pojo.visitable.JsonSchemaWrapperFactory;
import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
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
public class DefaultJsonSchemaAcceptorFactoryTest {

    @Mock
    private JsonSchemaWrapperFactory jsonSchemaWrapperFactory;

    @InjectMocks
    private DefaultJsonSchemaAcceptorFactory jsonSchemaAcceptorFactory;

    @Test
    public void shouldReturnAcceptorMap() throws Exception {
        final Map<Class<? extends Schema>, JsonSchemaAcceptor> acceptorMap = jsonSchemaAcceptorFactory.acceptorMap();

        assertThat(acceptorMap.size(), is(8));
        assertThat(acceptorMap.get(ArraySchema.class), is(instanceOf(ArraySchemaAcceptor.class)));
        assertThat(acceptorMap.get(CombinedSchema.class), is(instanceOf(CombinedSchemaAcceptor.class)));
        assertThat(acceptorMap.get(ObjectSchema.class), is(instanceOf(ObjectSchemaAcceptor.class)));
        assertThat(acceptorMap.get(ReferenceSchema.class), is(instanceOf(ReferenceSchemaAcceptor.class)));
        assertThat(acceptorMap.get(StringSchema.class), is(instanceOf(StringSchemaAcceptor.class)));
        assertThat(acceptorMap.get(BooleanSchema.class), is(instanceOf(BooleanSchemaAcceptor.class)));
        assertThat(acceptorMap.get(NumberSchema.class), is(instanceOf(NumberSchemaAcceptor.class)));
        assertThat(acceptorMap.get(EnumSchema.class), is(instanceOf(EnumSchemaAcceptor.class)));
    }

    @Test
    public void shouldWrapAndAcceptSchema() throws Exception {
        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final Schema schema = mock(Schema.class);
        final JsonSchemaWrapper jsonSchemaWrapper = mock(JsonSchemaWrapper.class);

        when(jsonSchemaWrapperFactory.createWith(schema, jsonSchemaAcceptorFactory)).thenReturn(jsonSchemaWrapper);

        jsonSchemaAcceptorFactory.visitSchema(fieldName, visitor, schema);

        verify(jsonSchemaWrapperFactory).createWith(schema, jsonSchemaAcceptorFactory);
        verify(jsonSchemaWrapper).accept(fieldName, visitor);
    }
}
