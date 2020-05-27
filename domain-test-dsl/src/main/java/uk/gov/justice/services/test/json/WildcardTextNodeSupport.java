package uk.gov.justice.services.test.json;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class WildcardTextNodeSupport {

    private WildcardTextNodeSupport() {
    }

    public static void enable(final JsonNode parent) {
        parent.fieldNames().forEachRemaining(fieldName -> {
            final JsonNode child = parent.get(fieldName);
            if (child.isTextual()) {
                ((ObjectNode) parent).put(fieldName, new WildcardTextNode(child.textValue()));
            } else {
                // use recursion
                enable(child);
            }
        });
    }

    public static void enable(final List<? extends JsonNode> parents) {
        for (JsonNode parent : parents) {
            enable(parent);
        }
    }
}
