package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;

import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class ClassGenerator implements ClassGeneratable {

    private final ClassDefinition classDefinition;
    private final ClassNameFactory classNameFactory;
    private final JavaGeneratorFactory javaGeneratorFactory;
    private final PluginProvider pluginProvider;
    private final GenerationContext generationContext;

    public ClassGenerator(final ClassDefinition classDefinition,
                          final JavaGeneratorFactory javaGeneratorFactory,
                          final PluginProvider pluginProvider,
                          final ClassNameFactory classNameFactory,
                          final GenerationContext generationContext) {
        this.classDefinition = classDefinition;
        this.classNameFactory = classNameFactory;
        this.javaGeneratorFactory = javaGeneratorFactory;
        this.pluginProvider = pluginProvider;
        this.generationContext = generationContext;
    }

    @Override
    public TypeSpec generate() {

        final Builder typeSpecBuilder = classBuilder(getSimpleClassName())
                .addModifiers(PUBLIC);

        pluginProvider.pluginClassGenerators().forEach(plugin ->
                plugin.generateWith(typeSpecBuilder, classDefinition, javaGeneratorFactory, classNameFactory, generationContext));

        return typeSpecBuilder.build();
    }

    @Override
    public String getSimpleClassName() {
        return capitalize(classDefinition.getFieldName());
    }
}
