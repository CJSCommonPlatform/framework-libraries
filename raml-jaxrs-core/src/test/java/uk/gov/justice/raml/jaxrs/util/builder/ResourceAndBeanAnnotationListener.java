package uk.gov.justice.raml.jaxrs.util.builder;

import com.sun.jersey.spi.scanning.AnnotationScannerListener;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

public class ResourceAndBeanAnnotationListener extends AnnotationScannerListener {
    @SuppressWarnings("unchecked")
    public ResourceAndBeanAnnotationListener() {
        super(new Class[]{Path.class, Stateless.class});
    }

}
