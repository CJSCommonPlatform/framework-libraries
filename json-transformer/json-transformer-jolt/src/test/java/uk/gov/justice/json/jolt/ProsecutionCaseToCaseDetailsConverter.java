package uk.gov.justice.json.jolt;

import java.util.Map;

import com.bazaarvoice.jolt.ContextualTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;

public class ProsecutionCaseToCaseDetailsConverter implements ContextualTransform {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProsecutionCaseToCaseDetailsConverter.class);

    @Override
    public Object transform(final Object input, final Map<String, Object> context) {
        LOGGER.debug("Transformed");
        return getJsonBuilderFactory().createObjectBuilder().add("label", "label").build();
    }
}
