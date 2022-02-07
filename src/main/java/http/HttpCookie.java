package http;

import java.util.*;

public class HttpCookie {
    public static String START_DELIMITER = "Cookie: ";
    public static String ATTR_DELIMITER = "; ";
    public static String KEY_VALUE_DELIMITER = "=";
    private Map<String, String> attribute;

    public HttpCookie(List<String> requestHeader){
        this.attribute = new HashMap<>();

        //parse cookies from request.
        for(String lineStr : requestHeader){
            if(lineStr.startsWith(START_DELIMITER)){
                String cookieStr = lineStr.substring(START_DELIMITER.length(), lineStr.length());
                Arrays.asList(cookieStr.split(ATTR_DELIMITER)).stream().forEach(
                        (keyValueStr) -> {String[] keyValue = keyValueStr.split(KEY_VALUE_DELIMITER);
                                            attribute.put(keyValue[0], keyValue[1]);});

                break;
            }
        }

        //set sessionId at the first request.
        if(attribute.get("sessionId") == null){
            String sessionId = UUID.randomUUID().toString();
            attribute.put("sessionId", sessionId);
            CookieManager.addNewCookie(sessionId, "sessionId", sessionId); //response에서 SetCookie에 해당될 요소를 버퍼에 추가.
        }
    }

    public String getValue(String key){
        return attribute.get(key);
    }

    public void addValue(String key, String value){
        attribute.put(key, value);
    }

}
