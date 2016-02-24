package uk.gov.justice.raml.jaxrs.core;

import com.sun.codemodel.JClassAlreadyExistsException;

public class CodeGenareationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public CodeGenareationException(JClassAlreadyExistsException e) {
        super(e);
    }


}
