package uk.gov.justice.generation.pojo.dom;

public class ClassName {

    private final String packageName;
    private final String simpleName;

    public ClassName(final String packageName, final String simpleName) {
        this.packageName = packageName;
        this.simpleName = simpleName;
    }

    public ClassName(final Class<?> clazz) {
        this.packageName = clazz.getPackage().getName();
        this.simpleName = clazz.getSimpleName();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getFullyQualifiedName() {
        return packageName + "." + simpleName;
    }
}
