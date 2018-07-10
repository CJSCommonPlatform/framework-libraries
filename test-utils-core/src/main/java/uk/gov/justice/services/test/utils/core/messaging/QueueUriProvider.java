package uk.gov.justice.services.test.utils.core.messaging;

import static java.lang.String.format;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.getArtemisHost;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.getHost;

import org.apache.commons.lang3.StringUtils;

public class QueueUriProvider {

    private static final String BASE_URI_PATTERN = "tcp://%s:61616";
    private static final String ARTEMIS_URI = "ARTEMIS_URI";

    public String getQueueUri() {
        return format(BASE_URI_PATTERN, getHost());
    }

    public String getArtemisQueueUri() {
        final String artemisUri = System.getProperty(ARTEMIS_URI);
        if (StringUtils.isNotBlank(artemisUri)) {
            return artemisUri;
        }
        return format(BASE_URI_PATTERN, getArtemisHost());
    }

    public static String queueUri() {
        return new QueueUriProvider().getQueueUri();
    }

    public static String artemisQueueUri(){
        return new QueueUriProvider().getArtemisQueueUri();
    }
}
