package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private static final String METHOD = "Method";
    private static final String PATH = "Url-Path";
    private static final String QUERIES = "Queries";
    private static final String ACCEPT = "Accept";

    private static final String ROOT_URL_PATH = "/";
    private static final String DEFAULT_URL_PATH = "/index.html";

    Map<String, String> map;
    public RequestParser(InputStream is) {
        this.map = new HashMap<>();
        generateRequestMap(is);
    }

    private boolean checkStringIsEmpty(String str) {
        return str == null || "".equals(str);
    }

    void generateRequestMap (InputStream is) {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            String requestMethod = br.readLine(); // 첫 줄
            if(checkStringIsEmpty(requestMethod)) {
                return;
            }
            writeBw(bw, requestMethod);
            parseRequestMethod(requestMethod);

            String requestLine = null;
            while(!checkStringIsEmpty(requestLine=br.readLine())) {
                writeBw(bw, requestLine);

                String[] input = requestLine.split(":");
                if(input.length == 2) {
                    map.put(input[0].trim(), input[1].trim());
                }
            }
            flushBw(bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRequestMethod (String requestMethod) {
        String[] methodAndURL = requestMethod.split(" ");
        map.put(METHOD, methodAndURL[0]);
        parseRequestURL(methodAndURL[1]);
    }

    private void parseRequestURL (String methodAndURL) {
        String[] pathAndQuery = methodAndURL.split("\\?");
        map.put(PATH, pathAndQuery[0]);
        if(pathAndQuery.length == 2) {
            map.put(QUERIES, pathAndQuery[1]);
        }
    }

    public Map<String,String> parseRequestQuery () {
        Map<String,String> queryMap = new HashMap<>();

        String queries = map.get(QUERIES);
        if(queries != null) {
            String[] queryList = queries.split("&");
            for(String query : queryList) {
                parseRequestKeyValue(queryMap, query);
            }
        }
        return queryMap;
    }

    private void parseRequestKeyValue (Map<String,String> queryMap, String query) {
        String[] keyAndValue = query.split("=");
        if(keyAndValue.length == 2) {
            queryMap.put(keyAndValue[0], keyAndValue[1]);
        }
    }

    private void writeBw (BufferedWriter bw, String str) throws IOException {
        bw.write("=== REQ :: ");
        bw.write(str);
        bw.write("\n");
    }

    private void flushBw (BufferedWriter bw) throws IOException {
        bw.write("==========================================\n");
        bw.flush();
    }

    public String getMethod () {
        return map.get(METHOD);
    }

    public String getPath() {
        String path = map.get(PATH);
        return ROOT_URL_PATH.equals(path) ? DEFAULT_URL_PATH : path;

    }

    public String getContentType () throws IOException {
        return map.get(ACCEPT).split(",")[0];
    }
}
