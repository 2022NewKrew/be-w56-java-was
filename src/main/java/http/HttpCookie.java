package http;

import util.HttpCookieUtils;

import java.util.*;

public class HttpCookie {
    private Map<String, String> attribute;

    public HttpCookie(List<String> requestHeader){
        this.attribute = HttpCookieUtils.parseCookie(requestHeader);

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
