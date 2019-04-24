package uk.gov.justice.json.jolt;

import static com.bazaarvoice.jolt.Chainr.fromSpec;
import static com.bazaarvoice.jolt.JsonUtils.jsonToList;
import static java.lang.String.format;

import uk.gov.justice.json.api.TransformerApi;
import uk.gov.justice.services.common.converter.exception.ConverterException;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.io.IOException;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.bazaarvoice.jolt.Chainr;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JoltTransformer implements TransformerApi {

    @Override
    public JsonObject transformWithJolt(final JsonArray operations,
                                        final JsonObject inputJson) {

        validate(operations, inputJson);

        final List chainrSpecJSON = jsonToList(operations.toString());

        final Chainr chainr = fromSpec(chainrSpecJSON);

        final Object transform = chainr.transform(inputJson);

        return convert(transform);
    }

    private void validate(final JsonArray specJson, final JsonObject inputJson) {


        if (null == specJson) {
            throw new IllegalArgumentException("Input specification is empty");
        }

        if (specJson.size() == 0) {
            throw new IllegalArgumentException("Input specification does not contain any operations");
        }

        if (inputJson.size() == 0) {
            throw new IllegalArgumentException("Input JSON is empty");
        }
    }


    public JsonObject convert(final Object source) {
        try {
            final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
            final JsonObject jsonObject = objectMapper.readValue(objectMapper.writeValueAsString(source), JsonObject.class);
            if (null == jsonObject) {
                throw new ConverterException(format("Failed to convert %s to JsonObject", source));
            }
            return jsonObject;
        } catch (IOException exception) {
            throw new IllegalArgumentException(format("Error while converting %s toJsonObject", source), exception);
        }
    }
}
