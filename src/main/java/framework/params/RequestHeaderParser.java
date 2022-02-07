package framework.params;

import com.google.common.collect.Maps;
import framework.util.HttpRequestUtils;
import lombok.Getter;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Getter
@ToString
public class RequestHeaderParser {
    private String url;
    private String method;
    private Map<String, String> requestParam;
    private int contentLength;
    private Cookie cookie;

    public void parseHeader(BufferedReader br) throws IOException {
        String[] tokens = br.readLine().split(" ");
        String[] urlTokens = tokens[1].split("\\?");
        url = urlTokens[0];
        method = tokens[0];
        requestParam = getRequestParam(urlTokens);
        setOtherField(br);
    }

    private Map<String, String> getRequestParam(String[] urlTokens) {
        if (urlTokens.length < 2) {
            return Maps.newHashMap();
        }

        String queryString = URLDecoder.decode(urlTokens[1], StandardCharsets.UTF_8);
        return HttpRequestUtils.parseQueryString(queryString);
    }

    private void setOtherField(BufferedReader br) throws IOException {
        String line;
        while (!"".equals(line = br.readLine())) {
            if (line == null) break;
            if (line.contains("Cookie:")) {
                String cookieString = line.split("Cookie: ")[1];
                cookie = parseCookie(cookieString);
            }
            if (line.contains("Content-Length")) {
                contentLength = Integer.parseInt(line.split(": ")[1]);
            }
        }
    }

    private Cookie parseCookie(String cookieString) {
        return new Cookie(HttpRequestUtils.parseCookies(cookieString));
    }
}
