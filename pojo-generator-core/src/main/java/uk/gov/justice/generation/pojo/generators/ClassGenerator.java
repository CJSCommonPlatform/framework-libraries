package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;

import com.squareup.javapoet.TypeSpec;

public class ClassGenerator implements ClassGeneratable {

    private final ClassDefinition classDefinition;
    private final JavaGeneratorFactory javaGeneratorFactory;
    private final PluginProvider pluginProvider;

    public ClassGenerator(final ClassDefinition classDefinition, final JavaGeneratorFactory javaGeneratorFactory, final PluginProvider pluginProvider) {
        this.classDefinition = classDefinition;
        this.javaGeneratorFactory = javaGeneratorFactory;
        this.pluginProvider = pluginProvider;
    }

    @Override
    public TypeSpec generate() {
        final String className = classDefinition.getClassName().getSimpleName();

        final TypeSpec.Builder typeSpecBuilder = classBuilder(className)
                .addModifiers(PUBLIC);

        pluginProvider.pluginClassGenerators().forEach(generator ->
                generator.generateWith(typeSpecBuilder, classDefinition, javaGeneratorFactory));

        return typeSpecBuilder.build();
    }

    @Override
    public ClassName getClassName() {
        return classDefinition.getClassName();
    }
}
