package uk.gov.justice.raml.jaxrs.core;

import java.io.File;

public class Configuration {

    private File outputDirectory;
    private String basePackageName;
    private File sourceDirectory;

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getBasePackageName() {
        return basePackageName;
    }

    public void setBasePackageName(final String basePackageName) {
        this.basePackageName = basePackageName;
    }

    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

}
