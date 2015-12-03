package com.sap.integration.utils;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Utility class used for converting between JSON objects and class objects.
 */
public class JsonUtil {

    private static final Logger LOG = Logger.getLogger(JsonUtil.class);

    /**
     * Method, converts Java object to JSON object.
     * 
     * @param object - object, which will be converted to JSON
     * @return converted Java object as JSON string
     * @throws JsonGenerationException possible exception during the processing
     * @throws JsonMappingException possible exception during the processing
     * @throws IOException possible exception during the processing
     */
    public static final String getJson(final Object object) throws JsonGenerationException, JsonMappingException, IOException {
        return getJson(object, Visibility.DEFAULT);
    }

    /**
     * Method, converts Java object to JSON object.
     * 
     * @param object - object, which will be converted to JSON
     * @param visibility - minimum visibility to require for the property descriptors of type
     * @return converted Java object as JSON string
     * @throws JsonGenerationException possible exception during the processing
     * @throws JsonMappingException possible exception during the processing
     * @throws IOException possible exception during the processing
     */
    public static final String getJson(final Object object, final Visibility visibility) throws JsonGenerationException,
            JsonMappingException, IOException {
        if (object != null && visibility != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            objectMapper.setVisibility(JsonMethod.FIELD, visibility);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } else {
            return null;
        }
    }

    /**
     * Convert JSON object to list of Java objects.
     * 
     * @param content - string content, which will be converted to list of Java objects
     * @param clazz - type of class on which will be objects converted
     * @return List<T> - list of converted Java objects
     * @throws JsonParseException possible exception during the processing
     * @throws JsonMappingException possible exception during the processing
     * @throws IOException possible exception during the processing
     */
    public static final <T> List<T> getObjects(final String content, final Class<T> clazz) throws JsonParseException,
            JsonMappingException, IOException {

        if (content != null && clazz != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            } catch (EOFException e) {
                LOG.warn("JSON converter - failed to parse list of JSON: " + content + " for class: " + clazz.getSimpleName()
                        + " with exception EOFException: " + e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Convert entered string content to entered class.
     * 
     * @param content - content, which will be converted
     * @param clazz - content will be converted to this class
     * @return parsed object with type of entered class
     * @throws JsonParseException possible exception during the processing
     * @throws JsonMappingException possible exception during the processing
     * @throws IOException possible exception during the processing
     */
    public static final <T> T getObject(String content, Class<T> clazz) throws JsonParseException,
            JsonMappingException, IOException {
        if (content != null && clazz != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(content, clazz);
            } catch (EOFException e) {
                LOG.warn("JSON converter - failed to parse JSON: " + content + " for class: " + clazz.getSimpleName()
                        + " with exception EOFException: " + e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }
}
