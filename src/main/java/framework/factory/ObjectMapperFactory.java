package framework.factory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
