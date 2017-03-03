package uk.gov.justice.services.jdbc.persistence;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Simple wrapper class for getting an {@link InitialContext} to allow for mocking in tests
 */
public class InitialContextFactory {

    /**
     * Instantiates a new {@link InitialContext}
     *
     * @return a shiny new {@link InitialContext}
     * @throws NamingException if instantiating the {@link InitialContext} fails
     */
    public InitialContext create() throws NamingException {
        return new InitialContext();
    }
}
