package webserver.handler.mappingValidator;

import app.annotation.PostMapping;
import app.http.HttpRequest;

import java.lang.reflect.Method;

public class PostMappingValidator implements MappingValidator {
    private static final PostMappingValidator postMappingValidator = new PostMappingValidator();

    public static PostMappingValidator getInstance() {
        return postMappingValidator;
    }

    private PostMappingValidator() {}

    @Override
    public boolean validateURL(HttpRequest request, Method method) {
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        return postMapping.value().equals(request.getUrl());
    }
}
