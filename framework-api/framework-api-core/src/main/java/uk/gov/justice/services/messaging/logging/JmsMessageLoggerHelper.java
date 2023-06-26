package uk.gov.justice.services.messaging.logging;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import jakarta.json.JsonObject;

public interface JmsMessageLoggerHelper {

    String toJmsTraceString(final Message message);

    JsonObject metadataAsJsonObject(final TextMessage message) throws JMSException;
}