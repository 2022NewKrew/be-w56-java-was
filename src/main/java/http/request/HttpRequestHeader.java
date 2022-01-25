package http.request;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestHeader {
    private final Map<String, String> header;

    public HttpRequestHeader(Map<String, String> header) {
        this.header = header;
    }

    public static HttpRequestHeader parseHeader(final List<String> headerString) {
        Map<String, String> header = new HashMap<>();

        for (String h : headerString) {
            String[] tokens = h.split(": ");

            if (tokens.length != 2 || Strings.isNullOrEmpty(tokens[0]) || Strings.isNullOrEmpty(tokens[1]))
                continue;

            header.put(tokens[0], tokens[1]);
        }

        return new HttpRequestHeader(header);
    }
}
