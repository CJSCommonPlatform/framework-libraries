package uk.gov.justice.generation.pojo.integration.utils;

public class NullParameter {

    private final Class<?> clazz;

    public NullParameter(final Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
