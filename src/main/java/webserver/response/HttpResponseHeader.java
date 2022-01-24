package webserver.response;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class HttpResponseHeader {

    public static final String KEY_FOR_CONTENT_TYPE = "Content-Type";
    public static final String SEPARATOR_FOR_VALUES = ",";

    private Map<String, String[]> headers;

    public HttpResponseHeader(Map<String, String[]> headers) {
        this.headers = headers;
    }

    public void putHeaderIfAbsent(String key, String[] values) {
        headers.putIfAbsent(key, values);
    }

    public String getHeaders() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : headers.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            sb.append(key + ": ");
            sb.append(String.join(SEPARATOR_FOR_VALUES, values));
            sb.append(StringUtils.CR + StringUtils.LF);
        }
        return sb.toString();
    }
}
