package Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RootController implements Controller{
    private final Map<String, Function<Map<String, String>, Map<String, Object>>> methodMapper;

    public RootController(){
        methodMapper = new HashMap<>();

        methodMapper.put("GET /", this::index);
        methodMapper.put("GET /index.html", this::index);
    }

    private Map<String, Object> index(Map<String, String> model){
        Map<String, Object> result = new HashMap<>();

        result.put("name", "/index.html");

        return result;
    }

    @Override
    public Function<Map<String, String>, Map<String, Object>> decideMethod(String method, String url) {
        url = method + " " + url;

        return methodMapper.get(url);
    }
}
