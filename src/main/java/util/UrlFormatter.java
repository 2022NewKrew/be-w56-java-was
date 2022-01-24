package util;

import webserver.config.WebConst;

public class UrlFormatter {
    public static String format(String url) {
        if(!url.startsWith(WebConst.URL_PREFIX)) {
            url = WebConst.URL_PREFIX + url;
        }
        if(!url.endsWith(".html")) {
            url = url + ".html";
        }
        return url;
    }
}
