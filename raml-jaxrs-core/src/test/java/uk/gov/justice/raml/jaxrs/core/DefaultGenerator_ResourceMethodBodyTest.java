package uk.gov.justice.raml.jaxrs.core;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.gov.justice.raml.jaxrs.util.builder.HttpMethod.POST;
import static uk.gov.justice.raml.jaxrs.util.builder.RamlBuilder.aRaml;
import static uk.gov.justice.raml.jaxrs.util.builder.RamlResourceBuilder.aResource;
import static uk.gov.justice.raml.jaxrs.util.builder.RamlResourceMethodBuilder.aResourceMethod;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.server.impl.application.WebApplicationImpl;
import com.sun.jersey.spi.container.ContainerRequest;

import uk.gov.justice.raml.jaxrs.core.Configuration;
import uk.gov.justice.raml.jaxrs.core.DefaultGenerator;
import uk.gov.justice.raml.jaxrs.lib.DefaultEnvelope;
import uk.gov.justice.raml.jaxrs.lib.Dispatcher;
import uk.gov.justice.raml.jaxrs.lib.Envelope;
import uk.gov.justice.raml.jaxrs.lib.RestProcessor;
import uk.gov.justice.raml.jaxrs.util.compiler.JavaCompilerUtil;

public class DefaultGenerator_ResourceMethodBodyTest {
    private static final JsonObject NOT_USED_JSONOBJECT = Json.createObjectBuilder().build();

    private static final String BASE_PACKAGE = "org.raml.test";

    @Rule
    public TemporaryFolder codegenOutputFolder = new TemporaryFolder();

    @Rule
    public TemporaryFolder compilationOutputFolder = new TemporaryFolder();

    private JavaCompilerUtil compiler;

    private Configuration configuration;

    private DefaultGenerator generator;

    @Mock
    private Dispatcher dispatcher;

    @Mock
    private RestProcessor restProcessorMock;

    @Before
    public void before() {
        initMocks(this);

        configuration = new Configuration();
        configuration.setOutputDirectory(codegenOutputFolder.getRoot());
        configuration.setBasePackageName(BASE_PACKAGE);
        configuration.setSourceDirectory(new File(getClass().getResource("/").getPath()));
        generator = new DefaultGenerator();
        compiler = new JavaCompilerUtil(codegenOutputFolder.getRoot(),
                compilationOutputFolder.getRoot());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnResponseGeneratedByRestProcessor() throws Exception {
        Set<String> generatedClasses = generator.run(
                aRaml()
                        .with(aResource()
                                .with(aResourceMethod(POST)))
                        .toString(),
                configuration);

        Class<?> resourceClass = compiler.compiledClassOf(generatedClasses, BASE_PACKAGE);
        Object resourceObject = instantiate(resourceClass);

        Response processorResponse = Response.ok().build();
        when(restProcessorMock.process(any(Consumer.class), any(JsonObject.class), any(HttpHeaders.class),
                any(Map.class))).thenReturn(processorResponse);

        Method method = firstMethodOf(resourceClass);

        Object result = method.invoke(resourceObject, NOT_USED_JSONOBJECT);

        assertThat(result, is(processorResponse));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void shouldCallDispatcher() throws Exception {

        Set<String> generatedClasses = generator.run(
                aRaml()
                        .with(aResource()
                                .with(aResourceMethod(POST)))
                        .toString(),
                configuration);

        Class<?> resourceClass = compiler.compiledClassOf(generatedClasses, BASE_PACKAGE);
        Object resourceObject = instantiate(resourceClass);

        Method method = firstMethodOf(resourceClass);

        method.invoke(resourceObject, NOT_USED_JSONOBJECT);

        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(restProcessorMock).process(consumerCaptor.capture(), any(JsonObject.class), any(HttpHeaders.class),
                any(Map.class));

        Envelope<JsonObject> envelope = new DefaultEnvelope<JsonObject>(null, null);
        consumerCaptor.getValue().accept(envelope);

        verify(dispatcher).dispatch(envelope);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassJsonObjectToRestProcessor() throws Exception {

        Set<String> generatedClasses = generator.run(
                aRaml()
                        .with(aResource()
                                .with(aResourceMethod(POST)))
                        .toString(),
                configuration);

        Class<?> resourceClass = compiler.compiledClassOf(generatedClasses, BASE_PACKAGE);
        Object resourceObject = instantiate(resourceClass);

        JsonObject jsonObject = Json.createObjectBuilder().add("dummy", "abc").build();

        Method method = firstMethodOf(resourceClass);
        method.invoke(resourceObject, jsonObject);

        verify(restProcessorMock).process(any(Consumer.class), eq(jsonObject), any(HttpHeaders.class), any(Map.class));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassHttpHeadersToRestProcessor() throws Exception {
        Set<String> generatedClasses = generator.run(
                aRaml()
                        .with(aResource()
                                .with(aResourceMethod(POST)))
                        .toString(),
                configuration);

        Class<?> resourceClass = compiler.compiledClassOf(generatedClasses, BASE_PACKAGE);
        Object resourceObject = instantiate(resourceClass);

        HttpHeaders headers = new ContainerRequest(new WebApplicationImpl(), null, null, null, new InBoundHeaders(),
                null);
        setField(resourceObject, "headers", headers);

        Method method = firstMethodOf(resourceClass);
        method.invoke(resourceObject, NOT_USED_JSONOBJECT);

        verify(restProcessorMock).process(any(Consumer.class), any(JsonObject.class), eq(headers), any(Map.class));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void shouldPassMapWithOnePathParamToRestProcessor() throws Exception {
        Set<String> generatedClasses = generator.run(
                aRaml()
                        .with(aResource()
                                .with(aResourceMethod(POST))
                                .withPath("/some/path/{paramA}"))
                        .toString(),
                configuration);

        Class<?> resourceClass = compiler.compiledClassOf(generatedClasses, BASE_PACKAGE);
        Object resourceObject = instantiate(resourceClass);

        Method method = firstMethodOf(resourceClass);
        method.invoke(resourceObject, "paramValue1234", NOT_USED_JSONOBJECT);

        ArgumentCaptor<Map> pathParamsCaptor = ArgumentCaptor.forClass(Map.class);

        verify(restProcessorMock).process(any(Consumer.class), any(JsonObject.class), any(HttpHeaders.class),
                pathParamsCaptor.capture());

        Map pathParams = pathParamsCaptor.getValue();
        assertThat(pathParams.entrySet().size(), is(1));
        assertThat(pathParams.containsKey("paramA"), is(true));
        assertThat(pathParams.get("paramA"), is("paramValue1234"));

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void shouldPassMapWithOnePathParamToRestProcessorWhenInvocing2ndMethod() throws Exception {
        Set<String> generatedClasses = generator.run(
                aRaml()
                        .with(aResource()
                                .with(aResourceMethod(POST)
                                        .withConsumedMediaTypes("application/vnd.cmd-aa+json",
                                                "application/vnd.cmd-bb+json"))
                                .withPath("/some/path/{p1}"))

                        .toString(),
                configuration);

        Class<?> resourceClass = compiler.compiledClassOf(generatedClasses, BASE_PACKAGE);
        Object resourceObject = instantiate(resourceClass);

        List<Method> methods = methodsOf(resourceClass);

        Method secondMethod = methods.get(1);
        secondMethod.invoke(resourceObject, "paramValueXYZ", NOT_USED_JSONOBJECT);

        ArgumentCaptor<Map> pathParamsCaptor = ArgumentCaptor.forClass(Map.class);

        verify(restProcessorMock).process(any(Consumer.class), any(JsonObject.class), any(HttpHeaders.class),
                pathParamsCaptor.capture());

        Map pathParams = pathParamsCaptor.getValue();
        assertThat(pathParams.entrySet().size(), is(1));
        assertThat(pathParams.containsKey("p1"), is(true));
        assertThat(pathParams.get("p1"), is("paramValueXYZ"));

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void shouldPassMapWithTwoPathParamsToRestProcessor() throws Exception {
        Set<String> generatedClasses = generator.run(
                aRaml()
                        .with(aResource()
                                .with(aResourceMethod(POST))
                                .withPath("/some/path/{param1}/{param2}"))

                        .toString(),
                configuration);

        Class<?> resourceClass = compiler.compiledClassOf(generatedClasses, BASE_PACKAGE);
        Object resourceObject = instantiate(resourceClass);

        Method method = firstMethodOf(resourceClass);
        method.invoke(resourceObject, "paramValueABC", "paramValueDEF", NOT_USED_JSONOBJECT);

        ArgumentCaptor<Map> pathParamsCaptor = ArgumentCaptor.forClass(Map.class);

        verify(restProcessorMock).process(any(Consumer.class), any(JsonObject.class), any(HttpHeaders.class),
                pathParamsCaptor.capture());

        Map pathParams = pathParamsCaptor.getValue();
        assertThat(pathParams.entrySet().size(), is(2));
        assertThat(pathParams.containsKey("param1"), is(true));
        assertThat(pathParams.get("param1"), is("paramValueABC"));

        assertThat(pathParams.containsKey("param2"), is(true));
        assertThat(pathParams.get("param2"), is("paramValueDEF"));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void shouldFailIfRamlInvalid() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("ERROR Invalid RAML");
        generator.run("not_a_raml", configuration);
    }
    
    @Test
    public void shouldFailIfInvalidMediaType() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Unknown key: cmd-aa"));
        generator.run(
                aRaml()
                        .with(aResource()
                                .with(aResourceMethod(POST)
                                        .withConsumedMediaTypes("cmd-aa"))
                                .withPath("/some/path/{p1}"))

                        .toString(),
                configuration);
    }

    private Object instantiate(Class<?> resourceClass) throws InstantiationException, IllegalAccessException {
        Object resourceObject = resourceClass.newInstance();
        setField(resourceObject, "restProcessor", restProcessorMock);
        setField(resourceObject, "dispatcher", dispatcher);
        return resourceObject;
    }

    private Method firstMethodOf(Class<?> resourceClass) {
        List<Method> methods = methodsOf(resourceClass);
        return methods.get(0);
    }

    private void setField(Object resourceObject, String fieldName, Object object)
            throws IllegalAccessException {
        Field field = fieldOf(resourceObject.getClass(), fieldName);
        field.setAccessible(true);
        field.set(resourceObject, object);
    }

    private Field fieldOf(Class<?> clazz, String fieldName) {
        Optional<Field> field = Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.getName().equals(fieldName))
                .findFirst();
        assertTrue(field.isPresent());
        return field.get();
    }

    private List<Method> methodsOf(Class<?> class1) {
        return Arrays.stream(class1.getDeclaredMethods()).filter(m -> !m.getName().contains("jacoco"))
                .collect(Collectors.toList());
    }

}
