package uk.gov.justice.raml.jaxrs.util.compiler;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.core.spi.scanning.Scanner;
import com.sun.jersey.spi.scanning.AnnotationScannerListener;

import uk.gov.justice.raml.jaxrs.util.builder.ResourceAndBeanAnnotationListener;

public class ExtendedPackagesResourceConfig extends PackagesResourceConfig {

    public ExtendedPackagesResourceConfig(String... packages) {
        super(packages);
    }
    
    @Override
    public void init(final Scanner scanner) {
        final AnnotationScannerListener asl = new ResourceAndBeanAnnotationListener();
        scanner.scan(asl);
        getClasses().addAll(asl.getAnnotatedClasses());
    }

}
