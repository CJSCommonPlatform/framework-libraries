package uk.gov.justice.services.test.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("_metadata")
public class Metadata {
    private String name;

    public Metadata(){}

    @JsonCreator
    public Metadata(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
