package uk.gov.justice.services.file.alfresco;


import static java.util.Collections.singletonList;

import javax.ws.rs.core.MultivaluedHashMap;

public final class Headers {

    public static final String ALFRESCO_USER_ID = "cppuid";

    private Headers() {
    }

    public static MultivaluedHashMap<String, Object> headersWithUserId(final String id) {
        final MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.put(ALFRESCO_USER_ID, singletonList(id));
        return headers;
    }
}
