package uk.gov.justice.generation.pojo.generators.plugin.classgenerator;

import static com.squareup.javapoet.TypeName.LONG;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import java.io.Serializable;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

public class SerializablePlugin implements ClassGeneratorPlugin {

    private static final String SERIAL_VERSION_FIELD_NAME = "serialVersionUID";
    private static final String SERIAL_VERSION_VALUE = "1L";

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder typeSpecBuilder,
                                         final ClassDefinition classDefinition,
                                         final JavaGeneratorFactory javaGeneratorFactory,
                                         final ClassNameFactory classNameFactory,
                                         final GenerationContext generationContext) {

        typeSpecBuilder.addSuperinterface(Serializable.class)
                .addField(FieldSpec
                        .builder(LONG, SERIAL_VERSION_FIELD_NAME, PRIVATE, STATIC, FINAL)
                        .initializer(SERIAL_VERSION_VALUE)
                        .build());

        return typeSpecBuilder;
    }
}
