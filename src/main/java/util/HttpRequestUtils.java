package util;

import static util.Constant.BLANK;
import static util.Constant.CONTENT_LENGTH;
import static util.Constant.EMPTY;
import static util.Constant.QUESTION_MARK;
import static util.Constant.REDIRECT;
import static util.Constant.ZERO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import app.http.HttpMethod;
import app.http.HttpRequest;
import app.http.HttpRequestBody;
import app.http.HttpRequestParams;
import app.http.HttpVersion;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);
    public static String parseRedirect(String url) {
        if(url.contains(REDIRECT)) {
            url = url.replace(REDIRECT, EMPTY);
        }
        return url;
    }

    public static Map<String, String> parseParams(String[] targetTokens) {
        if(targetTokens.length > 1) {
            return parseQueryString(targetTokens[1]);
        }
        return null;
    }

    public static String[] parseTarget(String target) {
        String[] targetTokens = target.split(QUESTION_MARK);
        return targetTokens;
    }

    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return Pair.of(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static HttpRequest parseInput(InputStream in) throws IOException {
        HttpRequest httpRequest = HttpRequest.of();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String requestLine = br.readLine();
        log.debug("request 내용: {}", requestLine);
        String[] tokens = requestLine.split(BLANK);
        httpRequest.setMethod(HttpMethod.valueOf(tokens[0]));
        String[] targetTokens = parseTarget(tokens[1]);
        httpRequest.setUrl(targetTokens[0]);
        httpRequest.setParams(HttpRequestParams.of(parseParams(targetTokens)));
        httpRequest.setVersion(HttpVersion.from(tokens[2]));

        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            Pair pair = parseHeader(line);
            httpRequest.put(pair.getKey(), pair.getValue());
            log.debug("request 내용: {}", line);
        }
        int length = Integer.parseInt(httpRequest.get(CONTENT_LENGTH, ZERO));
        HttpRequestBody body = HttpRequestBody.of(parseQueryString(IOUtils.readData(br, length)));
        httpRequest.setBody(body);
        return httpRequest;
    }

    public static class Pair {
        String key;
        String value;

        public static Pair of(String key, String value) {
            return new Pair(key, value);
        }
        private Pair(String key, String value) {
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
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
