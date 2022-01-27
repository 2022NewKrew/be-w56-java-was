package webserver.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import webserver.exception.JsonParsingException;

import java.io.IOException;

/**
 * Singleton
 */
public class JsonParser {

    private static final JsonParser INSTANCE = new JsonParser();

    public static JsonParser getInstance() {
        return INSTANCE;
    }

    private JsonParser() {
    }

    private ObjectMapper mapper = new ObjectMapper();

    public String objectToJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new JsonParsingException();
        }
    }
}
