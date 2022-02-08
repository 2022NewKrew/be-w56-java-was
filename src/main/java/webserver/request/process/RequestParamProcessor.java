package webserver.request.process;

import annotation.RequestParam;
import webserver.http.request.HttpRequest;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestParamProcessor {

    public static List<Object> process(HttpRequest httpRequest, Parameter parameter) {
        List<Object> objects = new ArrayList<>();
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        if(Objects.isNull(requestParam)) {
            return objects;
        }
        String value = httpRequest.getQueryAttribute(requestParam.name());
        if (value != null) {
            objects.add(value);
        }
        return objects;
    }
}
