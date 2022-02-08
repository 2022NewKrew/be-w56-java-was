package webserver.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Url {
    private String key;
    private String nextUrl;

    public Url(String url) {
        Pattern pattern = Pattern.compile("^/[^/]*");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            key = matcher.group().substring(1);
            nextUrl = url.substring(matcher.end());
        }
    }

    public String getKey() {
        return key;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    @Override
    public String toString() {
        return "Url{" +
                "key='" + key + '\'' +
                ", nextUrl='" + nextUrl + '\'' +
                '}';
    }
}
