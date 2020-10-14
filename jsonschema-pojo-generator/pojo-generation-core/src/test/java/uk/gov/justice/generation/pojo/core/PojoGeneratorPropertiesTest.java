package uk.gov.justice.generation.pojo.core;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static uk.gov.justice.generation.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

public class PojoGeneratorPropertiesTest {

    @Test
    public void shouldReturnTrueIfExcludeDefaultPluginsIsSetToTrue() throws Exception {
        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withExcludeDefaultPlugins(true)
                .build();

        assertThat(generatorProperties.isExcludeDefaultPlugins(), is(true));
    }

    @Test
    public void shouldReturnFalseIfExcludeDefaultPluginsIsSetToFalse() throws Exception {
        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withExcludeDefaultPlugins(false)
                .build();

        assertThat(generatorProperties.isExcludeDefaultPlugins(), is(false));
    }

    @Test
    public void shouldReturnFalseIfExcludeDefaultPluginsIsNotSet() throws Exception {
        final PojoGeneratorProperties generatorProperties = new PojoGeneratorProperties();

        assertThat(generatorProperties.isExcludeDefaultPlugins(), is(false));
    }

    @Test
    public void shouldReturnOptionalOfPluginsIfSet() throws Exception {
        final List<String> plugins = singletonList("plugins");
        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withPlugins(plugins)
                .build();

        assertThat(generatorProperties.getPlugins(), is(Optional.of(plugins)));
    }

    @Test
    public void shouldReturnOptionalOfEmptyForPluginsIfNotSet() throws Exception {
        final PojoGeneratorProperties generatorProperties = new PojoGeneratorProperties();

        assertThat(generatorProperties.getPlugins(), is(Optional.empty()));
    }

    @Test
    public void shouldReturnOptionalOfRootClassNameIfSet() throws Exception {
        final String rootClassName = "rootClassName";
        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withRootClassName(rootClassName)
                .build();

        assertThat(generatorProperties.getRootClassName(), is(Optional.of(rootClassName)));
    }

    @Test
    public void shouldReturnOptionalOfEmptyForRootClassNameIfNotSet() throws Exception {
        final PojoGeneratorProperties generatorProperties = new PojoGeneratorProperties();

        assertThat(generatorProperties.getRootClassName(), is(Optional.empty()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnReferenceTypeMappingsAsMapIfSet() throws Exception {
        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .addReferenceTypeMappingOf("key_1", "value_1")
                .addReferenceTypeMappingOf("key_2", "value_2")
                .addFormatTypeMappingOf("key_3", "value_3")
                .addReferenceTypeMappingOf("key_4", "value_4")
                .build();

        final Map<String, String> referenceTypeMappings = generatorProperties
                .typeMappingsFilteredBy(typeMapping -> "reference".equals(typeMapping.getType()));

        assertThat(referenceTypeMappings.size(), is(3));
        assertThat(referenceTypeMappings.get("key_1"), is("value_1"));
        assertThat(referenceTypeMappings.get("key_2"), is("value_2"));
        assertThat(referenceTypeMappings.get("key_4"), is("value_4"));
    }

    @Test
    public void shouldReturnEmptyMapIfTypeMappingsAreNotSet() throws Exception {
        final PojoGeneratorProperties generatorProperties = new PojoGeneratorProperties();

        final Map<String, String> referenceTypeMappings = generatorProperties
                .typeMappingsFilteredBy(typeMapping -> "reference".equals(typeMapping.getType()));

        assertThat(referenceTypeMappings.isEmpty(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnFormatTypeMappingsAsMapIfSet() throws Exception {
        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .addFormatTypeMappingOf("key_1", "value_1")
                .addFormatTypeMappingOf("key_2", "value_2")
                .addReferenceTypeMappingOf("key_3", "value_3")
                .addFormatTypeMappingOf("key_4", "value_4")
                .build();

        final Map<String, String> formatTypeMappings = generatorProperties
                .typeMappingsFilteredBy(typeMapping -> "format".equals(typeMapping.getType()));

        assertThat(formatTypeMappings.size(), is(3));
        assertThat(formatTypeMappings.get("key_1"), is("value_1"));
        assertThat(formatTypeMappings.get("key_2"), is("value_2"));
        assertThat(formatTypeMappings.get("key_4"), is("value_4"));
    }
}
