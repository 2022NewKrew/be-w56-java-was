package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final String body;

    public Request(String method, String path, String version, Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getBody() {
        return body;
    }

    public static Request parse(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();
        String[] split = line.split(" ");
        String method = split[0];
        String path = split[1];
        String version = split[2];
        Map<String, String> headers = parseHeaders(br);
        String body = parseBody(headers, br);
        return new Request(method, path, version, headers, body);
    }

    private static Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        Map<String, String> out = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] split = line.split(":");
            String key = split[0];
            String[] subarray = Arrays.copyOfRange(split, 1, split.length);
            String value = String.join(":", subarray)
                    .replaceFirst("^\\s+", "");
            out.put(key, value);
        }
        return out;
    }

    private static String parseBody(Map<String, String> headers, BufferedReader br) throws IOException {
        String contentLength = headers.get("Content-Length");
        if (contentLength == null) {
            return "";
        }
        int length = Integer.parseInt(contentLength);
        char[] chars = new char[length];
        br.read(chars);
        return String.valueOf(chars);
    }
}
