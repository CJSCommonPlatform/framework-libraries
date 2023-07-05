package uk.gov.justice.maven.generator.io.files.parser.it;


import static org.apache.commons.lang.StringUtils.substringBefore;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

public class RamlFilesScanningTest {

    @Test
    public void shouldProcessInternalAndExternalRamlFiles() throws Exception {

        final List<String> recordedRamlTitles = Files.readAllLines(recordedRamlTitlesFile());
        assertThat(recordedRamlTitles, hasItems("external-1.raml", "external-2.raml", "internal-1.raml"));
    }

    private Path recordedRamlTitlesFile() throws URISyntaxException {
        final URL classUrl = this.getClass().getResource(this.getClass().getSimpleName() + ".class");
        final String generatedSourcesFolder = substringBefore(classUrl.toString(), "test-classes") + "generated-sources/";
        return Paths.get(new URI(generatedSourcesFolder + "raml-titles.txt"));
    }
}
