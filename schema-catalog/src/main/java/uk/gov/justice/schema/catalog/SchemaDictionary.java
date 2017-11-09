package uk.gov.justice.schema.catalog;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import org.everit.json.schema.Schema;

@ApplicationScoped
public class SchemaDictionary {


    private Map<String, Schema> schemas = new ConcurrentHashMap<String, Schema>();


    public boolean contains(String id) {
        return this.schemas.containsKey(id);
    }

    public Schema getSchema(String id) {
        return this.schemas.get(id);
    }

    public void putSchema(String id, Schema schema) {
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
