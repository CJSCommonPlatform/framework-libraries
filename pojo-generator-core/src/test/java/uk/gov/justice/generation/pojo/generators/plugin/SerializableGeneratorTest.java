package uk.gov.justice.generation.pojo.generators.plugin;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import java.io.Serializable;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SerializableGeneratorTest {

    @Test
    public void shouldAddSerializationToTypeSpec() throws Exception {
        final JavaGeneratorFactory generatorFactory = mock(JavaGeneratorFactory.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final ClassDefinition classDefinition = new ClassDefinition(CLASS,"address");

        final TypeSpec.Builder typeSpecBuilder = classBuilder("ClassName");

        new SerializableGenerator().generateWith(typeSpecBuilder, classDefinition, generatorFactory, classNameFactory);

        final TypeSpec typeSpec = typeSpecBuilder.build();

        assertThat(typeSpec.superinterfaces.size(), is(1));
        assertThat(typeSpec.superinterfaces, hasItem(TypeName.get(Serializable.class)));
        assertThat(typeSpec.fieldSpecs, hasItem(FieldSpec
                .builder(TypeName.LONG, "serialVersionUID", PRIVATE, STATIC, FINAL)
                .initializer("1L").build()));

        verifyZeroInteractions(generatorFactory);
    }
}
