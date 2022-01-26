package Controller;

import mapper.AssignedModelKey;

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

        String request_url = model.get(AssignedModelKey.REQUEST_URL);

        result.put(AssignedModelKey.NAME, request_url);

        return result;
    }

    @Override
    public Function<Map<String, String>, Map<String, Object>> decideMethod(String method, String url) {
        url = method + " /";

        return methodMapper.get(url);
    }
}
