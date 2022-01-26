package Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface Controller{
    Function<Map<String,String>, Map<String, Object>> decideMethod(String method, String url);

    default Map<String, String> addHttpRequestParamsToModel(String url, Map<String, String> model){
        String[] split = url.split("\\?");
        if(split.length == 1)
            return model;

        url = split[1];

        for(String data: url.split("&")){
            model.put(data.split("=")[0], data.split("=")[1]);
        }

        return model;
    }

    default Map<String, Object> run(String method, String url, Map<String, String> model){
        if(decideMethod(method, url) == null){
            Map<String, Object> map = new HashMap<>();
            map.put("name", "/404_error.html");

            return map;
        }
        return decideMethod(method, url).apply(addHttpRequestParamsToModel(url, model));
    }
}
