package uk.gov.justice.generation.pojo.generators.plugin;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.ROOT;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;

public class EventAnnotationGenerator implements PluginClassGeneratable {

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder typeSpecBuilder,
                                         final ClassDefinition classDefinition,
                                         final JavaGeneratorFactory javaGeneratorFactory,
                                         final ClassNameFactory classNameFactory,
                                         final GenerationContext generationContext) {

        if (ROOT.equals(classDefinition.type())) {

            final String eventName = removeFileExtensionFrom(generationContext.getSourceFilename());

            typeSpecBuilder.addAnnotation(AnnotationSpec.builder(Event.class)
                    .addMember("value", "$S", eventName)
                    .build());
        }

        return typeSpecBuilder;
    }

    private String removeFileExtensionFrom(final String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
