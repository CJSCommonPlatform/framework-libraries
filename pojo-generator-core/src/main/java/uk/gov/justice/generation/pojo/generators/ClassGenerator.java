package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.generators.plugin.PluginClassGeneratable;

import java.util.List;

import com.squareup.javapoet.TypeSpec;

public class ClassGenerator implements ClassGeneratable {

    private final ClassDefinition classDefinition;
    private final JavaGeneratorFactory javaGeneratorFactory;
    private final List<PluginClassGeneratable> plugins;

    public ClassGenerator(final ClassDefinition classDefinition, final JavaGeneratorFactory javaGeneratorFactory, final List<PluginClassGeneratable> plugins) {
        this.classDefinition = classDefinition;
        this.javaGeneratorFactory = javaGeneratorFactory;
        this.plugins = plugins;
    }

    @Override
    public TypeSpec generate() {
        final String className = classDefinition.getClassName().getSimpleName();

        final TypeSpec.Builder typeSpecBuilder = classBuilder(className)
                .addModifiers(PUBLIC);

        plugins.forEach(generator ->
                generator.generateWith(typeSpecBuilder, classDefinition, javaGeneratorFactory));

        return typeSpecBuilder.build();
    }

    @Override
    public ClassName getClassName() {
        return classDefinition.getClassName();
    }
}
