package uk.gov.justice.schema.catalog.generation.maven;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorFactory;
import uk.gov.justice.schema.catalog.generation.GenerationObjectFactory;

import java.net.URI;
import java.util.List;

/**
 * Factory for creating a {@link MavenCatalogGenerator}
 */
public class MavenCatalogGeneratorFactory implements GeneratorFactory<List<URI>> {

    /**
     * @return A new instance of {@link MavenCatalogGenerator}
     */
    @Override
    public Generator<List<URI>> create() {
        return new MavenCatalogGenerator(new GenerationObjectFactory());
    }
}
