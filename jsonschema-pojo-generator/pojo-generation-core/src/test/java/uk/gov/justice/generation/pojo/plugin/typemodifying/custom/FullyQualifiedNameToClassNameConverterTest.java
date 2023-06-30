package uk.gov.justice.generation.pojo.plugin.typemodifying.custom;

import static com.squareup.javapoet.ClassName.get;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import uk.gov.justice.generation.pojo.plugin.PluginConfigurationException;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FullyQualifiedNameToClassNameConverterTest {

    @InjectMocks
    private FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter;

    @Test
    public void shouldConvertAReferenceValueFromJsonSchemaIntoAClassName() throws Exception {

        assertThat(fullyQualifiedNameToClassNameConverter.convert("java.math.BigInteger"), is(equalTo(get(BigInteger.class))));
        assertThat(fullyQualifiedNameToClassNameConverter.convert("java.time.ZonedDateTime"), is(equalTo(get(ZonedDateTime.class))));
        assertThat(fullyQualifiedNameToClassNameConverter.convert("java.util.UUID"), is(equalTo(get(UUID.class))));
    }

    @Test
    public void shouldFailIfTheFullyQualifiedNameHasNoDot() throws Exception {

        try {
            fullyQualifiedNameToClassNameConverter.convert("BigInteger");
            fail();
        } catch (final PluginConfigurationException expected) {
            assertThat(expected.getMessage(), is("Failed to create class name from fully qualified name 'BigInteger'"));
        }
    }

    @Test
    public void shouldFailIfTheFullyQualifiedNameHasNoPackageName() throws Exception {

        try {
            fullyQualifiedNameToClassNameConverter.convert(".BigInteger");
            fail();
        } catch (final PluginConfigurationException expected) {
            assertThat(expected.getMessage(), is("Cannot create class name from fully qualified name '.BigInteger'. No package name found"));
        }
    }

    @Test
    public void shouldFailIfTheFullyQualifiedNameHasNoSimpleName() throws Exception {

        try {
            fullyQualifiedNameToClassNameConverter.convert("java.math.");
            fail();
        } catch (final PluginConfigurationException expected) {
            assertThat(expected.getMessage(), is("Cannot create class name from fully qualified name 'java.math.'. No simple name found"));
        }
    }
}
