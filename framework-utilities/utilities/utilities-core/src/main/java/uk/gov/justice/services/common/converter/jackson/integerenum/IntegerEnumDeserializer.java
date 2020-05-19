package uk.gov.justice.services.common.converter.jackson.integerenum;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class IntegerEnumDeserializer extends EnumDeserializer {

    private static final long serialVersionUID = 1L;

    private final List<Enum<?>> enumerations;
    private final EnumObjectUtil enumObjectUtil;

    public IntegerEnumDeserializer(final EnumResolver enumResolver,
                                   final EnumObjectUtil enumObjectUtil) {
        super(enumResolver);
        this.enumerations = asList(enumResolver.getRawEnums());
        this.enumObjectUtil = enumObjectUtil;
    }

    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext context) throws IOException {

        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NUMBER_INT) {

            final int enumValue = jsonParser.getIntValue();

            for (final Object enumObject : enumerations) {

                final Optional<Object> enumerationFound = enumObjectUtil.findEnumObject(enumObject, enumValue);

                if (enumerationFound.isPresent()) {
                    return enumerationFound.get();
                }
            }
        }

        return super.deserialize(jsonParser, context);
    }
}
