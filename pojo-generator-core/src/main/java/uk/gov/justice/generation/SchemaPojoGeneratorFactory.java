package uk.gov.justice.generation;

import uk.gov.justice.generation.io.files.JavaFileSimpleNameLister;
import uk.gov.justice.generation.io.files.loader.SchemaLoader;
import uk.gov.justice.generation.pojo.plugin.PluginProviderFactoryFactory;
import uk.gov.justice.generation.pojo.visitable.VisitableFactory;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
import uk.gov.justice.generation.pojo.visitable.acceptor.DefaultAcceptorService;
import uk.gov.justice.generation.pojo.visitor.DefaultDefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.DefinitionBuilderVisitor;
import uk.gov.justice.generation.pojo.visitor.DefinitionFactory;
import uk.gov.justice.generation.pojo.visitor.ReferenceValueParser;
import uk.gov.justice.generation.pojo.visitor.StringFormatValueParser;
import uk.gov.justice.generation.pojo.write.JavaSourceFileProvider;
import uk.gov.justice.generation.pojo.write.NonDuplicatingSourceWriter;
import uk.gov.justice.generation.pojo.write.SourceWriter;
import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorFactory;

import java.io.File;

public class SchemaPojoGeneratorFactory implements GeneratorFactory<File> {

    private final SchemaLoader schemaLoader = new SchemaLoader();
    private final VisitableFactory visitableFactory = new VisitableFactory();
    private final DefinitionFactory definitionFactory = new DefaultDefinitionFactory(new ReferenceValueParser(), new StringFormatValueParser());
    private final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);
    private final AcceptorService acceptorService = new DefaultAcceptorService(visitableFactory);

    private final JavaFileSimpleNameLister javaFileSimpleNameLister = new JavaFileSimpleNameLister();

    private final SourceWriter sourceWriter = new SourceWriter();
    private NonDuplicatingSourceWriter writer = new NonDuplicatingSourceWriter(new JavaSourceFileProvider(), sourceWriter);

    @Override
    public Generator<File> create() {
        return new SchemaPojoGenerator(
                new PluginProviderFactoryFactory().create(),
                new DefinitionProvider(schemaLoader, visitableFactory, definitionBuilderVisitor, acceptorService),
                new GeneratorContextProvider(javaFileSimpleNameLister),
                new ClassNameFactoryProvider(),
                new JavaGeneratorFactoryProvider(),
                new JavaClassFileWriter(writer),
                new PluginContextProvider());
    }
}
