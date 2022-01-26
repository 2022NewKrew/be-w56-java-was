package webserver.domain;

import webserver.util.HttpRequestUtils;
import webserver.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private RequestMethod method;
    private String url;
    private String bodyString;
    private String acceptType;
    private Map<String, String> body;

    public Request(BufferedReader br) throws IOException {
        Map<String, String> headerDatas;

        getMethodUrlQueryParam(br);
        headerDatas = getHeaderDatas(br);
        if (headerDatas.containsKey("Content-Length"))
            bodyString = IOUtils.readData(br, Integer.valueOf(headerDatas.get("Content-Length")));
        if (bodyString != null)
            body = HttpRequestUtils.parseQueryString(bodyString);
    }

    public Request(RequestMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public String getAcceptType() {
        return acceptType;
    }

    private void getMethodUrlQueryParam(BufferedReader br) throws IOException {
        String[] tokens = br.readLine().split(" ");
        method = RequestMethod.valueOf(tokens[0]);
        url = tokens[1];
        acceptType = null;
        bodyString = null;
        if (url.contains("\\?")) {
            bodyString = url.substring(url.indexOf("\\?") + 1);
            url = url.substring(0, url.indexOf("\\?"));
        }
        if (url.contains(".")) {
            acceptType = url.substring(url.lastIndexOf('.') + 1);
        }
    }

    private Map<String, String> getHeaderDatas(BufferedReader br) throws IOException {
        Map<String, String> headerDatas = new HashMap<>();
        String line = br.readLine();
        while (!(line.equals("") || line == null)) {
            String[] tokens = line.split(": ");
            headerDatas.put(tokens[0], tokens[1]);
            line = br.readLine();
        }
        return headerDatas;
    }

    @Override
    public boolean equals(Object o) {
        Request target = (Request) o;
        return (method == target.method) && (url.equals(target.url));
    }

    @Override
    public int hashCode() {
        return 31 * method.hashCode() + url.hashCode();
    }
}
