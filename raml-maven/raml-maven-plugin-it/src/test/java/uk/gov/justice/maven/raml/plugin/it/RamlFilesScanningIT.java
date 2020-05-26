package uk.gov.justice.maven.raml.plugin.it;


import static org.apache.commons.lang.StringUtils.substringBefore;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import uk.gov.justice.raml.maven.test.RamlTitleAppendingGenerator;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

public class RamlFilesScanningIT {

    @Test
    public void shouldProcessInternalAndExternalRamlFiles() throws Exception {

        List<String> recordedRamlTitles = Files.readAllLines(recordedRamlTitlesFile());
        assertThat(recordedRamlTitles, containsInAnyOrder("external-1.raml", "external-2.raml", "internal-1.raml"));
    }

    private Path recordedRamlTitlesFile() throws URISyntaxException {
        URL classUrl = this.getClass().getResource(this.getClass().getSimpleName() + ".class");
        String generatedSourcesFolder = substringBefore(classUrl.toString(), "test-classes") + "generated-sources/";
        return Paths.get(new URI(generatedSourcesFolder + RamlTitleAppendingGenerator.FILE_NAME));
    }
}
