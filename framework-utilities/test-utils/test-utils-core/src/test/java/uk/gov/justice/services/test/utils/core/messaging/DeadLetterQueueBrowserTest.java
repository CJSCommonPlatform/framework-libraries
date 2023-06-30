package uk.gov.justice.services.test.utils.core.messaging;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.json.JsonObject;

import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeadLetterQueueBrowserTest {

    @Mock
    private Session session;

    @Mock
    private Queue dlqQueue;

    @Mock
    private MessageConsumer dlqMessageConsumer;

    @Mock
    private QueueBrowser dlqBrowser;

    @Mock
    private ConsumerClient consumerClient;

    @Mock
    private JmsSessionFactory jmsSessionFactory;

    private DeadLetterQueueBrowser deadLetterQueueBrowser;

    @BeforeEach
    public void setup() {
        deadLetterQueueBrowser = new DeadLetterQueueBrowser(
                "jms.queue.DLQ",
                dlqQueue,
                newArrayList(session),
                newArrayList(jmsSessionFactory),
                consumerClient);
    }

    @Test
    public void shouldRemoveMessages() throws JMSException {

        when(session.createConsumer(any(Queue.class))).thenReturn(dlqMessageConsumer);

        deadLetterQueueBrowser.removeMessages();

        verify(consumerClient).cleanQueue(dlqMessageConsumer);
    }

    @Test
    public void shouldBeAbleToBrowseAsJson() throws JMSException {
        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn("{\"urn1\": \"urn1\"}", "{\"urn2\": \"urn2\"}");
        Vector<TextMessage> textMessages = new Vector<>(Arrays.asList(textMessage, textMessage));
        when(session.createBrowser(dlqQueue)).thenReturn(dlqBrowser);
        when(dlqBrowser.getEnumeration()).thenReturn(textMessages.elements());

        List<JsonObject> result = deadLetterQueueBrowser.browseAsJson();

        assertThat(result.size(), is(2));
        assertThat(result, contains(IsMapContaining.hasKey("urn1"), IsMapContaining.hasKey("urn2")));
    }

    @Test
    public void shouldBeAbleToBrowse() throws JMSException {
        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn("abc", "def");
        Vector<TextMessage> textMessages = new Vector<>(Arrays.asList(textMessage, textMessage));
        when(session.createBrowser(dlqQueue)).thenReturn(dlqBrowser);
        when(dlqBrowser.getEnumeration()).thenReturn(textMessages.elements());

        List<String> result = deadLetterQueueBrowser.browse();

        assertThat(result.size(), is(2));
        assertThat(result, contains("abc", "def"));
    }

    @Test
    public void shouldReturnEmptyListWhenNoElementsInDlq() throws JMSException {
        Vector<TextMessage> textMessages = new Vector<>();
        when(session.createBrowser(dlqQueue)).thenReturn(dlqBrowser);
        when(dlqBrowser.getEnumeration()).thenReturn(textMessages.elements());

        List<String> result = deadLetterQueueBrowser.browse();

        assertThat(result.size(), is(0));
    }

    @Test
    public void shouldThrowExceptionWhenBrowsing() throws JMSException {

        final JMSException jmsException = new JMSException("Oops");
        when(session.createBrowser(dlqQueue)).thenReturn(dlqBrowser);
        when(dlqBrowser.getEnumeration()).thenThrow(jmsException);


        final MessageConsumerException messageConsumerException = assertThrows(MessageConsumerException.class, () ->
                deadLetterQueueBrowser.browse()
        );

        assertThat(messageConsumerException.getMessage(), is("Fatal error getting messages from DLQ"));
        assertThat(messageConsumerException.getCause(), is(jmsException));
    }

    @Test
    public void shouldThrowExceptionWhenCleaningQueue() throws JMSException {

        when(session.createConsumer(any(Queue.class))).thenThrow(JMSException.class);

        final MessageConsumerException messageConsumerException = assertThrows(MessageConsumerException.class, () ->
                deadLetterQueueBrowser.removeMessages()
        );

        assertThat(messageConsumerException.getMessage(), is("Fatal error cleaning messges from DLQ"));
    }

    @Test
    public void shouldClose() {
        deadLetterQueueBrowser.close();

        verify(jmsSessionFactory).close();
    }

}
