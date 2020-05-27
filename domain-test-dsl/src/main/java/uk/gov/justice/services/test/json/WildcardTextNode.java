package uk.gov.justice.services.test.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class WildcardTextNode extends TextNode {

    private static final String WILDCARD_PATTERN = "*";

    private final boolean wildard;

    public WildcardTextNode(String v) {
        super(v);
        this.wildard = WILDCARD_PATTERN.equals(v);
    }

    @Override
    public boolean equals(Object o) {
        if (wildard && ((JsonNode)o).isTextual()) {
            return true;
        }
        return super.equals(o);
    }
}
