package uk.gov.justice.json.jolt;

import static com.bazaarvoice.jolt.Chainr.fromSpec;
import static com.bazaarvoice.jolt.JsonUtils.jsonToList;
import static java.lang.String.format;

import uk.gov.justice.services.common.converter.StringToJsonObjectConverter;
import uk.gov.justice.services.common.converter.exception.ConverterException;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.unifiedsearch.TransformerApi;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;

import com.bazaarvoice.jolt.Chainr;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class JoltTransformer implements TransformerApi {

    @Inject
    private StringToJsonObjectConverter stringToJsonObjectConverter;


    @Override
    public JsonObject transformWithJolt(final String operationsString, final JsonObject inputJson) {

        validateArguments(operationsString, inputJson);

        final JsonObject operations = stringToJsonObjectConverter.convert(operationsString);

        final JsonArray operationsArray = operations.getJsonArray("operations");

        final List chainrSpecJSON = jsonToList(operationsArray.toString());

        final Chainr chainr = fromSpec(chainrSpecJSON);

        final Object transform = chainr.transform(inputJson);

        return convert(transform);
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
            final JsonObject jsonObject = objectMapper.readValue(objectMapper.writeValueAsString(source), JsonObject.class);
            if (null == jsonObject) {
                throw new ConverterException(format("Failed to convert %s to JsonObject", source));
            }
            return jsonObject;
        } catch (final IOException exception) {
            throw new IllegalArgumentException(format("Error while converting %s toJsonObject", source), exception);
        }
    }
}
