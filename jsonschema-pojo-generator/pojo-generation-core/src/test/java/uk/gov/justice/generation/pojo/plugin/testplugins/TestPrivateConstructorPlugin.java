package uk.gov.justice.generation.pojo.plugin.testplugins;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;

import com.squareup.javapoet.TypeSpec;

public class TestPrivateConstructorPlugin implements ClassModifyingPlugin {

    private TestPrivateConstructorPlugin() {
    }

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder classBuilder, final ClassDefinition classDefinition, final PluginContext pluginContext) {
        return null;
    }
}
