package webserver.manage;

import webserver.io.RequestIO;
import webserver.io.RequestInput;
import webserver.io.RequestOutput;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private static final String METHOD_TOKEN = " ";
    private static final String REQUEST_TOKEN = ":";
    private static final String PATH_TOKEN = "\\?";
    private static final String QUERY_TOKEN = "&";
    private static final String KEY_VALUE_TOKEN = "=";
    private static final String ACCEPT_TOKEN = ",";

    private static final String KEY_METHOD = "Method";
    private static final String KEY_PATH = "Url-Path";
    private static final String KEY_QUERIES = "Queries";
    private static final String KEY_ACCEPT = "Accept";

    private static final String ROOT_URL_PATH = "/";
    private static final String DEFAULT_URL_PATH = "/index.html";

    Map<String, String> map;
    Map<String, String> queryMap;

    public RequestParser(InputStream is) {
        this.map = new HashMap<>();
        generateRequestMap(is);
        parseRequestQuery();
    }

    void generateRequestMap (InputStream is) {
        RequestOutput ro = new RequestOutput();
        RequestInput ri = new RequestInput(is);

        String requestMethod = ri.readLine(); // 첫 줄
        if(RequestIO.checkStringIsEmpty(requestMethod)) {
            return;
        }
        ro.write(requestMethod);
        parseRequestMethod(requestMethod);

        String requestLine = null;
        while(!RequestIO.checkStringIsEmpty(requestLine=ri.readLine())) {
            ro.write(requestLine);

            String[] input = requestLine.split(REQUEST_TOKEN);
            if(input.length == 2) {
                map.put(input[0].trim(), input[1].trim());
            }
        }
        ro.flush();
    }

    private void parseRequestMethod (String requestMethod) {
        String[] methodAndURL = requestMethod.split(METHOD_TOKEN);
        map.put(KEY_METHOD, methodAndURL[0]);
        parseRequestURL(methodAndURL[1]);
    }

    private void parseRequestURL (String methodAndURL) {
        String[] pathAndQuery = methodAndURL.split(PATH_TOKEN);
        map.put(KEY_PATH, pathAndQuery[0]);
        if(pathAndQuery.length == 2) {
            map.put(KEY_QUERIES, pathAndQuery[1]);
        }
    }

    public String getQuery(String key) {
        return queryMap.get(key);
    }

    private void parseRequestQuery () {
        this.queryMap = new HashMap<>();

        String queries = map.get(KEY_QUERIES);
        if(queries != null) {
            String[] queryList = queries.split(QUERY_TOKEN);
            for(String query : queryList) {
                parseRequestKeyValue(queryMap, query);
            }
        }
    }

    private void parseRequestKeyValue (Map<String,String> queryMap, String query) {
        String[] keyAndValue = query.split(KEY_VALUE_TOKEN);
        if(keyAndValue.length == 2) {
            queryMap.put(keyAndValue[0], keyAndValue[1]);
        }
    }

    public String getMethod () {
        return map.get(KEY_METHOD);
    }

    public String getPath() {
        String path = map.get(KEY_PATH);
        return ROOT_URL_PATH.equals(path) ? DEFAULT_URL_PATH : path;
    }

    public String getContentType () {
        return map.get(KEY_ACCEPT).split(ACCEPT_TOKEN)[0];
    }
}
