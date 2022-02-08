package webserver.web;

import lombok.NoArgsConstructor;
import webserver.Model;
import webserver.web.request.Request;
import webserver.web.response.Response;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class Parameters {

    private final Map<String, Object> parameters = new HashMap<>();

    public Parameters(Request request) {
        parameters.put("request", request);
        parameters.put("builder", new Response.ResponseBuilder());
        parameters.put("model", request.getModel());
        inputData(request.getUrl().getParameters());
        inputData(request.getBody().getBody());
    }

    public void inputData(Map<String, String> data) {
        data.keySet().forEach(key ->
                parameters.put(key, data.get(key)));
    }

    public Object inquireData(String name, Class<?> type) {
        if (type.equals(Model.class)) {
            return parameters.get("model");
        }
        if (type.equals(Response.ResponseBuilder.class)) {
            return parameters.get("builder");
        }
        if (type.equals(Request.class)) {
            return parameters.get("request");
        }
        String data = (String) parameters.get(name);
        if (type.equals(Integer.class)) {
            return Integer.parseInt(data);
        }
        if (type.equals(Long.class)) {
            return Long.parseLong(data);
        }
        return data;
    }

    public Response.ResponseBuilder getResponseBuilder() {
        return (Response.ResponseBuilder) parameters.get("builder");
    }
}
