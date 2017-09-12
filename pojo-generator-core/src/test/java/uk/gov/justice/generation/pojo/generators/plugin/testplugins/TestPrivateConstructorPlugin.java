package uk.gov.justice.generation.pojo.generators.plugin.testplugins;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.PluginContext;

import com.squareup.javapoet.TypeSpec;

public class TestPrivateConstructorPlugin implements ClassModifyingPlugin {

    private TestPrivateConstructorPlugin() {
    }

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder typeSpecBuilder, final ClassDefinition classDefinition, final PluginContext pluginContext) {
        return null;
    }
}
