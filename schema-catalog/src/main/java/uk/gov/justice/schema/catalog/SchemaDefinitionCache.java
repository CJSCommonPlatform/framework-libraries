package uk.gov.justice.schema.catalog;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


class SchemaDefinitionCache {

    private Map<String, String> schemas = new ConcurrentHashMap<String, String>();


    public boolean contains(String id) {
        return this.schemas.containsKey(id);
    }

    public String getSchema(String id) {
        return this.schemas.get(id);
    }

    public void putSchema(String id, String schema) {
        this.schemas.put(id, schema);
    }

    public void clear() {
        this.schemas.clear();
    }

    public Set<String> getIDSet() {
        return this.schemas.keySet();
    }

    public int size()
    {
        return this.schemas.size();
    }
}
