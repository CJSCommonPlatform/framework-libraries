package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.JavaFileSimpleNameLister;
import uk.gov.justice.generation.io.files.parser.SchemaDefinition;
import uk.gov.justice.generation.pojo.core.PackageNameParser;
import uk.gov.justice.generation.pojo.plugin.PluginProviderFactory;
import uk.gov.justice.generation.pojo.plugin.PluginProviderFactoryFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.hashcode.SerialVersionUIDGenerator;
import uk.gov.justice.generation.pojo.validation.SchemaValidatorVisitor;
import uk.gov.justice.generation.pojo.validation.Validator;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorService;
import uk.gov.justice.generation.pojo.write.JavaClassFileWriter;
import uk.gov.justice.generation.pojo.write.JavaSourceFileProvider;
import uk.gov.justice.generation.pojo.write.NonDuplicatingSourceWriter;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.generation.provider.DefinitionBuilderVisitorProvider;
import uk.gov.justice.generation.provider.DefinitionsFactory;
import uk.gov.justice.generation.provider.GeneratorContextProvider;
import uk.gov.justice.generation.provider.PluginContextProvider;
import uk.gov.justice.generation.provider.PojoGeneratorFactoriesProvider;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorFactory;

public class SchemaPojoGeneratorFactory implements GeneratorFactory<SchemaDefinition> {

    private final VisitableFactory visitableFactory = new VisitableFactory();
    private final DefinitionBuilderVisitorProvider definitionBuilderVisitorProvider = new DefinitionBuilderVisitorProvider();
    private final AcceptorService acceptorService = new DefaultAcceptorService(visitableFactory);

    private final JavaFileSimpleNameLister javaFileSimpleNameLister = new JavaFileSimpleNameLister();

    private final SourceWriter sourceWriter = new SourceWriter();
    private final NonDuplicatingSourceWriter writer = new NonDuplicatingSourceWriter(new JavaSourceFileProvider(), sourceWriter);

    private final PackageNameParser packageNameParser = new PackageNameParser();

    private final PluginContextProvider pluginContextProvider = new PluginContextProvider();
    private final PojoGeneratorFactoriesProvider pojoGeneratorFactoriesProvider = new PojoGeneratorFactoriesProvider();
    private final PluginProviderFactory pluginProviderFactory = new PluginProviderFactoryFactory().create();
    private final DefinitionsFactory definitionsFactory = new DefinitionsFactory(definitionBuilderVisitorProvider);
    private final GeneratorContextProvider generatorContextProvider = new GeneratorContextProvider(javaFileSimpleNameLister, packageNameParser);
    private final SchemaValidatorVisitor schemaValidatorVisitor = new SchemaValidatorVisitor(new Validator());
    private final SerialVersionUIDGenerator serialVersionUIDGenerator = new SerialVersionUIDGenerator();

    private final Bootstrapper bootstrapper = new Bootstrapper(
            pluginContextProvider,
            pojoGeneratorFactoriesProvider,
            pluginProviderFactory,
            generatorContextProvider);

    @Override
    public Generator<SchemaDefinition> create() {
        return new SchemaPojoGenerator(
                definitionsFactory,
                new JavaClassFileWriter(writer),
                visitableFactory,
                acceptorService,
                bootstrapper,
                schemaValidatorVisitor,
                serialVersionUIDGenerator
        );
    }
}
