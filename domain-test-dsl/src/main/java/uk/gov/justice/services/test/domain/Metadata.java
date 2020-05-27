package uk.gov.justice.services.test.domain;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("_metadata")
public class Metadata {
    private String name;

    public Metadata(){}

    public Metadata(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
