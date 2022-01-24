package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    public static final String METHOD = "Method";
    public static final String URL_PATH = "Url-Path";
    public static final String ACCEPT = "Accept";

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

            String[] methodAndPath = requestMethod.split(" ");
            map.put(METHOD, methodAndPath[0]);
            map.put(URL_PATH, methodAndPath[1]);

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

    private void writeBw (BufferedWriter bw, String str) throws IOException {
        bw.write("=== REQ :: ");
        bw.write(str);
        bw.write("\n");
    }
    private void flushBw (BufferedWriter bw) throws IOException {
        bw.write("==========================================\n");
        bw.flush();
    }

    String getMethod () throws IOException {
        return map.get(METHOD);
    }

    String getPath() throws IOException {
        String path = map.get(URL_PATH);
        return ROOT_URL_PATH.equals(path) ? DEFAULT_URL_PATH : path;

    }

    String getContentType () throws IOException {
        return map.get(ACCEPT).split(",")[0];
    }
}
