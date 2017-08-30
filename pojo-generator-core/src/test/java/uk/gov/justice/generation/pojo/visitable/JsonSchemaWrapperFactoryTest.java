package uk.gov.justice.generation.pojo.visitable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.visitable.acceptor.JsonSchemaAcceptorFactory;

import org.everit.json.schema.ObjectSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JsonSchemaWrapperFactoryTest {

    @InjectMocks
    private JsonSchemaWrapperFactory jsonSchemaWrapperFactory;

    @Test
    public void shouldCreateANewJsonSchemaWrapper() throws Exception {

        final ObjectSchema objectSchema = mock(ObjectSchema.class);

        final JsonSchemaWrapper jsonSchemaWrapper = jsonSchemaWrapperFactory.createWith(objectSchema, mock(JsonSchemaAcceptorFactory.class));

        assertThat(jsonSchemaWrapper.getSchema(), is(objectSchema));
    }
}
