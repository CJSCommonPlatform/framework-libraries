package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

/**
 * Generator for creating a java POJO class. The class generation can be modified
 * using {@link uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin}s
 */
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

    @Override
    public String getPackageName() {
        return getClassName().packageName();
    }

    private ClassName getClassName() {
        return classNameFactory.createClassNameFrom(classDefinition);
    }
}
