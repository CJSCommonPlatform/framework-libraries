package uk.gov.justice.maven.generator.io.files.parser.generator;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;
import uk.gov.justice.maven.generator.io.files.parser.io.FileTreeScannerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * Service for calling a generator on RAML files.
 */
public class GenerateGoalProcessor {
    private static final Logger LOGGER = getLogger(GenerateGoalProcessor.class);
    private static final String CLASSPATH = "CLASSPATH";

    private final MojoGeneratorFactory mojoGeneratorFactory;
    private final FileParser parser;

    private final FileTreeScannerFactory scannerFactory;

    public GenerateGoalProcessor(final MojoGeneratorFactory mojoGeneratorFactory,
                                 final FileTreeScannerFactory scannerFactory,
                                 final FileParser parser) {
        this.mojoGeneratorFactory = mojoGeneratorFactory;
        this.scannerFactory = scannerFactory;
        this.parser = parser;
    }

    @SuppressWarnings("unchecked")
    public void generate(final GenerateGoalConfig config) throws IOException {
        LOGGER.info("Config: {}", config);
        final String[] includes = config.getIncludes().toArray(new String[config.getIncludes().size()]);
        final String[] excludes = config.getExcludes().toArray(new String[config.getExcludes().size()]);

        final GeneratorConfig generatorConfig = new GeneratorConfig(config.getSourceDirectory(),
                config.getOutputDirectory(), config.getBasePackageName(), config.getProperties(), config.getSourcePaths());

        final Collection<Path> paths = getPaths(config.getSourceDirectory(), includes, excludes);
        final Collection<Path> classPaths;

        if (config.getGenerationPath()==GenerateMojo.GenerationPath.SOURCE_AND_CLASS_PATH) {
            final Map<Path, Collection<Path>> combinedPaths = new HashMap<>();

            classPaths = getPaths(Paths.get(CLASSPATH), includes, excludes);

            combinedPaths.put(config.getSourceDirectory(), paths);
            combinedPaths.put((Paths.get(format("%s/%s", config.getSourceDirectory().toString(), CLASSPATH))), classPaths);

            combinedPaths.forEach((baseDir, combinedPath) ->
                parseSourceDirectory(config, generatorConfig, combinedPath, isPathFromClasspath(baseDir, config.getGenerationPath())));
        } else {
            if ((config.getGenerationPath()==GenerateMojo.GenerationPath.CLASSPATH)) {
                classPaths = getPaths(Paths.get(CLASSPATH), includes, excludes);

                parseSourceDirectory(config, generatorConfig,
                        classPaths, isPathFromClasspath(config.getSourceDirectory(), config.getGenerationPath()));
            } else {

                if(StringUtils.isNoneBlank(config.getSourceDirectory().toString()) && !config.getSourceDirectory().toString().contains(CLASSPATH)){
                    parseSourceDirectory(config, generatorConfig, paths, isPathFromClasspath(config.getSourceDirectory(), config.getGenerationPath()));
                }
                else{
                    classPaths = getPaths(Paths.get(CLASSPATH), includes, excludes);

                    parseSourceDirectory(config, generatorConfig, classPaths, isPathFromClasspath(config.getSourceDirectory(), config.getGenerationPath()));
                }
            }
        }
    }

    private Collection<Path> getPaths(final Path path, final String[] includes, final String[] excludes) throws IOException {
        return scannerFactory.create().find(path, includes, excludes);
    }

    private void parseSourceDirectory(final GenerateGoalConfig config, final GeneratorConfig generatorConfig,
                                      final Collection<Path> paths, boolean isPathFromClasspath) {
        parser
                .parse((isPathFromClasspath?Paths.get(format("%s/%s",config.getSourceDirectory().toString(),CLASSPATH)):config.getSourceDirectory()), paths)
                .forEach(file -> mojoGeneratorFactory.instanceOf(config.getGeneratorName()).run(file, generatorConfig));
    }

    private boolean isPathFromClasspath(final Path baseDir, final GenerateMojo.GenerationPath generationPath){
        return baseDir.toString().contains(CLASSPATH) || (generationPath != null && generationPath.name()==(CLASSPATH));
    }
}

