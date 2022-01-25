package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 2:48
 */
public enum ContentType {
    HTML("html"),
    CSS("css"),
    JS("js");

    private String contentType;
    private static Map<String, ContentType> contentTypeMap;

    static {
        contentTypeMap = initializeContentTypeMap();
    }

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    private static Map<String, ContentType> initializeContentTypeMap() {
        Map<String, ContentType> contentTypeMap = new HashMap<>();
        ContentType[] contentTypes = values();
        for (ContentType contentType : contentTypes) {
            contentTypeMap.put(contentType.contentType, contentType);
        }
        return contentTypeMap;
    }

    public static ContentType of(String requestType) {
        return contentTypeMap.getOrDefault(requestType, ContentType.HTML);
    }

    public String getContentType() {
        return contentType;
    }
}
