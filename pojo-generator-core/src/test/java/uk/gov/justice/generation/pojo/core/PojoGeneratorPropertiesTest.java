package uk.gov.justice.generation.pojo.core;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
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
    public void shouldReturnTypeMappingsIfSet() throws Exception {
        final Map<String, String> typemappings = mock(Map.class);
        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withTypeMappings(typemappings)
                .build();

        assertThat(generatorProperties.getTypeMappings(), is(typemappings));
    }

    @Test
    public void shouldReturnOptionalOfEmptyForTypeMappingsIfNotSet() throws Exception {
        final PojoGeneratorProperties generatorProperties = new PojoGeneratorProperties();

        assertThat(generatorProperties.getTypeMappings().isEmpty(), is(true));
    }
}
