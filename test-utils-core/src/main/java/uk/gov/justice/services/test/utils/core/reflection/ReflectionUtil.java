package uk.gov.justice.services.test.utils.core.reflection;


import static java.lang.String.format;
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
     */
    public static void setField(final Object object, final String fieldName, final Object fieldValue) {

        final Field field = getField(object.getClass(), fieldName);
        try {
            field.setAccessible(true);
            field.set(object, fieldValue);
        } catch (final IllegalAccessException e) {
            throw new ReflectionException(format("Unable to access field '%s' on class %s", fieldName, object.getClass().getName()), e);
        }
    }

    /**
     * Searches for a field in the given class by reflection
     *
     * @param aClass    - class type
     * @param fieldName - name of field in class
     * @return - field belonging to the given classWithField with the given fieldName
     */
    @SuppressWarnings("CatchMayIgnoreException")
    public static Field getField(final Class<?> aClass, final String fieldName) {

        for (Class<?> currentClass = aClass; currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
            if (hasField(fieldName, currentClass)) {
                try {
                    return currentClass.getDeclaredField(fieldName);
                } catch (final NoSuchFieldException thisWillNeverHappen) {
                }
            }
        }

        throw new ReflectionException(format("No field named '%s' found on class %s", fieldName, aClass.getName()));
    }

    /**
     * Get the value of a field from an Object
     *
     * @param object         - object to find value from
     * @param fieldName      - name of field in class
     * @param fieldClassType - defines the return field value class type
     * @param <T>            the class type to return
     * @return the field value returned cast to the fieldClassType
     */
    public static <T> T getValueOfField(
            final Object object,
            final String fieldName,
            final Class<T> fieldClassType) {

        try {
            final Field field = getField(object.getClass(), fieldName);
            field.setAccessible(true);
            return fieldClassType.cast(field.get(object));
        } catch (final IllegalAccessException e) {
            throw new ReflectionException(format("Unable to access field '%s' on class %s", fieldName, object.getClass().getName()), e);
        }
    }

    private static boolean hasField(final String fieldName, final Class<?> aClass) {
        final Field[] declaredFields = aClass.getDeclaredFields();

        if (declaredFields == null) {
            return false;
        }

        return stream(declaredFields)
                .anyMatch(field -> field.getName().equals(fieldName));
    }
}
