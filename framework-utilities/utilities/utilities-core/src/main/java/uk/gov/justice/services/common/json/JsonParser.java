package uk.gov.justice.services.common.json;

public interface JsonParser {

    /**
     * Converts from a json String to an Object
     *
     * @param json  the json to parse
     * @param clazz the object to convert to
     * @param <T>   the type of the object
     * @return one freshly parsed pojo
     */
    <T> T toObject(final String json, final Class<T> clazz);

    /**
     * Converts from a pojo to a json String
     *
     * @param object the object to convert. Must be a jackson parsable object
     * @param <T>    the object's type
     * @return the object as json
     */
    <T> String fromObject(final T object);
}
