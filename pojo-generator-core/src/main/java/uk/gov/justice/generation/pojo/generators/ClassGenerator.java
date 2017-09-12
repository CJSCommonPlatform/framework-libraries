package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.PluginContext;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class ClassGenerator implements ClassGeneratable {

    private final ClassDefinition classDefinition;
    private final ClassNameFactory classNameFactory;
    private final PluginProvider pluginProvider;
    private final PluginContext pluginContext;

    public ClassGenerator(final ClassDefinition classDefinition,
                          final ClassNameFactory classNameFactory,
                          final PluginProvider pluginProvider,
                          final PluginContext pluginContext) {
        this.classDefinition = classDefinition;
        this.classNameFactory = classNameFactory;
        this.pluginProvider = pluginProvider;
        this.pluginContext = pluginContext;
    }

    @Override
    public TypeSpec generate() {

        final Builder typeSpecBuilder = classBuilder(getClassName())
                .addModifiers(PUBLIC);

        pluginProvider.classModifyingPlugins().forEach(plugin ->
                plugin.generateWith(typeSpecBuilder, classDefinition, pluginContext));

        return typeSpecBuilder.build();
    }

    @Override
    public String getSimpleClassName() {
        return getClassName().simpleName();
    }

    private ClassName getClassName() {
        return classNameFactory.createClassNameFrom(classDefinition);
    }
}
