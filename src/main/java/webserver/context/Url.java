package webserver.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Url {
    public enum Method {GET("GET"),POST("POST"),PUT("POST"),DELETE("DELETE");
        String name;
        Method(String name) {
            this.name = name;
        }
    };

    private final Method method;

    private final String url;

    private final List<String> patterns;

    private final Map<Integer,String> pathVarIndex;

    private final int fixedHashCode;

    private Url(Method method, String url) {
        this.method = method;
        this.url = url;
        this.patterns = List.of(url.split("/"));
        this.pathVarIndex = createPathVarIndex(this.patterns);
        this.fixedHashCode = createFixedHashCode(this.patterns);
    }

    public static Url of(Method method, String url) {
        return new Url(method, url);
    }

    private Map<Integer,String> createPathVarIndex(List<String> patterns) {
        Map<Integer,String> pathVarIndex = new HashMap<>();
        for (int i = 0; i < patterns.size(); i++) {
            String pattern = patterns.get(i);
            if (pattern.matches("\\{.+\\}")) {
                pathVarIndex.put(i, pattern.substring(1,pattern.length()-1));
            }
        }
        return Collections.unmodifiableMap(pathVarIndex);
    }

    private int createFixedHashCode(List<String> patterns) {
        int hashCode = 0;
        for (int i = 0; i < patterns.size(); i++) {
            if (pathVarIndex.containsKey(i)) continue;
            String pattern = patterns.get(i);
            hashCode = (31 * hashCode + pattern.hashCode())%1000000007;
        }
        return hashCode;
    }

    public Map<String,String> getValidatedPathVar(Url other) {
        if (!equals(other)) throw new IllegalArgumentException("Invalid Url!");
        Map<String,String> pathVar = new HashMap<>();
        List<String> patterns = other.getPatterns();
        for (int i : pathVarIndex.keySet()) {
            pathVar.put(pathVarIndex.get(i), patterns.get(i));
        }
        return pathVar;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public Map<Integer, String> getPathVarIndex() {
        return pathVarIndex;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Url other = (Url)o;
        List<String> splitedUrl = other.getPatterns();
        if (this.patterns.size() != splitedUrl.size()) return false;
        for (int i = 0; i < this.patterns.size(); i++) {
            if (pathVarIndex.containsKey(i)) continue;
            String pattern = this.patterns.get(i);
            if (!pattern.matches(splitedUrl.get(i))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.fixedHashCode;
    }

}
