package uk.gov.justice.services.test.utils.alfresco.stub;


import static java.nio.charset.StandardCharsets.UTF_8;
import static jakarta.json.Json.createArrayBuilder;
import static jakarta.json.Json.createObjectBuilder;

import java.io.IOException;
import java.io.InputStream;

import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;

import org.apache.commons.io.IOUtils;

public class RequestRecorder {

    private JsonArrayBuilder recordedRequests = createArrayBuilder();

    public void recordUploadRequest(final String fileName, final InputStream fileContent, final String userId) throws IOException {
        recordedRequests.add(createObjectBuilder()
                .add("fileName", fileName)
                .add("fileContent", IOUtils.toString(fileContent, UTF_8))
                .add("userId", userId)
        );
    }

    public JsonArray recordedRequests() {
        return recordedRequests.build();
    }

    public void reset() {
        recordedRequests = createArrayBuilder();
    }
}