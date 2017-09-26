package uk.gov.justice.generation.utils;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class ReflectionUtil {

    private ReflectionUtil() {
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
     * Get the value of a field from an Object
     *
     * @param object    - object to find value from
     * @param fieldName - name of field in class
     * @return - field belonging to the given clazz with the given fieldName
     */
    public static Object fieldValue(final Object object, final String fieldName) throws IllegalAccessException {
        final Field field = fieldOf(object.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(object);
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
