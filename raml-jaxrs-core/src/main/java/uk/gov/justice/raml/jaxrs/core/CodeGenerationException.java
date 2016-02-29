package uk.gov.justice.raml.jaxrs.core;

import com.sun.codemodel.JClassAlreadyExistsException;

public class CodeGenerationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public CodeGenerationException(JClassAlreadyExistsException e) {
        super(e);
    }


}
