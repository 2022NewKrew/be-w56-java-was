package webserver.handler.mappingValidator;

import app.annotation.GetMapping;
import app.http.HttpRequest;

import java.lang.reflect.Method;

public class GetMappingValidator implements MappingValidator {
    private static final GetMappingValidator getMappingValidator = new GetMappingValidator();

    public static GetMappingValidator getInstance() {
        return getMappingValidator;
    }

    private GetMappingValidator() {}

    @Override
    public boolean validateURL(HttpRequest request, Method method) {
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        return getMapping.value().equals(request.getUrl());
    }
}
