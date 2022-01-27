package webserver.handler.mappingValidator;

import app.annotation.DeleteMapping;
import app.http.HttpRequest;

import java.lang.reflect.Method;

public class DeleteMappingValidator implements MappingValidator {
    private static final DeleteMappingValidator deleteMappingValidator = new DeleteMappingValidator();

    public static DeleteMappingValidator getInstance() {
        return deleteMappingValidator;
    }

    private DeleteMappingValidator() {}

    @Override
    public boolean validateURL(HttpRequest request, Method method) {
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        return deleteMapping.value().equals(request.getUrl());
    }
}
