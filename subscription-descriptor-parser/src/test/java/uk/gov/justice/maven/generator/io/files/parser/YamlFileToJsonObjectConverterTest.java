package uk.gov.justice.maven.generator.io.files.parser;

import static java.nio.file.Paths.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

public class YamlFileToJsonObjectConverterTest {

    @Test
    public void shouldConvertYamlToJsonObject() {
        final YamlFileToJsonObjectConverter converter = new YamlFileToJsonObjectConverter();
        try {
            converter.convert(get("src/test/resources/subscription.yaml"));
        } catch (IOException e) {
            fail("Unable to convert to JSON Object");
        }
    }

    @Test
    public void shouldThrowExceptionWhenlToConvertYamlToJsonObjectAUnavailableFile() {
        final YamlFileToJsonObjectConverter converter = new YamlFileToJsonObjectConverter();
        try {
            converter.convert(get("src/test/resources/no-subscription.yaml"));
            fail("Failure, Converted a unavailable file to JSON Object");
        } catch (IOException e) {
            assertThat(e, is(instanceOf(IOException.class)));
            assertThat(e.getLocalizedMessage(), is("src/test/resources/no-subscription.yaml"));
        }
    }

}