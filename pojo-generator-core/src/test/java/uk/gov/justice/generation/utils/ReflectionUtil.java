package uk.gov.justice.generation.utils;

import static java.util.Arrays.stream;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public class ReflectionUtil {

    private ReflectionUtil() {
    }

    /**
     * Sets value of the field by reflection
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
     * @param object          - object to find value from
     * @param fieldName       - name of field in class
     * @param returnClassType - defines the return field value class type
     * @param <T>             the class type to return
     * @return the field value returned cast to the class type defined by the returnClassType
     */
    @SuppressWarnings("unchecked")
    public static <T> T fieldValue(final Object object, final String fieldName, final Class<T> returnClassType) throws Exception {
        return returnClassType.cast(fieldValue(object, fieldName));
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
     * @param classWithField - class type
     * @param fieldName      - name of field in class
     * @return - field belonging to the given classWithField with the given fieldName
     */
    public static Field fieldOf(final Class<?> classWithField, final String fieldName) {
        final Optional<Field> field = stream(classWithField.getDeclaredFields()).filter(f -> f.getName().equals(fieldName))
                .findFirst();
        assertTrue(field.isPresent());
        return field.get();
    }

    /**
     * Get the method of a Class with the given annotation.  Returns Optional.empty() if no method
     * found.
     *
     * @param classWithMethod - the Class with the annotated method
     * @param annotationClass - the Annotation Class to find
     * @return the Method with the given annotation
     */
    public static Optional<Method> annotatedMethod(final Class<?> classWithMethod, final Class<? extends Annotation> annotationClass) {
        return stream(classWithMethod.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .findFirst();
    }
}
