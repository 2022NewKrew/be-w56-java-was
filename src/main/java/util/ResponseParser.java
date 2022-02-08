package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.Response;
import webserver.DynamicHtmlMapper;
import webserver.RequestHandler;

public final class ResponseParser {
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String SET_COOKIE = "Set-Cookie: ";
    private static final String CHARSET = "charset=utf-8";

    private static final String KEY_VALUE_SEPARATOR = "\\?";
    private static final String NEW_LINE = "\r\n";

    private static final String SOURCE_ROOT = "webapp";

    private static final String EXTENSION_HTML = "html";
    private static final String EXTENSION_CSS = "css";
    private static final String EXTENSION_JS = "js";

    private static final String CONTENT_TYPE_JS = "application/js;";
    private static final String CONTENT_TYPE_HTML = "text/html;";
    private static final String CONTENT_TYPE_CSS = "text/css;";
    private static final String CONTENT_TYPE_REDIRECT = "/";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private ResponseParser() {}

    public static byte[] responseToBytes(String fullUrl, Response response) {
        String url = fullUrl.split(KEY_VALUE_SEPARATOR)[0];

        byte[] body = getBody(url, response);
        byte[] header = getHeader(url, body.length, response);

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

    private static byte[] getHeader(String url, Integer contentLength, Response response) {
        StringBuilder stringBuilder = new StringBuilder();

        String cookieString = response.getCookie().toString();

        stringBuilder.append(response.getType()).append(NEW_LINE)
                     .append(CONTENT_TYPE).append(getContentType(url)).append(NEW_LINE);
        if (cookieString.length() > 1) {
            stringBuilder.append(SET_COOKIE).append(cookieString).append(NEW_LINE);
        }
        stringBuilder.append(CHARSET).append(NEW_LINE)
                     .append(CONTENT_LENGTH).append(contentLength).append(NEW_LINE).append(NEW_LINE);

        return stringBuilder.toString().getBytes();
    }

    private static byte[] getBody(String url, Response response) {
        return DynamicHtmlMapper.getMapper(url).getPage(response, url);
    }

    private static String getContentType(String url) {
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
