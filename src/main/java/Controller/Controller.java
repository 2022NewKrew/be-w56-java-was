package Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface Controller{
    Function<Map<String,String>, Map<String, Object>> decideMethod(String method, String url);

    default Map<String, String> parseUrlInfo(String url, Map<String, String> message){
        String[] split = url.split("\\?");
        if(split.length == 1)
            return message;

        url = split[1];

        for(String data: url.split("&")){
            message.put(data.split("=")[0], data.split("=")[1]);
        }

        return message;
    }

    default Map<String, Object> run(String method, String url, Map<String, String> message){
        if(decideMethod(method, url) == null){
            Map<String, Object> map = new HashMap<>();
            map.put("name", "/404_error.html");

            return map;
        }
        return decideMethod(method, url).apply(parseUrlInfo(url, message));
    }
}
