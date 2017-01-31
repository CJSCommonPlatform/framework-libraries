package uk.gov.justice.raml.maven.lintchecker;

import uk.gov.justice.raml.io.files.parser.RamlFileParser;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;

import com.google.common.annotations.VisibleForTesting;
import org.raml.model.Raml;

public class RamlProvider {

    private final RamlFileParser ramlFileParser;

    public RamlProvider() {
        this(new RamlFileParser());
    }

    @VisibleForTesting
    RamlProvider(final RamlFileParser ramlFileParser) {
        this.ramlFileParser = ramlFileParser;
    }

    public Collection<Raml> getRamls(final File sourceDirectory, final Collection<Path> paths) {
        return ramlFileParser.ramlOf(sourceDirectory.toPath(), paths);
    }
}
