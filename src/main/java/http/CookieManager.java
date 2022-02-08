package http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CookieManager {
    private static String KEY_VALUE_DELIMITER = "=";
    private static Map<String, List<String>> updateBuffer; //response header 에서 추가될 쿠키를 저장.

    static{
        updateBuffer = new ConcurrentHashMap<>();
    }

    public static void addNewCookie(String sessionId, String key, String value){
        List<String> keyValueList = updateBuffer.get(sessionId);
        if(keyValueList == null){
            keyValueList = new ArrayList<>();
            updateBuffer.put(sessionId, keyValueList);
        }

        //add key value.
        keyValueList.add(key + KEY_VALUE_DELIMITER + value);
    }

    public static List<String> getNewCookie(String sessionId){
        List<String> newCookieAttributes = updateBuffer.get(sessionId);

        updateBuffer.put(sessionId, new ArrayList<>());

        if(newCookieAttributes == null){
            return new ArrayList<>();
        }

        return newCookieAttributes;
    }
}
