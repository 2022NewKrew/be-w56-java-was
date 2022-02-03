package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import http.Response;

public final class ResponseGenerator {
    private static final String HTTP_REQUEST_200 = "HTTP/1.1 200 OK ";
    private static final String HTTP_REQUEST_302 = "HTTP/1.1 302 OK ";
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String SET_COOKIE = "Set-Cookie: ";
    private static final String CHARSET = "charset=utf-8";
    private static final String NEW_LINE = "\r\n";

    private static final String KEY_VALUE_SEPARATOR = "\\?";

    private static final String SOURCE_ROOT = "webapp";

    private static final String EXTENSION_HTML = "html";
    private static final String EXTENSION_CSS = "css";
    private static final String EXTENSION_JS = "js";

    private static final String CONTENT_TYPE_JS = "application/js;";
    private static final String CONTENT_TYPE_HTML = "text/html;";
    private static final String CONTENT_TYPE_CSS = "text/css;";
    private static final String CONTENT_TYPE_REDIRECT = "/";

    private ResponseGenerator() {}

    public static byte[] responseToBytes(String fullUrl, Response response) {
        String url = fullUrl.split(KEY_VALUE_SEPARATOR)[0];
        String cookie = response.getCookie();

        byte[] body = getBody(url);
        byte[] header = getHeader(url, body.length, cookie);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(header);
            outputStream.write(body);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
        return outputStream.toByteArray();
    }

    private static byte[] getHeader(String url, int contentLength, String cookie) {
        StringBuilder stringBuilder = new StringBuilder();
        String contentType = getContentType(url);

        String requstType = HTTP_REQUEST_200;
        if (contentType.equals(CONTENT_TYPE_REDIRECT)) {
            requstType = HTTP_REQUEST_302;
        }

        stringBuilder.append(requstType).append(NEW_LINE)
                     .append(CONTENT_TYPE).append(contentType).append(NEW_LINE);
        if (cookie.length() > 1) {
            stringBuilder.append(SET_COOKIE).append(cookie).append(NEW_LINE);
        }
        stringBuilder.append(CHARSET).append(NEW_LINE)
                     .append(CONTENT_LENGTH).append(contentLength).append(NEW_LINE).append(NEW_LINE);

        return stringBuilder.toString().getBytes();
    }

    private static byte[] getBody(String url) {
        try {
            return Files.readAllBytes(new File(SOURCE_ROOT + url).toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static String getContentType(String url) {
        if (url.contains(EXTENSION_JS)) {
            return CONTENT_TYPE_JS;
        }
        if (url.contains(EXTENSION_CSS)) {
            return CONTENT_TYPE_CSS;
        }
        if (url.contains(EXTENSION_HTML)) {
            return CONTENT_TYPE_HTML;
        }
        return CONTENT_TYPE_REDIRECT;
    }
}
