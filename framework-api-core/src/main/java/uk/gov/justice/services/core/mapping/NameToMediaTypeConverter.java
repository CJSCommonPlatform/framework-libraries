package uk.gov.justice.services.core.mapping;

import static java.lang.String.format;
import static java.util.Optional.of;

public interface NameToMediaTypeConverter {

    MediaType convert(final String name);
    String convert(final MediaType mediaType);
}