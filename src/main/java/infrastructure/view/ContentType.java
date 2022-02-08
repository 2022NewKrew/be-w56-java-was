package infrastructure.view;

import http.common.HttpMethod;

public enum ContentType {
    HTML("html", "text/html"),
    ICO("ico", "image/x-icon"),
    JS("js", "text/javascript"),
    CSS("css", "text/css"),
    WOFF("woff", "application/x-font-woff");

    public final String type;
    public final String code;

    ContentType(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public static String findMime(String type){
        try {
            return ContentType.valueOf(type.toUpperCase()).code;
        } catch(IllegalArgumentException e) {
            return "application/octet-stream";
        }
    }
}
