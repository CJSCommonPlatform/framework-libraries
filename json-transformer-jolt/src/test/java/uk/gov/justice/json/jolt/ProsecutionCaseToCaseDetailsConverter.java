package uk.gov.justice.json.jolt;

import java.util.Map;

import javax.json.Json;

import com.bazaarvoice.jolt.ContextualTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProsecutionCaseToCaseDetailsConverter implements ContextualTransform {
    Logger LOGGER = LoggerFactory.getLogger(ProsecutionCaseToCaseDetailsConverter.class);

    @Override
    public Object transform(final Object input, final Map<String, Object> context) {
        LOGGER.debug("Transformed");
        return Json.createObjectBuilder().add("label", "label").build();
    }

}
