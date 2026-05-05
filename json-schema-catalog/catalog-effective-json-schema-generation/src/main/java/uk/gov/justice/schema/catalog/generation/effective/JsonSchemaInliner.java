package uk.gov.justice.schema.catalog.generation.effective;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Inlines all external {@code $ref} references in a JSON schema into a single self-contained
 * effective JSON schema document. Referenced schemas are collected in a top-level
 * {@code definitions} block, and all external {@code $ref} values are rewritten to point to
 * those local definitions.
 *
 * <p>Fragment references (e.g. {@code http://example.com/address.json#/definitions/postal})
 * are preserved as a JSON Pointer path beneath the inlined definition entry.
 *
 * <p>Circular references are handled safely: if a schema is encountered while it is already
 * being processed, that particular {@code $ref} is left pointing to the external URL rather
 * than causing infinite recursion.
 */
public class JsonSchemaInliner {

    private final DefinitionNameFactory definitionNameFactory;

    public JsonSchemaInliner(final DefinitionNameFactory definitionNameFactory) {
        this.definitionNameFactory = definitionNameFactory;
    }

    /**
     * Produces an effective JSON schema by recursively inlining all externally referenced schemas.
     *
     * @param schema         the root JSON schema to process
     * @param allSchemasById a map of schema ID to raw JSON string for every known schema
     * @return a new {@link JSONObject} with all reachable external {@code $ref} values inlined
     */
    public JSONObject inline(final JSONObject schema, final Map<String, String> allSchemasById) {
        final Map<String, JSONObject> collectedDefinitions = new LinkedHashMap<>();
        final Set<String> currentlyProcessing = new HashSet<>();

        final JSONObject workingSchema = deepCopy(schema);
        resolveRefsInNode(workingSchema, allSchemasById, collectedDefinitions, currentlyProcessing);

        if (!collectedDefinitions.isEmpty()) {
            final JSONObject mergedDefinitions = workingSchema.has("definitions")
                    ? deepCopy(workingSchema.getJSONObject("definitions"))
                    : new JSONObject();
            collectedDefinitions.forEach(mergedDefinitions::put);
            workingSchema.put("definitions", mergedDefinitions);
        }

        return workingSchema;
    }

    private void resolveRefsInNode(final Object node,
                                   final Map<String, String> allSchemas,
                                   final Map<String, JSONObject> definitions,
                                   final Set<String> processing) {
        if (node instanceof JSONObject obj) {
            if (obj.has("$ref")) {
                final String ref = obj.getString("$ref");
                if (!ref.startsWith("#")) {
                    processExternalRef(obj, ref, allSchemas, definitions, processing);
                }
            } else {
                for (final String key : obj.keySet()) {
                    resolveRefsInNode(obj.get(key), allSchemas, definitions, processing);
                }
            }
        } else if (node instanceof JSONArray arr) {
            for (int i = 0; i < arr.length(); i++) {
                resolveRefsInNode(arr.get(i), allSchemas, definitions, processing);
            }
        }
    }

    private void processExternalRef(final JSONObject refNode,
                                    final String ref,
                                    final Map<String, String> allSchemas,
                                    final Map<String, JSONObject> definitions,
                                    final Set<String> processing) {
        final int hashIdx = ref.indexOf('#');
        final String baseId = hashIdx >= 0 ? ref.substring(0, hashIdx) : ref;
        final String fragment = hashIdx >= 0 ? ref.substring(hashIdx + 1) : "";

        final String defName = definitionNameFactory.createFor(baseId);

        if (!definitions.containsKey(defName) && !processing.contains(baseId)) {
            final String rawSchema = allSchemas.get(baseId);
            if (rawSchema == null) {
                return;
            }
            processing.add(baseId);
            final JSONObject referencedSchema = new JSONObject(rawSchema);
            resolveRefsInNode(referencedSchema, allSchemas, definitions, processing);
            definitions.put(defName, referencedSchema);
            processing.remove(baseId);
        }

        if (definitions.containsKey(defName)) {
            final String localFragment = fragment.replaceFirst("^/", "");
            final String newRef = localFragment.isEmpty()
                    ? "#/definitions/" + defName
                    : "#/definitions/" + defName + "/" + localFragment;
            refNode.put("$ref", newRef);
        }
    }

    private JSONObject deepCopy(final JSONObject original) {
        return new JSONObject(original.toString());
    }
}
