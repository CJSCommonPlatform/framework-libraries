package uk.gov.justice.generation.pojo.generators;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.EnumDefinition;

import com.squareup.javapoet.TypeSpec;
import org.junit.Test;

public class EnumGeneratorTest {

    @Test
    public void shouldGenerateTypeSpecForSingleWordEnumValue() {
        final EnumDefinition enumDefinition = new EnumDefinition("title", singletonList("Mr"));
        final EnumGenerator enumGenerator = new EnumGenerator(enumDefinition);

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Title"));
        assertThat(typeSpec.enumConstants.keySet(), hasItem("MR"));
        assertThat(typeSpec.modifiers.size(), is(1));
        assertThat(typeSpec.modifiers, hasItem(PUBLIC));
        assertThat(typeSpec.fieldSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs.size(), is(2));
    }

    @Test
    public void shouldGenerateTypeSpecForMultiWordEnumValue() {
        final EnumDefinition enumDefinition = new EnumDefinition("title", singletonList("Multi word value"));
        final EnumGenerator enumGenerator = new EnumGenerator(enumDefinition);

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Title"));
        assertThat(typeSpec.enumConstants.keySet(), hasItem("MULTI_WORD_VALUE"));
        assertThat(typeSpec.modifiers.size(), is(1));
        assertThat(typeSpec.modifiers, hasItem(PUBLIC));
        assertThat(typeSpec.fieldSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs.size(), is(2));
    }

    @Test
    public void shouldGenerateTypeSpecForBlankString() {
        final EnumDefinition enumDefinition = new EnumDefinition("title", singletonList(""));
        final EnumGenerator enumGenerator = new EnumGenerator(enumDefinition);

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Title"));
        assertThat(typeSpec.enumConstants.keySet(), hasItem("BLANK"));
    }

    @Test
    public void shouldReturnClassName() {
        final EnumDefinition enumDefinition = new EnumDefinition("title", emptyList());

        final EnumGenerator enumGenerator = new EnumGenerator(enumDefinition);

        assertThat(enumGenerator.getClassName(), is("Title"));

    }

}