package uk.gov.justice.services.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.CreationException;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.InjectionException;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@ApplicationScoped
public class InitialContextProducer {

    @Produces
    public InitialContext initialContext(@SuppressWarnings("unused") final InjectionPoint injectionPoint) {

        try {
            return new InitialContext();
        } catch (final NamingException e) {
            throw new CreationException("Failed to create an InitialContext", e);
        }
    }

    public void close(@Disposes final InitialContext initialContext) {

        try {
            initialContext.close();
        } catch (final NamingException e) {
            throw new InjectionException("Failed to close InitialContext", e);
        }
    }
}
