package webserver.http.request;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import http.ContentType;
import webserver.exception.InternalServerErrorException;

import java.io.IOException;
import java.lang.reflect.Type;

public class JsonRequestBodyResolver implements RequestBodyResolver {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(ContentType contentType, Type type) {
        return contentType.equals(ContentType.APPLICATION_JSON);
    }

    @Override
    public Object resolveRequestBody(byte[] requestBody, Type type) {
        try {
            JavaType javaType = objectMapper.constructType(type);
            return objectMapper.readValue(requestBody, javaType);
        } catch (IOException e){
            e.printStackTrace();
            throw new InternalServerErrorException(e.getClass().getName(), e);
        }
    }
}
