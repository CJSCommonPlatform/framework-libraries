package uk.gov.justice.generation.pojo.plugin.classmodifying.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AdditionalPropertiesDeterminerTest {


    @InjectMocks
    private AdditionalPropertiesDeterminer additionalPropertiesDeterminer;

    @Test
    public void shouldReturnTrueIfAdditionalPropertiesIsTrueAndTheAddAdditionalPropertiesPluginIsInUse() throws Exception {

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(classDefinition.allowAdditionalProperties()).thenReturn(true);
        when(pluginContext.isPluginInUse(AddAdditionalPropertiesToClassPlugin.class)).thenReturn(true);

        assertThat(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext), is(true));
    }

    @Test
    public void shouldNotReturnTrueIfAdditionalPropertiesIsTrueAndTheAddAdditionalPropertiesPluginIsNotInUse() throws Exception {

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(classDefinition.allowAdditionalProperties()).thenReturn(true);
        when(pluginContext.isPluginInUse(AddAdditionalPropertiesToClassPlugin.class)).thenReturn(false);

        assertThat(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext), is(false));
    }

    @Test
    public void shouldNotReturnTrueIfAdditionalPropertiesIsFalseAndTheAddAdditionalPropertiesPluginIsInUse() throws Exception {

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(classDefinition.allowAdditionalProperties()).thenReturn(false);
        when(pluginContext.isPluginInUse(AddAdditionalPropertiesToClassPlugin.class)).thenReturn(true);

        assertThat(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext), is(false));
    }

    @Test
    public void shouldNotReturnTrueIfAdditionalPropertiesIsFalseAndTheAddAdditionalPropertiesPluginIsNotInUse() throws Exception {

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(classDefinition.allowAdditionalProperties()).thenReturn(false);
        when(pluginContext.isPluginInUse(AddAdditionalPropertiesToClassPlugin.class)).thenReturn(false);

        assertThat(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext), is(false));
    }
}
