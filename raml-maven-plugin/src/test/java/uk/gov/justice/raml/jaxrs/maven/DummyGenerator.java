package uk.gov.justice.raml.jaxrs.maven;

import uk.gov.justice.raml.core.Configuration;
import uk.gov.justice.raml.core.Generator;

import javax.json.Json;
import javax.json.JsonWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Created by david on 04/03/16.
 */
public class DummyGenerator implements Generator {

    private String raml;
    private Configuration configuration;

    @Override
    public Set<String> run(String raml, Configuration configuration) {
        this.raml = raml;
        this.configuration = configuration;

        Path outputPath = Paths.get(configuration.getOutputDirectory().getAbsolutePath(), "example.json");
        try {
            Files.createDirectories(outputPath.getParent());
            OutputStream outputStream = Files.newOutputStream(outputPath);
            JsonWriter writer = Json.createWriter(outputStream);
            writer.writeObject(Json.createObjectBuilder()
                            .add("raml", raml)
                            .add("configuration", Json.createObjectBuilder()
                                    .add("basePackageName", configuration.getBasePackageName())
                                    .add("sourceDirectory", configuration.getSourceDirectory().getPath())
                                    .add("outputDirectory", configuration.getOutputDirectory().getPath())
                            )
                            .build());
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
