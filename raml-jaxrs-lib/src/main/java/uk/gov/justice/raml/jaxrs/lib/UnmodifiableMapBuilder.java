package uk.gov.justice.raml.jaxrs.lib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UnmodifiableMapBuilder<K, T> {
    private final Map<K, T> map = new HashMap<>();

    public UnmodifiableMapBuilder<K, T> with(final K key, final T value) {
        map.put(key, value);
        return this;
    }

    public Map<K, T> build() {
        return Collections.unmodifiableMap(map);
    }
}
