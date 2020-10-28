package uk.gov.justice.services.common.util;

import java.util.function.Supplier;

/**
 * Threadsafe class to allow automatic caching and lazy fetching of values. The value is
 * retrieved only once by the Supplier and cached for future access.
 *
 * Usage:
 *
 *     final StringProducer stringProducer = ...
 *
 *     final LazyValue lazyValue = new LazyValue();
 *
 *     final String someString = lazyValue.get(stringProducer::getString)
 */
public class LazyValue {

    private static final Object LOCK = new Object();

    /**
     * The lazily fetched value. Volatile for thead safety reasons
     */
    private volatile Object value;

    /**
     * Gets a value using a Supplier and then caches for future use. This method is fully thread
     * safe
     *
     * @param supplier The java Supplier
     * @param <T> The Type of the value
     * @return The value using the Supplier if not already cached
     */
    @SuppressWarnings("unchecked")
    public <T> T createIfAbsent(final Supplier<T> supplier) {

        Object localRef = value;
        if (localRef == null) {
            synchronized (LOCK) {
                localRef = value;
                if (localRef == null) {
                    value = localRef = supplier.get();
                }
            }
        }

        return (T) localRef;
    }
}
