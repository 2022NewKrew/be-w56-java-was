package was.http;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.meta.HttpHeader;
import was.meta.HttpVersion;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpMapper {
    public static Logger log = LoggerFactory.getLogger(HttpMapper.class);

    public static HttpRequest toHttpRequest(String rawRequest) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(rawRequest));

            String[] requestLine = bufferedReader.readLine().split(" ");

            StringBuilder headerStringBuilder = new StringBuilder();
            String headerLine = bufferedReader.readLine();
            while (headerLine.length() > 0) {
                headerStringBuilder.append(headerLine).append("\r\n");
                headerLine = bufferedReader.readLine();
            }

            StringBuilder bodyStringBuilder = new StringBuilder();
            String bodyLine = bufferedReader.readLine();
            while (bodyLine != null) {
                bodyStringBuilder.append(bodyLine).append("\r\n");
                bodyLine = bufferedReader.readLine();
            }

            HttpVersion version = HttpVersion.of(requestLine[2]);
            String method = requestLine[0];
            String path = requestLine[1].contains("?") ? requestLine[1].split("\\?")[0] : requestLine[1];
            Map<String, String> queryParams = requestLine[1].contains("?") ? parseQueryString(requestLine[1].split("\\?")[1]) : new HashMap<>();
            Map<String, String> headers = parseHeaders(headerStringBuilder.toString());
            Cookie cookie = headers.containsKey(HttpHeader.COOKIE) ? new Cookie(parseCookies(headers.get(HttpHeader.COOKIE))) : new Cookie();
            String body = bodyStringBuilder.length() > 0 ? bodyStringBuilder.toString() : "";
            Map<String, String> requestParams = body.length() > 0 ? parseQueryString(body) : new HashMap<>();

            return HttpRequest.builder()
                    .version(version)
                    .method(method)
                    .path(path)
                    .queryParams(queryParams)
                    .headers(headers)
                    .cookie(cookie)
                    .body(body)
                    .requestParams(requestParams)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpResponse fromHttpRequest(HttpRequest request) {
        HttpVersion version = request.getVersion();
        return HttpResponse.of(version);
    }

    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "=", "&");
    }

    public static Map<String, String> parseHeaders(String headerString) {
        return parseValues(headerString, ": ", "\r\n");
    }

    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, "=", ";");
    }

    public static Map<String, String> parseValues(String values, String tokenDelimiter, String lineDelimiter) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(lineDelimiter);
        return Arrays.stream(tokens)
                .map(token -> getKeyValue(token, tokenDelimiter))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (oldPair, newPair) -> oldPair));
    }

    public static Pair getKeyValue(String token, String regex) {
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }

        String[] tokens = token.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                return other.value == null;
            } else return value.equals(other.value);
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
