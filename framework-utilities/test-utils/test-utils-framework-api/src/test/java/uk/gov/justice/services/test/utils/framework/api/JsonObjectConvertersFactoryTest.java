package uk.gov.justice.services.test.utils.framework.api;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.justice.services.common.converter.ObjectToJsonValueConverter;
import uk.gov.justice.services.common.converter.StringToJsonObjectConverter;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsonObjectConvertersFactoryTest {

    private JsonObjectConvertersFactory jsonObjectConvertersFactory = new JsonObjectConvertersFactory();

    @Test
    public void shouldCreateJsonObjectToObjectConverterWithObjectMapperSetUpCorrectly() throws Exception {

        final JsonObjectToObjectConverter jsonObjectToObjectConverter = jsonObjectConvertersFactory
                .jsonObjectToObjectConverter();

        final ObjectMapper objectMapper = getValueOfField(jsonObjectToObjectConverter, "objectMapper", ObjectMapper.class);

        checkSetUpCorrectly(objectMapper);
    }

    @Test
    public void shouldCreateObjectToJsonObjectConverterWithObjectMapperSetUpCorrectly() throws Exception {

        final ObjectToJsonObjectConverter objectToJsonObjectConverter = jsonObjectConvertersFactory
                .objectToJsonObjectConverter();

        final ObjectMapper objectMapper = getValueOfField(objectToJsonObjectConverter, "mapper", ObjectMapper.class);

        checkSetUpCorrectly(objectMapper);
    }

    @Test
    public void shouldCreateObjectToJsonValueConverterWithObjectMapperSetUpCorrectly() throws Exception {

        final ObjectToJsonValueConverter objectToJsonValueConverter = jsonObjectConvertersFactory
                .objectToJsonValueConverter();

        final ObjectMapper objectMapper = getValueOfField(objectToJsonValueConverter, "mapper", ObjectMapper.class);

        checkSetUpCorrectly(objectMapper);
    }

    @Test
    public void shouldCreateStringToJsonObjectConverter() throws Exception {

        assertThat(jsonObjectConvertersFactory.stringToJsonObjectConverter(), is(instanceOf(StringToJsonObjectConverter.class)));
    }

    @SuppressWarnings("unchecked")
    private void checkSetUpCorrectly(final ObjectMapper objectMapper) {
        final Set<String> registeredModuleTypes = getValueOfField(objectMapper, "_registeredModuleTypes", Set.class);

        assertThat(registeredModuleTypes, hasItem("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule"));
        assertThat(registeredModuleTypes, hasItem("com.fasterxml.jackson.datatype.jdk8.Jdk8Module"));
        assertThat(registeredModuleTypes, hasItem("com.fasterxml.jackson.module.paramnames.ParameterNamesModule"));
        assertThat(registeredModuleTypes, hasItem("uk.gov.justice.services.common.converter.jackson.jsr353.InclusionAwareJSR353Module"));
        assertThat(registeredModuleTypes, hasItem("uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesModule"));
        assertThat(registeredModuleTypes, hasItem("uk.gov.justice.services.common.converter.jackson.integerenum.IntegerEnumModule"));
    }
}