package uk.gov.justice.services.test.utils.core.messaging;

import static com.google.common.base.Splitter.on;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.getArtemisHost;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.getHost;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class QueueUriProvider {

    private static final String BASE_URI_PATTERN = "tcp://%s:61616";

    /**
     * Takes a comma separated list of broker hosts
     */
    private static final String ARTEMIS_URI = "ARTEMIS_URI";

    public String getQueueUri() {
        return format(BASE_URI_PATTERN, getHost());
    }

    public List<String> getArtemisQueueUri() {
        final String artemisUri = System.getProperty(ARTEMIS_URI);
        if (StringUtils.isNotBlank(artemisUri)) {
            return on(",").splitToList(artemisUri);
        }
        return newArrayList(format(BASE_URI_PATTERN, getArtemisHost()));
    }

    public static String queueUri() {
        return new QueueUriProvider().getQueueUri();
    }

    public static List<String> artemisQueueUri() {
        return new QueueUriProvider().getArtemisQueueUri();
    }
}
