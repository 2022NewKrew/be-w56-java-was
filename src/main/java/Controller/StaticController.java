package Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StaticController implements Controller{
    private final Map<String, Function<Map<String, String>, Map<String, Object>>> methodMapper;

    public StaticController(){
        methodMapper = new HashMap<>();

        methodMapper.put("GET /", this::staticFile);

    }

    private Map<String, Object> staticFile(Map<String, String> model){
        Map<String, Object> result = new HashMap<>();

        String request_url = model.get("request_url");

        result.put("name", request_url);

        return result;
    }

    @Override
    public Map<String, String> parseUrlInfo(String url, Map<String, String> message) {
        return message;
    }

    @Override
    public Function<Map<String, String>, Map<String, Object>> decideMethod(String method, String url) {
        url = method + " /";

        return methodMapper.get(url);
    }
}
