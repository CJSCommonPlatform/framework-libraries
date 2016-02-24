package uk.gov.justice.raml.jaxrs.core;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.raml.model.Resource;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

public class JaxRsResourceCodeGenerator {
    private final Configuration configuration;
    private final JCodeModel codeModel;
    private JaxRsResourceInterfaceCodeGenerator interfaceGenerator;
    private JaxRsResourceImplementationCodeGenerator implementationGenerator;

    public JaxRsResourceCodeGenerator(final Configuration configuration) {
        Validate.notNull(configuration, "configuration can't be null");
        this.configuration = configuration;
        codeModel = new JCodeModel();
        interfaceGenerator = new JaxRsResourceInterfaceCodeGenerator(codeModel, configuration);
        implementationGenerator = new JaxRsResourceImplementationCodeGenerator(codeModel);

    }

    public JDefinedClass createInterface(final Resource ramlResourceDef)
            throws JClassAlreadyExistsException {
        return interfaceGenerator.createInterface(ramlResourceDef);

    }

    public void createImplementation(JDefinedClass resourceInterface)
            throws JClassAlreadyExistsException {
        implementationGenerator.createImplementation(resourceInterface);
    }

    public Set<String> generate() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(baos);
        codeModel.build(configuration.getOutputDirectory(), ps);
        ps.close();
        final Set<String> generatedFiles = new HashSet<String>();
        generatedFiles.addAll(Arrays.asList(StringUtils.split(baos.toString())));
        return generatedFiles;
    }

}
