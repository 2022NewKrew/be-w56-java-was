package http;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Request {
    private List<String> requestHeader;
    private List<String> requestBody;
    private String path;
    private HttpMethod method;
    private Map<String, String> elements;
    private HttpCookie cookie;

    public Request(InputStream in) throws IOException {
        String[] requestString = RequestParser.inputStreamToString(in).split("START_BODY");

        this.requestHeader = Arrays.asList(requestString[0].split("\n"));
        this.requestBody = (requestString.length > 1 ? new ArrayList<>(Arrays.asList(requestString[1])) : new ArrayList<>(Arrays.asList("")));
        this.elements = new HashMap<>();

        this.path = RequestParser.parsePath(this);
        this.method = RequestParser.parseMethod(this);
        this.elements.putAll(RequestParser.parseElementsFromGet(this));
        this.elements.putAll(RequestParser.parseElementsFromPost(this));

        this.cookie = new HttpCookie(this.requestHeader);
    }

    public List<String> getRequestHeader() {
        return requestHeader;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getElements() {
        return elements;
    }

    public List<String> getRequestBody() {
        return requestBody;
    }

    public void setCookieValue(String key, String value) {
        this.cookie.addValue(key, value);
    }

    public String getCookieValue(String key) {
        return this.cookie.getValue(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String line : requestHeader){
            sb.append(line + "\n");
        }
        return sb.toString();
    }
}
