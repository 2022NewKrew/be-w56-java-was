package app.http;

import static util.Constant.BLANK;
import static util.Constant.HEADER_FORMAT;
import static util.Constant.NEW_LINE;
import static util.Constant.SET_COOKIE;

import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpResponse {
    private HttpVersion version;
    private HttpStatus status;
    private HttpHeader header;
    private Cookie cookie;
    private byte[] body;

    public static HttpResponse of() {
        return new HttpResponse();
    }

    private HttpResponse() {
        this.header = HttpHeader.of();
    }

    public String getStatusLine() {
        if(version == null || status == null) {
            return "";
        }
        return version.version() + BLANK + status.status();
    }

    public String get(String key, String defaultValue) {
        return header.get(key, defaultValue);
    }

    public void put(String key, String value) {
        header.put(key, value);
    }

    public void setCookie(String name, String value) {
        this.cookie = Cookie.of();
        cookie.setName(name);
        cookie.setValue(value);
    }

    public int getBodyLength() {
        if(body == null) {
            return 0;
        }
        return body.length;
    }

    public String header() {
        Map<String, String> header = this.header.header();
        if(cookie != null) {
            header.put(SET_COOKIE, cookie.cookieValue());
        }
        return header.entrySet()
                      .stream()
                      .map(entry -> String.format(HEADER_FORMAT, entry.getKey(), entry.getValue()))
                      .collect(Collectors.joining(NEW_LINE)) + NEW_LINE + NEW_LINE;
    }
}
