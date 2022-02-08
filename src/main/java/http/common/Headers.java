package http.common;

import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import http.response.Cookie;

import java.util.List;

public class Headers {

    private final List<HttpHeader> httpHeaders;

    private Headers(List<HttpHeader> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public static Headers empty() {
        return new Headers(Lists.newArrayList());
    }

    public static Headers withBodyOf(int length, Mime mime) {
        List<HttpHeader> httpHeaders = Lists.newArrayList();
        httpHeaders.add(new HttpHeader(HttpHeaders.CONTENT_TYPE, mime.getContentType() + ";charset=utf-8"));
        httpHeaders.add(new HttpHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(length)));
        return new Headers(httpHeaders);
    }

    public static Headers redirection(String uri) {
        List<HttpHeader> httpHeaders = Lists.newArrayList();
        httpHeaders.add(new HttpHeader(HttpHeaders.LOCATION, uri));
        return new Headers(httpHeaders);
    }

    public String getHeader(String name) {
        return httpHeaders.stream()
                .filter(httpHeader -> httpHeader.isNameMatched(name))
                .findAny()
                .map(HttpHeader::getValue)
                .orElse(null);
    }

    public void addHeader(HttpHeader httpHeader) {
        httpHeaders.add(httpHeader);
    }

    public void addCookies(List<Cookie> cookies) {
        cookies.stream()
                .map(HttpHeader::fromCookie)
                .forEach(httpHeaders::add);
    }

    @Override
    public String toString() {
        return "Headers{" +
                "headers=" + httpHeaders +
                '}';
    }

    public String getFormattedHeader() {
        StringBuilder sb = new StringBuilder();
        httpHeaders.forEach(httpHeader -> sb.append(httpHeader.getFormattedHeader()));
        return sb.toString();
    }
}
