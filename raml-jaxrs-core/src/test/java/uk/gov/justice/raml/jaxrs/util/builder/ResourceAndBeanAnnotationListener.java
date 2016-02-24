package uk.gov.justice.raml.jaxrs.util.builder;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

import com.sun.jersey.spi.scanning.AnnotationScannerListener;

public class ResourceAndBeanAnnotationListener extends AnnotationScannerListener{
    @SuppressWarnings("unchecked")
    public ResourceAndBeanAnnotationListener() {
        super(new Class[] { Path.class, Stateless.class });
    }

}
