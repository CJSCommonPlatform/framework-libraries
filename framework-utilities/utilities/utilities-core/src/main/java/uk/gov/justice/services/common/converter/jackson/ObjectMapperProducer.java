package uk.gov.justice.services.common.converter.jackson;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_WITH_ZONE_ID;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_NULL_MAP_VALUES;
import static com.fasterxml.jackson.databind.cfg.ConstructorDetector.USE_PROPERTIES_BASED;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.TimeZone.getTimeZone;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.ISO_8601;

import uk.gov.justice.services.common.converter.jackson.additionalproperties.AdditionalPropertiesModule;
import uk.gov.justice.services.common.converter.jackson.integerenum.IntegerEnumModule;
import uk.gov.justice.services.common.converter.jackson.jsr353.InclusionAwareJSR353Module;

import java.time.ZonedDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * Produces the configured {@link ObjectMapper}.
 */
@ApplicationScoped
public class ObjectMapperProducer {

    @Produces
    public ObjectMapper objectMapper() {
        return configureObjectMapper(new ObjectMapper());
    }

    public ObjectMapper objectMapperWith(final JsonFactory jsonFactory) {
        return configureObjectMapper(new ObjectMapper(jsonFactory));
    }

    public ObjectMapper yamlObjectMapper() {
        return configureObjectMapper(new ObjectMapper(new YAMLFactory()))
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    private ObjectMapper configureObjectMapper(final ObjectMapper objectMapper) {
        return objectMapper
                .registerModule(javaTimeModuleWithFormattedDateTime())
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule(PROPERTIES))
                .registerModule(new InclusionAwareJSR353Module())
                .registerModule(new AdditionalPropertiesModule())
                .registerModule(new IntegerEnumModule())
                .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(WRITE_DATES_WITH_ZONE_ID, false)
                .configure(WRITE_NULL_MAP_VALUES, false)
                .setTimeZone(getTimeZone(UTC))
                .setSerializationInclusion(NON_ABSENT)
                .enable(WRITE_ENUMS_USING_TO_STRING)
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(READ_ENUMS_USING_TO_STRING)
                .setConstructorDetector(USE_PROPERTIES_BASED);
    }

    private JavaTimeModule javaTimeModuleWithFormattedDateTime() {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(ofPattern(ISO_8601)));
        return javaTimeModule;
    }
}
