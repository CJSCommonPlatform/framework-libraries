package uk.gov.justice.services.core.mapping;

public interface NameToMediaTypeConverter {

    MediaType convert(final String name);
    String convert(final MediaType mediaType);
}