package webserver.handler.mappingValidator;

import app.annotation.PutMapping;
import app.http.HttpRequest;

import java.lang.reflect.Method;

public class PutMappingValidator implements MappingValidator {
    private static final PutMappingValidator putMappingValidator = new PutMappingValidator();

    public static PutMappingValidator getInstance() {
        return putMappingValidator;
    }

    private PutMappingValidator() {}

    @Override
    public boolean validateURL(HttpRequest request, Method method) {
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        return putMapping.value().equals(request.getUrl());
    }
}
