package util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;

public class ObjectMappingUtils {

    private static Gson gson = new Gson();

    public static<T> T mapObject(Map<String, String> map, Class<T> c) {
        JsonElement jsonElement = gson.toJsonTree(map);
        return gson.fromJson(jsonElement, c);
    }
}
