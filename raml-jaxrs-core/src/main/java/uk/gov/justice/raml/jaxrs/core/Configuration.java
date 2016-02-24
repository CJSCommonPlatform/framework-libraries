package uk.gov.justice.raml.jaxrs.core;

import java.io.File;

public class Configuration {

    private File outputDirectory;
    private String basePackageName;
    private File sourceDirectory;
    private String restIFPackageName = "resource";
    private String interfaceNameSuffix = "Resource";


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

    public String getRestIFPackageName() {
        return restIFPackageName;
    }

    public void setRestIFPackageName(String restIFPackageName) {
        this.restIFPackageName = restIFPackageName;
    }

    public String getInterfaceNameSuffix() {
        return interfaceNameSuffix;
    }

    public void setInterfaceNameSuffix(String interfaceNameSuffix) {
        this.interfaceNameSuffix = interfaceNameSuffix;
    }

}
