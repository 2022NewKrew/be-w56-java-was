package collections;

import util.HttpRequestUtils;

import java.util.Map;

public class RequestStartLine {

    private Map<String, String> startLine;

    public RequestStartLine(Map<String, String> startLine) {
        String path = startLine.get("Path");
        String[] pathWithQuery = path.split("\\?");

        if (pathWithQuery.length == 2) {
            startLine.put("Path", pathWithQuery[0]);
            startLine.put("Parameters", pathWithQuery[1]);
        }

        this.startLine = startLine;
    }

    public String getProtocol() {
        return startLine.get("Protocol");
    }

    public String getMethod() {
        return startLine.get("Method");
    }

    public String getPath() {
        return startLine.get("Path");
    }

    public Map<String, String> getParameters() {
        return HttpRequestUtils.parseValues(startLine.getOrDefault("Parameters", ""), "&");
    }
}
