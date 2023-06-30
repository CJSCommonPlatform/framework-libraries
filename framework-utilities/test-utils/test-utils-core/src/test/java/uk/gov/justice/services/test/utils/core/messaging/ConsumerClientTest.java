package uk.gov.justice.services.test.utils.core.messaging;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ConsumerClientTest {

    private ConsumerClient consumerClient = new ConsumerClient();

    @Mock
    private MessageConsumer messageConsumer;

    private String message_1 = "message 1";

    @Test
    public void shouldRetrieveMessagesNoWait() throws Exception {

        TextMessage textMessage = mock(TextMessage.class);

        when(textMessage.getText()).thenReturn(message_1);
        when(messageConsumer.receiveNoWait()).thenReturn(textMessage);

        Optional<String> result = consumerClient.retrieveMessageNoWait(messageConsumer);
        assertThat(result.get(), is(message_1));
        verify(messageConsumer).receiveNoWait();
    }

    @Test
    public void shouldRetrieveMessagesWithAWait() throws Exception {
        TextMessage textMessage = mock(TextMessage.class);

        when(textMessage.getText()).thenReturn(message_1);
        when(messageConsumer.receive(1000)).thenReturn(textMessage);

        Optional<String> result = consumerClient.retrieveMessage(messageConsumer, 1000);
        assertThat(result.get(), is(message_1));
        verify(messageConsumer).receive(1000);
    }

    @Test
    public void shouldDrainTheQueueOnClean() throws Exception {
        final TextMessage textMessage_1 = mock(TextMessage.class);
        final TextMessage textMessage_2 = mock(TextMessage.class);
        final TextMessage textMessage_3 = mock(TextMessage.class);

        when(textMessage_1.getText()).thenReturn("message 1");
        when(textMessage_2.getText()).thenReturn("message 2");
        when(textMessage_3.getText()).thenReturn("message 3");
        when(messageConsumer.receiveNoWait()).thenReturn(textMessage_1, textMessage_2, textMessage_3, null);

        consumerClient.cleanQueue(messageConsumer);
        verify(messageConsumer, times(4)).receiveNoWait();
    }

    @Test
    public void shouldThrowExceptionForNullConsumer() {

        final MessageConsumerException messageConsumerException = assertThrows(MessageConsumerException.class, () ->
                consumerClient.retrieveMessageNoWait(null)
        );

        assertThat(messageConsumerException.getMessage(), is("Message consumer not started"));
    }

    @Test
    public void shouldThrowExceptionWhenRetrievingMessage() throws Exception {

        final JMSException jmsException = new JMSException("Ooops");
        when(messageConsumer.receive(10)).thenThrow(jmsException);

        final MessageConsumerException messageConsumerException = assertThrows(MessageConsumerException.class, () ->
                consumerClient.retrieveMessage(messageConsumer, 10)
        );

        assertThat(messageConsumerException.getMessage(), is("Failed to retrieve message"));
        assertThat(messageConsumerException.getCause(), is(jmsException));
    }

}
