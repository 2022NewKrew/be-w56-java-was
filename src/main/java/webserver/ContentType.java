package webserver;

import java.util.Arrays;

public enum ContentType {
    HTML("text/html"),
    CSS("text/css"),
    JS("application/javascript"),
    JSON("application/json"),
    ICO("image/x-icon");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public static ContentType matching(String extension) {
        if(extension.equals("html")) {
            return HTML;
        }
        if(extension.equals("css")) {
            return CSS;
        }
        if(extension.equals("js")) {
            return JS;
        }
        if(extension.equals("ico")) {
            return ICO;
        }
        return JSON;
    }

    public String getType() {
        return type;
    }
}
