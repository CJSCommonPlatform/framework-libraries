package uk.gov.justice.services.messaging.jms.exception;

/**
 * Exception representing a failure to send an envelope via Jms to a {@link jakarta.jms.Queue} or
 * {@link jakarta.jms.Topic}.
 */
public class JmsEnvelopeSenderException extends RuntimeException {

    private static final long serialVersionUID = -6321871357233248686L;

    public JmsEnvelopeSenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
