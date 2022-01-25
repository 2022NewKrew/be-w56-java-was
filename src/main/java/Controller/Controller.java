package Controller;

import java.util.Map;
import java.util.function.Function;

public interface Controller{
    Map<String, String> parseUrlInfo(String url, Map<String, String> message);
    Function<Map<String,String>, Map<String, Object>> decideMethod(String method, String url);

    default Map<String, Object> run(String method, String url, Map<String, String> message){
        return decideMethod(method, url).apply(parseUrlInfo(url, message));
    }
}
