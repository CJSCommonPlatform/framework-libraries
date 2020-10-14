package uk.gov.justice.services.yaml;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.ValidationException;
import org.junit.Test;

public class YamlFileValidatorIT {

    private static final String SUBSCRIPTION_SCHEMA_PATH = "/schema/subscription-schema.json";
    private static final String INVALID_YAML_FILE_PATH = "/yaml/failing-validation.yaml";

    private ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();

    private YamlToJsonObjectConverter yamlToJsonObjectConverter = new YamlToJsonObjectConverter(new YamlParser(), objectMapper);

    private YamlFileValidator yamlFileValidator = new YamlFileValidator(
            yamlToJsonObjectConverter,
            new YamlSchemaLoader()
    );

    @Test
    public void shouldFailIfValidationFails() throws Exception {

        final String errorMessageStart = "'/schema/subscription-schema.json' failed validation against schema 'file:";

        final String errorMessageEnd =
                "/yaml/failing-validation.yaml'. " +
                        "Errors: [" +
                            "#/subscriptions_descriptor/subscriptions/0/events/0/schema_uri: " +
                            "string [this-is-wrong://justice.gov.uk/json/schemas/domains/example/example.recipe-added.json] " +
                            "does not match pattern ^http|https:(\\/?\\/?)[^\\s]+$, " +
                            "#/subscriptions_descriptor/subscriptions/0/events/1/schema_uri: " +
                            "string [so-is-this://justice.gov.uk/json/schemas/domains/example/example.recipe-deleted.json] " +
                            "does not match pattern ^http|https:(\\/?\\/?)[^\\s]+$" +
                        "]";

        try {
            final URL yamlUrl = getClass().getResource(INVALID_YAML_FILE_PATH);

            yamlFileValidator.validate(SUBSCRIPTION_SCHEMA_PATH, yamlUrl);
            fail();
        } catch (final YamlValidationException expected) {
            assertThat(expected.getCause(), is(instanceOf(ValidationException.class)));
            assertThat(expected.getMessage(), startsWith(errorMessageStart));
            assertThat(expected.getMessage(), endsWith(errorMessageEnd));
        }
    }
}
