package uk.gov.justice.generation.pojo.core;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PojoGeneratorPropertiesBuilder {

    private boolean excludeDefaultPlugins;
    private List<String> plugins;
    private String rootClassName;
    private Map<String, String> typeMappings;

    public static PojoGeneratorPropertiesBuilder pojoGeneratorPropertiesBuilder() {
        return new PojoGeneratorPropertiesBuilder();
    }

    public PojoGeneratorPropertiesBuilder withExcludeDefaultPlugins(final boolean excludeDefaultPlugins) {
        this.excludeDefaultPlugins = excludeDefaultPlugins;
        return this;
    }

    public PojoGeneratorPropertiesBuilder withPlugins(final List<String> plugins) {
        this.plugins = plugins;
        return this;
    }

    public PojoGeneratorPropertiesBuilder withRootClassName(final String rootClassName) {
        this.rootClassName = rootClassName;
        return this;
    }

    public PojoGeneratorPropertiesBuilder withTypeMappings(final Map<String, String> typeMappings) {
        this.typeMappings = typeMappings;
        return this;
    }

    public PojoGeneratorProperties build() throws IllegalAccessException {
        final PojoGeneratorProperties pojoGeneratorProperties = new PojoGeneratorProperties();

        setField(pojoGeneratorProperties, "excludeDefaultPlugins", excludeDefaultPlugins);
        setField(pojoGeneratorProperties, "plugins", plugins);
        setField(pojoGeneratorProperties, "rootClassName", rootClassName);
        setField(pojoGeneratorProperties, "typeMappings", typeMappings);

        return pojoGeneratorProperties;
    }

    /**
     * sets value of the field by reflection
     *
     * @param object     - object to modify
     * @param fieldName  - name of the field belonging to the object
     * @param fieldValue - value of the field to be set
     * @throws IllegalAccessException if unable to access field
     */
    public static void setField(final Object object, final String fieldName, final Object fieldValue)
            throws IllegalAccessException {
        final Field field = fieldOf(object.getClass(), fieldName);
        field.setAccessible(true);
        field.set(object, fieldValue);
    }

    /**
     * Searches for a field in the given class by reflection
     *
     * @param clazz     - class type
     * @param fieldName - name of field in class
     * @return - field belonging to the given clazz with the given fieldName
     */
    public static Field fieldOf(final Class<?> clazz, final String fieldName) {
        final Optional<Field> field = Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.getName().equals(fieldName))
                .findFirst();
        assertTrue(field.isPresent());
        return field.get();
    }
}
