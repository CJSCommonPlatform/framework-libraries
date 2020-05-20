package uk.gov.justice.services.messaging;

/**
 * Abstract class that contains static names of the Json value fields of the metadata and paths to
 * access Json metadata.
 */
public abstract class JsonMetadata implements Metadata {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CREATED_AT = "createdAt";
    public static final String CORRELATION = "correlation";
    public static final String CLIENT_ID = "client";
    public static final String CONTEXT = "context";
    public static final String USER_ID = "user";
    public static final String SESSION_ID = "session";
    public static final String STREAM = "stream";
    public static final String STREAM_ID = "id";
    public static final String VERSION = "version";
    public static final String CAUSATION = "causation";
    public static final String SOURCE = "source";
    public static final String EVENT = "event";
    public static final String EVENT_NUMBER = "eventNumber";
    public static final String PREVIOUS_EVENT_NUMBER = "previousEventNumber";

    protected static final String[] USER_ID_PATH = {CONTEXT, USER_ID};
    protected static final String[] CLIENT_CORRELATION_PATH = {CORRELATION, CLIENT_ID};
    protected static final String[] VERSION_PATH = {STREAM, VERSION};
    protected static final String[] SESSION_ID_PATH = {CONTEXT, SESSION_ID};
    protected static final String[] STREAM_ID_PATH = {STREAM, STREAM_ID};
    protected static final String[] EVENT_NUMBER_PATH = {EVENT, EVENT_NUMBER};
    protected static final String[] PREVIOUS_EVENT_NUMBER_PATH = {EVENT, PREVIOUS_EVENT_NUMBER};
}
