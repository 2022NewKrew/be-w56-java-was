package network;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private BufferedReader bufferedReader;
    private Method method;
    private String path;
    private String protocol;
    private Map<String, String> queryString;
    private Map<String, String> header = new HashMap<>();
    private Map<String, String> body;

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        this.bufferedReader = bufferedReader;
        parseQuery();
    }

    private void parseQuery() throws IOException {
        parseFirstHeader();
    }

    private void parseFirstHeader() throws IOException {
        String[] firstLineToken = bufferedReader.readLine().split(" ");
        this.method = Method.valueOf(firstLineToken[0]);
        this.path = firstLineToken[1];
        if (this.path.equals("/")||this.path.equals("")) {
            this.path = "/index.html";
        }
        this.protocol = firstLineToken[2];

        handleMethod(this.method);
    }

    private void handleMethod(Method method) throws IOException {
        switch (method){
            case GET:
                parseHeader();
                break;
            case POST:
                parseHeader();
                parseBody();
                break;
        }
    }
    private void parseHeader() throws IOException {
        String[] token = this.path.split("\\?");
        this.path = token[0];

        if(token.length > 1){
            this.queryString = HttpRequestUtils.parseQueryString(token[1]);
        }

        String line;
        while(!(line = bufferedReader.readLine()).equals("")){
            Pair pair = HttpRequestUtils.parseHeader(line);
            header.put(pair.getKey(), pair.getValue());
        }
    }

    private void parseBody() throws IOException {
        String line = bufferedReader.readLine();
        this.body = HttpRequestUtils.parseQueryString(line);
    }

    public Map<String, String> getQueryString(){
        return queryString;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getBody(){
        return body;
    }

    public Method getMethod() {
        return method;
    }
}
