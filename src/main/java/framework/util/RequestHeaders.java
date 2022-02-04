package framework.util;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 요청 헤더 데이터들을 담을 일급 컬렉션 클래스
 */
public class RequestHeaders {
    private final Map<String, Object> requestHeaders = new HashMap<>();

    public Object getRequestHeader(String key) {
        return requestHeaders.get(key);
    }

    public void setRequestHeader(String key, Object value) {
        requestHeaders.put(key, value);
    }

    public boolean contains(String key) {
        return requestHeaders.containsKey(key);
    }

    public void parseRequestHeaders(BufferedReader br) throws Exception {
        String line;

        while (!(line = br.readLine()).equals("")) {
            String[] splited = line.split(": ");
            String key = splited[0];
            String value = splited[1];

            if ("Accept".equals(key)
                    || "Accept-Language".equals(key)
                    || "Accept-Encoding".equals(key)) {
                requestHeaders.put(key, new CommaSeries(value));
                continue;
            }

            if ("Content-Length".equals(key)) {
                requestHeaders.put(key, Integer.parseInt(value));
                continue;
            }

            if ("Cookie".equals(key)) {
                Cookies cookies = new Cookies();
                cookies.parseCookies(value);
                requestHeaders.put(key, cookies);
                continue;
            }

            requestHeaders.put(key, value);
        }
    }

    public void clear() {
        requestHeaders.clear();
    }
}
