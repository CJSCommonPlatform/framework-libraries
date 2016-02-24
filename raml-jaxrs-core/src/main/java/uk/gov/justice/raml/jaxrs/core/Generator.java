package uk.gov.justice.raml.jaxrs.core;

import java.util.Set;

public interface Generator {

    Set<String> run(String raml, Configuration configuration);

}