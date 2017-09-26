package uk.gov.justice.services.test.utils.core.reflection;


import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    /**
     * @param classWithMethods - class type
     * @return - list of methods of the classWithMethods
     */
    public static List<Method> methodsOf(final Class<?> classWithMethods) {
        return stream(classWithMethods.getDeclaredMethods()).filter(m -> !m.getName().contains("jacoco") && !m.getName().contains("lambda"))
                .sorted(comparing(Method::getName))
                .collect(toList());
    }

    /**
     * returns first method of the given class
     *
     * @param classWithMethod - class type
     * @return - first method of the given classWithMethod
     */
    public static Optional<Method> firstMethodOf(final Class<?> classWithMethod) {
        final List<Method> methods = methodsOf(classWithMethod);

        return methods.stream().findFirst();
    }

    /**
     * Returns method of the given class with the given name
     *
     * @param classWithMethod - class type
     * @param methodName      - name of method in class
     * @return -  method of the given class with the given name
     */
    public static Optional<Method> methodOf(final Class<?> classWithMethod, final String methodName) {
        return methodsOf(classWithMethod).stream()
                .filter(method -> method.getName().equals(methodName))
                .findFirst();
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
        final Optional<Field> field = fieldOf(object.getClass(), fieldName);

        if (field.isPresent()) {
            field.get().setAccessible(true);
            field.get().set(object, fieldValue);
        }
    }

    /**
     * Searches for a field in the given class by reflection
     *
     * @param classWithField - class type
     * @param fieldName      - name of field in class
     * @return - field belonging to the given classWithField with the given fieldName
     */
    public static Optional<Field> fieldOf(final Class<?> classWithField, final String fieldName) {
        return stream(classWithField.getDeclaredFields())
                .filter(f -> f.getName().equals(fieldName))
                .findFirst();
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
    public static <T> Optional<T> fieldValueAs(final Object object,
                                               final String fieldName,
                                               final Class<T> returnClassType) throws IllegalAccessException {
        return fieldValue(object, fieldName)
                .map(returnClassType::cast);
    }

    /**
     * Get the value of a field from an Object
     *
     * @param object    - object to find value from
     * @param fieldName - name of field in class
     * @return - field belonging to the given clazz with the given fieldName
     */
    public static Optional<Object> fieldValue(final Object object, final String fieldName) throws IllegalAccessException {
        final Optional<Field> field = fieldOf(object.getClass(), fieldName);

        if (field.isPresent()) {
            field.get().setAccessible(true);

            return Optional.ofNullable(field.get().get(object));
        }

        return Optional.empty();
    }
}
