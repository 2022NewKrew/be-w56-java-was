package app.http;

import app.annotation.DeleteMapping;
import app.annotation.GetMapping;
import app.annotation.PostMapping;
import app.annotation.PutMapping;

import java.lang.annotation.Annotation;

public enum HttpMethod {
    GET(GetMapping.class),
    POST(PostMapping.class),
    PUT(PutMapping.class),
    DELETE(DeleteMapping.class);

    private final Class<? extends Annotation> annotationClass;

    HttpMethod(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public String method() {
        return name();
    }
}
