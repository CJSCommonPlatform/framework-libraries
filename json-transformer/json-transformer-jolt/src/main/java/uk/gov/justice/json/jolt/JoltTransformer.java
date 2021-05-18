package uk.gov.justice.json.jolt;

import static java.lang.String.format;

import uk.gov.justice.services.common.converter.StringToJsonObjectConverter;
import uk.gov.justice.services.common.converter.exception.ConverterException;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.unifiedsearch.TransformerApi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

@ApplicationScoped
public class JoltTransformer implements TransformerApi {

    @Inject
    private StringToJsonObjectConverter stringToJsonObjectConverter;


    @Override
    public JsonObject transformWithJolt(final String operationsString, final JsonObject inputJson) {

        validateArguments(operationsString, inputJson);

        final JSONObject operations = new JSONObject(operationsString);
        final JSONArray operationsArray = operations.getJSONArray("operations");
        final List chainrSpecJSON = JsonUtils.jsonToList(operationsArray.toString());
        final Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
        final Map<String, Object> stringObjectMap = JsonUtils.jsonToMap(new ByteArrayInputStream(inputJson.toString().getBytes()));

        final Object transform = chainr.transform(stringObjectMap);

        if (transform != null) {
            return convert(transform);
        }

        throw new ConverterException(format("Failed to jolt transform '%s' to using operations '%s'", inputJson, operationsString));
    }


    private void validateArguments(final String specJson, final JsonObject inputJson) {
        final JsonObject operations = stringToJsonObjectConverter.convert(specJson);

        if (null == operations.get("operations") ) {
            throw new IllegalArgumentException("Input specification is empty");
        }

        if (operations.getJsonArray("operations").size() == 0) {
            throw new IllegalArgumentException("Input specification does not contain any operations");
        }

        if (inputJson.size() == 0) {
            throw new IllegalArgumentException("Input JSON is empty");
        }
    }


    public JsonObject convert(final Object source) {
        try {
            final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
            final String content = objectMapper.writeValueAsString(source);
            final JsonObject jsonObject = objectMapper.readValue(content, JsonObject.class);
            if (null == jsonObject) {
                throw new ConverterException(format("Failed to convert %s to JsonObject", source));
            }
            return jsonObject;
        } catch (final IOException exception) {
            throw new IllegalArgumentException(format("Error while converting %s toJsonObject", source), exception);
        }
    }
}
