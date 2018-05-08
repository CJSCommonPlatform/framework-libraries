package uk.gov.justice.generation.pojo.plugin.typemodifying.custom;

import static com.squareup.javapoet.ClassName.get;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.TypeMapping;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

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
    @SuppressWarnings("unchecked")
    public void shouldGetCustomTypeMappingFromGeneratorPropertiesAndConvertToClassName() throws Exception {

        final String mappingPropertyName = "uuid";
        final Optional<String> mappingPropertyValue = of("java.util.UUID");
        final ClassName uuidClassName = get(UUID.class);

        final Predicate<TypeMapping> mappingType = mock(Predicate.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(pluginContext.typeMappingsFilteredBy(mappingType, mappingPropertyName)).thenReturn(mappingPropertyValue);
        when(fullyQualifiedNameToClassNameConverter.convert(mappingPropertyValue.get())).thenReturn(uuidClassName);

        final Optional<ClassName> className = customReturnTypeMapper.customTypeFor(mappingType, mappingPropertyName, pluginContext);

        if (className.isPresent()) {
            assertThat(className.get(), is(uuidClassName));
        } else {
            fail();
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnEmptyIfNoMappingExistsForThePropertyName() throws Exception {

        final String mappingPropertyName = "uuid";
        final Optional<String> mappingPropertyValue = empty();
        final Predicate<TypeMapping> mappingType = mock(Predicate.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(pluginContext.typeMappingsFilteredBy(mappingType, mappingPropertyName)).thenReturn(mappingPropertyValue);

        final Optional<ClassName> className = customReturnTypeMapper.customTypeFor(mappingType, mappingPropertyName, pluginContext);

        assertThat(className.isPresent(), is(false));

        verifyZeroInteractions(fullyQualifiedNameToClassNameConverter);
    }
}
