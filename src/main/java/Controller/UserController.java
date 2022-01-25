package Controller;

import java.util.Map;
import java.util.function.Function;

public class UserController implements Controller{
    @Override
    public Map<String, String> parseUrlInfo(String url, Map<String, String> message) {
        return null;
    }

    @Override
    public Function<Map<String, String>, Map<String, Object>> decideMethod(String method, String url) {
        return null;
    }
}
