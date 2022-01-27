package util.request;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import static java.util.stream.Collectors.toMap;

class HttpRequestParser {
    public static MethodType parsingMethod(String requestLine){
        return MethodType.of(requestLine.split(" ")[0]);
    }

    public static String parsingUrl(String requestLine){
        return requestLine.split(" ")[1].split("\\?")[0];
    }

    public static Map<String, String> parsingQueryParams(String requestLine){
        String[] urlSplit = requestLine.split(" ")[1].split("\\?");

        if(urlSplit.length == 1){
            return Collections.emptyMap();
        }
        return parseQueryString(urlSplit[1]);
    }

    static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    public static HttpVersion parseHttpVersion(String requestLine){
        return HttpVersion.of(requestLine.split(" ")[2]);
    }

    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    public static Map<String, String> parsingBodyParams(String body){
        if(Strings.isNullOrEmpty(body)){
            return Collections.emptyMap();
        }

        return Arrays.stream(body.split("&"))
                .map(parameter -> {
                    String[] split = parameter.split("=");
                    return Map.entry(split[0], split[1]);})
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
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
