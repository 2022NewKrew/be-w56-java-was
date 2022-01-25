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
    private static final String COOKIE_TOKEN = ";";

    private static final String KEY_METHOD = "Method";
    private static final String KEY_PATH = "Url-Path";
    private static final String KEY_QUERIES = "Queries";
    private static final String KEY_CONTENT_LENGTH = "Content-Length";
    private static final String KEY_COOKIE = "Cookie";


    private static final String ROOT_URL_PATH = "/";
    private static final String DEFAULT_URL_PATH = "/index.html";

    private static final int MIN_CONTENT_LENGTH = 1;

    private Map<String, String> optionMap;
    private Map<String, String> queryMap;
    private Map<String, String> bodyMap;
    private Map<String, String> cookieMap;

    public RequestParser(InputStream is) {
        this.optionMap = new HashMap<>();
        generateRequestMap(is);
        parseRequestQuery();
        parseRequestCookie();
    }

    private void generateRequestMap (InputStream is) {
        RequestOutput ro = new RequestOutput();
        RequestInput ri = new RequestInput(is);

        // 요청 데이터
        String requestMethod = ri.readLine();
        if(RequestIO.checkStringIsEmpty(requestMethod)) {
            return;
        }
        ro.write(requestMethod);
        parseRequestMethod(requestMethod);

        // Header
        String headerLine = null;
        while(!RequestIO.checkStringIsEmpty(headerLine=ri.readLine())) {
            ro.write(headerLine);

            String[] input = headerLine.split(REQUEST_TOKEN);
            if(input.length == 2) {
                optionMap.put(input[0].trim(), input[1].trim());
            }
        }

        // Body
        int contentLength = Integer.parseInt(optionMap.getOrDefault(KEY_CONTENT_LENGTH, "0"));
        if( contentLength >= MIN_CONTENT_LENGTH ) {
            String bodyLine = ri.read(contentLength);
            parseRequestBody(bodyLine);
            ro.write(bodyLine);
        }
        ro.flush();
    }

    private void parseRequestMethod (String requestMethod) {
        String[] methodAndURL = requestMethod.split(METHOD_TOKEN);
        optionMap.put(KEY_METHOD, methodAndURL[0]);
        parseRequestURL(methodAndURL[1]);
    }

    private void parseRequestURL (String methodAndURL) {
        String[] pathAndQuery = methodAndURL.split(PATH_TOKEN);
        optionMap.put(KEY_PATH, pathAndQuery[0]);
        if(pathAndQuery.length == 2) {
            optionMap.put(KEY_QUERIES, pathAndQuery[1]);
        }
    }

    private void parseRequestQuery () {
        this.queryMap = new HashMap<>();

        String queries = optionMap.get(KEY_QUERIES);
        if(queries != null) {
            String[] queryList = queries.split(QUERY_TOKEN);
            for(String query : queryList) {
                parseRequestKeyValue(this.queryMap, query);
            }
        }
    }

    private void parseRequestKeyValue (Map<String,String> map, String query) {
        String[] keyAndValue = query.split(KEY_VALUE_TOKEN);
        if(keyAndValue.length == 2) {
            map.put(keyAndValue[0].trim(), keyAndValue[1].trim());
        }
    }

    private void parseRequestBody (String requestBody) {
        this.bodyMap = new HashMap<>();
        if(requestBody != null) {
            String[] bodyList = requestBody.split(QUERY_TOKEN);
            for(String query : bodyList) {
                parseRequestKeyValue(this.bodyMap, query);
            }
        }
    }

    private void parseRequestCookie() {
        this.cookieMap = new HashMap<>();
        String cookieOption = optionMap.get(KEY_COOKIE);
        if( cookieOption == null || "".equals(cookieOption) ) {
            return;
        }

        String[] cookies = cookieOption.split(COOKIE_TOKEN);
        for(String cookie: cookies) {
            parseRequestKeyValue(cookieMap, cookie);
        }
    }

    public String getQuery(String key) {
        return queryMap.getOrDefault(key, null);
    }

    public String getBody(String key) {
        return bodyMap.getOrDefault(key, null);
    }

    public String getMethod () {
        return optionMap.getOrDefault(KEY_METHOD, null);
    }

    public String getPath() {
        String path = optionMap.get(KEY_PATH);
        return ROOT_URL_PATH.equals(path) ? DEFAULT_URL_PATH : path;
    }

    public String getCookie(String key) {
        return cookieMap.get(key);
    }

}
