package uk.gov.justice.api.resource;

import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.function.Consumer;

import javax.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.gov.justice.raml.jaxrs.lib.DefaultEnvelope;
import uk.gov.justice.raml.jaxrs.lib.Dispatcher;
import uk.gov.justice.raml.jaxrs.lib.Envelope;
import uk.gov.justice.raml.jaxrs.lib.RestProcessor;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUsersUserIdResourceTest {
    private static final String NOT_USED_USER_ID = "1";
    private static final JsonObject NOT_USED_ENTITY = createObjectBuilder().build();

    @Mock
    private Dispatcher dispatcher;

    @Mock
    private RestProcessor restProcessor;

    @InjectMocks
    private UsersUserIdResource resource = new DefaultUsersUserIdResource();

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnResponseGeneratedByRestProcessorWhenCreatingUser() throws Exception {

        Response processorResponse = Response.ok().build();
        when(restProcessor.process(any(Consumer.class), any(JsonObject.class), any(HttpHeaders.class),
                any(Map.class))).thenReturn(processorResponse);

        Response resourceResponse = resource.postVndCreateUserJsonUsersByUserId(NOT_USED_USER_ID, NOT_USED_ENTITY);
        assertThat(resourceResponse, is(processorResponse));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnResponseGeneratedByRestProcessorWhenUpdatingUser() throws Exception {

        Response processorResponse = Response.ok().build();
        when(restProcessor.process(any(Consumer.class), any(JsonObject.class), any(HttpHeaders.class),
                any(Map.class))).thenReturn(processorResponse);

        Response resourceResponse = resource.postVndUpdateUserJsonUsersByUserId(NOT_USED_USER_ID, NOT_USED_ENTITY);
        assertThat(resourceResponse, is(processorResponse));

    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void shouldCallDispatcherWhenCreatingUser() throws Exception {

        resource.postVndCreateUserJsonUsersByUserId(NOT_USED_USER_ID, NOT_USED_ENTITY);

        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(restProcessor).process(consumerCaptor.capture(), any(JsonObject.class), any(HttpHeaders.class),
                any(Map.class));

        Envelope<JsonObject> envelope = new DefaultEnvelope<JsonObject>(null, null);
        consumerCaptor.getValue().accept(envelope);

        verify(dispatcher).dispatch(envelope);

    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void shouldCallDispatcherWhenUpdatingUser() throws Exception {

        resource.postVndUpdateUserJsonUsersByUserId(NOT_USED_USER_ID, NOT_USED_ENTITY);

        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(restProcessor).process(consumerCaptor.capture(), any(JsonObject.class), any(HttpHeaders.class),
                any(Map.class));

        Envelope<JsonObject> envelope = new DefaultEnvelope<JsonObject>(null, null);
        consumerCaptor.getValue().accept(envelope);

        verify(dispatcher).dispatch(envelope);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassEntityToRestProcessorWhenCreatingUser() throws Exception {

        JsonObject entity = createObjectBuilder().add("paramNameA", "valueABCD").build();

        resource.postVndUpdateUserJsonUsersByUserId(NOT_USED_USER_ID, entity);

        verify(restProcessor).process(any(Consumer.class), eq(entity), any(HttpHeaders.class), any(Map.class));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassEntityToRestProcessorWhenUpdatingUser() throws Exception {

        JsonObject entity = createObjectBuilder().add("paramNameB", "valueEFG").build();

        resource.postVndUpdateUserJsonUsersByUserId(NOT_USED_USER_ID, entity);

        verify(restProcessor).process(any(Consumer.class), eq(entity), any(HttpHeaders.class), any(Map.class));

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void shouldPassUserIdToRestProcessorWhenCreatingUser() throws Exception {

        resource.postVndUpdateUserJsonUsersByUserId("user1234", NOT_USED_ENTITY);
        ArgumentCaptor<Map> pathParamsCaptor = ArgumentCaptor.forClass(Map.class);

        verify(restProcessor).process(any(Consumer.class), any(JsonObject.class), any(HttpHeaders.class),
                pathParamsCaptor.capture());
        
        Map pathParams = pathParamsCaptor.getValue();
        assertThat(pathParams.entrySet().size(), is(1));
        assertThat(pathParams.containsKey("userId"), is(true));
        assertThat(pathParams.get("userId"), is("user1234"));


    }

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void shouldPassUserIdToRestProcessorWhenUpdatingUser() throws Exception {

        resource.postVndUpdateUserJsonUsersByUserId("user5678", NOT_USED_ENTITY);
        ArgumentCaptor<Map> pathParamsCaptor = ArgumentCaptor.forClass(Map.class);

        verify(restProcessor).process(any(Consumer.class), any(JsonObject.class), any(HttpHeaders.class),
                pathParamsCaptor.capture());
        
        Map pathParams = pathParamsCaptor.getValue();
        assertThat(pathParams.entrySet().size(), is(1));
        assertThat(pathParams.containsKey("userId"), is(true));
        assertThat(pathParams.get("userId"), is("user5678"));

    }

}