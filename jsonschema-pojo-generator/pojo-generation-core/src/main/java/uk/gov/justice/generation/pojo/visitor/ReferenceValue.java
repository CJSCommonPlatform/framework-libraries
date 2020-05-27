package uk.gov.justice.generation.pojo.visitor;

public class ReferenceValue {

    private final String path;
    private final String name;

    public ReferenceValue(final String path, final String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public static ReferenceValue fromReferenceValueString(final String referenceValueString) {
        final String path = referenceValueString.substring(0, referenceValueString.lastIndexOf('/'));
        final String name = referenceValueString.substring(referenceValueString.lastIndexOf('/') + 1);

        return new ReferenceValue(path, name);
    }

    @Override
    public String toString() {
        return path + "/" + name;
    }
}
