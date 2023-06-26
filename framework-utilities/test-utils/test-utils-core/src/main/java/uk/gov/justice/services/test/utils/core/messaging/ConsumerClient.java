package uk.gov.justice.services.test.utils.core.messaging;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;


/**
 * Utility class  for {@link MessageConsumer} to
 * retrieve /clean messages from {@link Queue} / {@link Topic}
 *
 * @author gopal
 *
 */
public class ConsumerClient {

    public Optional<String> retrieveMessageNoWait(final MessageConsumer messageConsumer) {
        if (messageConsumer == null) {
            throw new MessageConsumerException("Message consumer not started");
        }
        return retrieve(() -> messageConsumer.receiveNoWait());
    }

    public Optional<String> retrieveMessage(final MessageConsumer messageConsumer, final long timeout) {
        if (messageConsumer == null) {
            throw new MessageConsumerException("Message consumer not started");
        }
        return retrieve(() -> messageConsumer.receive(timeout));
    }

    private Optional<String> retrieve(final MessageSupplier messageSupplier) {
        try {
            final TextMessage message = (TextMessage) messageSupplier.getMessage();
            if (message == null) {
                return empty();
            }
            return of(message.getText());
        } catch (final JMSException e) {
            throw new MessageConsumerException("Failed to retrieve message", e);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void cleanQueue(final MessageConsumer messageConsumer) {
        if (messageConsumer == null) {
            throw new MessageConsumerException("Message consumer not started");
        }
        while (retrieveMessageNoWait(messageConsumer).isPresent()) {
        }
    }

    @FunctionalInterface
    private interface MessageSupplier {
        Message getMessage() throws JMSException;
    }

}
