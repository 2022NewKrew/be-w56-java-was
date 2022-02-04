package http.request;

import com.google.common.base.Strings;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestHeader {
    private final Map<String, String> header;
    private final Map<String, String> cookie;

    public HttpRequestHeader(Map<String, String> header, Map<String, String> cookie) {
        this.header = header;
        this.cookie = cookie;
    }

    public static HttpRequestHeader parseHeader(final List<String> headerString) {
        Map<String, String> header = new HashMap<>();
        Map<String, String> cookie;

        for (String h : headerString) {
            String[] tokens = h.split(": ");

            if (tokens.length != 2 || Strings.isNullOrEmpty(tokens[0]) || Strings.isNullOrEmpty(tokens[1]))
                continue;

            header.put(tokens[0], tokens[1]);
        }
        cookie = HttpRequestUtils.parseCookies(header.get("Cookie"));

        return new HttpRequestHeader(header, cookie);
    }

    public String getIfPresent(String key) {
        return header.getOrDefault(key, "");
    }

    public String getCookie(String key) {
        return cookie.getOrDefault(key, "");
    }
}
