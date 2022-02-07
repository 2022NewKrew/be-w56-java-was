package webserver.context;

import webserver.annotations.DeleteMapping;
import webserver.annotations.GetMapping;
import webserver.annotations.PostMapping;
import webserver.annotations.PutMapping;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Url {
    public enum HttpMethod {GET("GET", GetMapping.class),POST("POST", PostMapping.class),PUT("PUT", PutMapping.class),DELETE("DELETE", DeleteMapping.class);
        String name;
        Class<?> annotation;
        HttpMethod(String name, Class<?> annotation) {
            this.name = name;
            this.annotation = annotation;
        }
        public static HttpMethod valueOf(Class<?> annotation) {
            for (HttpMethod httpMethod : values()) {
                if (httpMethod.annotation.equals(annotation)) return httpMethod;
            }
            return null;
        }
    };

    private final HttpMethod httpMethod;

    private final String url;

    private final List<String> tokens;

    private final Map<Integer,String> pathVarIndex;

    private final int fixedHashCode;

    private final boolean isPattern;

    private Url(HttpMethod httpMethod, String url, List<String> tokens, Map<Integer,String> pathVarIndex, int fixedHashCode, boolean isPattern) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.tokens = tokens;
        this.pathVarIndex = pathVarIndex;
        this.fixedHashCode = fixedHashCode;
        this.isPattern = isPattern;
    }

    public static Url of(HttpMethod httpMethod, String url, boolean isPattern) {
        List<String> patterns = List.of(url.split("/"));
        Map<Integer,String> pathVarIndex = isPattern?createPathVarIndex(patterns):null;
        int fixedHashCode = createFixedHashCode(httpMethod);
        return new Url(httpMethod, url, patterns, pathVarIndex, fixedHashCode, isPattern);
    }

    private static Map<Integer,String> createPathVarIndex(List<String> tokens) {
        Map<Integer,String> pathVarIndex = new HashMap<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.matches("\\{.+\\}")) {
                pathVarIndex.put(i, token.substring(1,token.length()-1));
            }
        }
        return Collections.unmodifiableMap(pathVarIndex);
    }

    private static int createFixedHashCode(HttpMethod httpMethod) {
        long hashCode = httpMethod.hashCode();
        return (int)hashCode;
    }

    public Map<String,String> getValidatedPathVar(Url other) {
        if (!equals(other)) throw new IllegalArgumentException("Invalid Url!");
        Map<String,String> pathVar = new HashMap<>();
        List<String> tokens = other.getTokens();
        for (int i : pathVarIndex.keySet()) {
            pathVar.put(pathVarIndex.get(i), tokens.get(i));
        }
        return pathVar;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public Map<Integer, String> getPathVarIndex() {
        return pathVarIndex;
    }

    public boolean isPattern() {
        return isPattern;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Url my = ((Url) o).isPattern() ? (Url) o : this;
        Url other = ((Url) o).isPattern() ? this : (Url) o;
        if (!getHttpMethod().equals(other.getHttpMethod())) return false;
        List<String> myTokens = my.getTokens();
        Map<Integer, String> pathVarIndex = my.getPathVarIndex();
        List<String> otherTokens = other.getTokens();
        if (myTokens.size() != otherTokens.size()) return false;
        for (int i = 0; i < myTokens.size(); i++) {
            if (pathVarIndex.containsKey(i)) continue;
            String pattern = myTokens.get(i);
            if (!pattern.equals(otherTokens.get(i))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.fixedHashCode;
    }

}
