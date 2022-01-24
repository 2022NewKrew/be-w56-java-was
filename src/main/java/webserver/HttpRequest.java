package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String method; // TODO : enum 클래스로?!
    private final String url;
    private final String version;
    private Map<String, String> headers;

    public HttpRequest(String method, String url, String version) {
        this.method = method;
        this.url = url;
        this.version = version;
        this.headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public static HttpRequest create(BufferedReader br) throws IOException {

        String[] tokens = br.readLine().split(" ");
        HttpRequest httpRequest = new HttpRequest(tokens[0], tokens[1], tokens[2]);

        String line;
        while (!(line = br.readLine()).equals("")) {
            tokens = line.split(":");
            httpRequest.addHeader(tokens[0].trim(), tokens[1].trim());
        }
        return httpRequest;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public String toString() {
        String responseMessage = String.format("%s %s %s \n", method, url, version);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            responseMessage += String.format("%s: %s\n", entry.getKey(), entry.getValue());
        }
        return responseMessage;
    }
}
