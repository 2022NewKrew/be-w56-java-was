package Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ErrorController implements Controller{
    private final Map<String, Function<Map<String, String>, Map<String, Object>>> methodMapper;

    public ErrorController(){
        methodMapper = new HashMap<>();

        methodMapper.put("GET /error", this::notFoundError);
        methodMapper.put("POST /error", this::notFoundError);
    }

    private Map<String, Object> notFoundError(Map<String, String> model){
        Map<String, Object> result = new HashMap<>();

        result.put("name", "/404_error.html");

        return result;
    }

    @Override
    public Map<String, String> parseUrlInfo(String url, Map<String, String> message) {
        return message;
    }

    @Override
    public Function<Map<String, String>, Map<String, Object>> decideMethod(String method, String url) {
        url = method + " /error";

        return methodMapper.get(url);
    }
}
