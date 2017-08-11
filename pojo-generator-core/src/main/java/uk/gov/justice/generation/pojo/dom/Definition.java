package uk.gov.justice.generation.pojo.dom;

import java.util.Optional;

public interface Definition {

    String getFieldName();
    ClassName getClassName();
    Optional<ClassName> getGenericType();
}
