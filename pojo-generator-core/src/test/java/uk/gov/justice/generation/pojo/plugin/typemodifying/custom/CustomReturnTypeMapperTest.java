package uk.gov.justice.generation.pojo.plugin.typemodifying.custom;

import static com.google.common.collect.Sets.newHashSet;
import static com.squareup.javapoet.ClassName.get;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.plugin.PluginConfigurationException;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;
import uk.gov.justice.generation.pojo.visitor.ReferenceValue;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.squareup.javapoet.ClassName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomReturnTypeMapperTest {

    @Mock
    private FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter;

    @InjectMocks
    private CustomReturnTypeMapper customReturnTypeMapper;

    @Test
    public void shouldGetTheCustomTypeMappingFromGeneratorPropertiesAndConvertToAClassName() throws Exception {

        final String mappingPropertyName = "typemapping.reference.uuid";
        final ReferenceValue referenceValue = new ReferenceValue("#/definitions", "uuid");
        final Optional<String> mappingPropertyValue = of("java.util.UUID");
        final ClassName uuidClassName = get(UUID.class);

        final PluginContext pluginContext = mock(PluginContext.class);

        final Set<String> propertyNames = newHashSet(
                "some.property",
                "typemapping.reference.custom-date-time",
                mappingPropertyName,
                "some.other.property");

        when(pluginContext.getPropertyNames()).thenReturn(propertyNames);
        when(pluginContext.generatorPropertyValueOf(mappingPropertyName)).thenReturn(mappingPropertyValue);
        when(fullyQualifiedNameToClassNameConverter.convert(mappingPropertyValue.get())).thenReturn(uuidClassName);

        final Optional<ClassName> className = customReturnTypeMapper.customType(referenceValue, pluginContext);

        if (className.isPresent()) {
          assertThat(className.get(), is(uuidClassName));
        } else {
            fail();
        }
    }

    @Test
    public void shouldReturnEmptyIfNoCustomMappingExistsForTheReferenceValueName() throws Exception {

        final ReferenceValue referenceValue = new ReferenceValue("#/definitions", "uuid");

        final PluginContext pluginContext = mock(PluginContext.class);

        final Set<String> propertyNames = newHashSet(
                "some.property",
                "typemapping.reference.custom-date-time",
                "some.other.property");

        when(pluginContext.getPropertyNames()).thenReturn(propertyNames);

        final Optional<ClassName> className = customReturnTypeMapper.customType(referenceValue, pluginContext);

        assertThat(className.isPresent(), is(false));

        verifyZeroInteractions(fullyQualifiedNameToClassNameConverter);
    }

    @Test
    public void shouldFailIfNoPropertyValueFoundInThePluginContext() throws Exception {

        final String mappingPropertyName = "typemapping.reference.uuid";
        final ReferenceValue referenceValue = new ReferenceValue("#/definitions", "uuid");
        final Optional<String> missingPropertyValue = empty();

        final PluginContext pluginContext = mock(PluginContext.class);

        final Set<String> propertyNames = newHashSet(
                "some.property",
                "typemapping.reference.custom-date-time",
                mappingPropertyName,
                "some.other.property");

        when(pluginContext.getPropertyNames()).thenReturn(propertyNames);
        when(pluginContext.generatorPropertyValueOf(mappingPropertyName)).thenReturn(missingPropertyValue);

        try {
            customReturnTypeMapper.customType(referenceValue, pluginContext);
            fail();
        } catch (final PluginConfigurationException expected) {
            assertThat(expected.getMessage(), is("Failed to get generator property 'typemapping.reference.uuid'"));
        }

        verifyZeroInteractions(fullyQualifiedNameToClassNameConverter);
    }
}
