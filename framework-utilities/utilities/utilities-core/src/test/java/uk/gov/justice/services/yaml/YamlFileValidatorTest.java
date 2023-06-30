package uk.gov.justice.services.yaml;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;

import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class YamlFileValidatorTest {

    private static final String SUBSCRIPTION_SCHEMA_PATH = "/schema/subscription-schema.json";

    @Mock
    private YamlSchemaLoader yamlSchemaLoader;

    @Mock
    private YamlToJsonObjectConverter yamlToJsonObjectConverter;

    @InjectMocks
    private YamlFileValidator yamlFileValidator;


    @Test
    public void shouldValidateSubscriptionYamlFile() throws IOException {
        final URL yamlUrl = new URL("file:/test");
        final JSONObject jsonObject = mock(JSONObject.class);
        final Schema schema = mock(Schema.class);

        when(yamlToJsonObjectConverter.convert(yamlUrl)).thenReturn(jsonObject);
        when(yamlSchemaLoader.loadSchema(SUBSCRIPTION_SCHEMA_PATH)).thenReturn(schema);

        yamlFileValidator.validate(SUBSCRIPTION_SCHEMA_PATH, yamlUrl);

        verify(schema).validate(jsonObject);
    }

    @Test
    public void shouldThrowYamlParserException() throws IOException {
        try {
            final URL yamlUrl = new URL("file:/test");
            when(yamlSchemaLoader.loadSchema(SUBSCRIPTION_SCHEMA_PATH)).thenThrow(new IOException());

            yamlFileValidator.validate(SUBSCRIPTION_SCHEMA_PATH, yamlUrl);
            fail();
        } catch (final Exception expected) {
            assertThat(expected, is(instanceOf(YamlParserException.class)));
            assertThat(expected.getMessage(), containsString("Unable to load JSON schema"));
        }
    }
}
