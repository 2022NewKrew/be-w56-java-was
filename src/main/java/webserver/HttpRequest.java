package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String url;
    private final String protocol;
    private Map<String, String> header;
    private Map<String, String> data;

    public HttpRequest(String[] firstLineSplit){
        this.method = firstLineSplit[0];
        this.url = firstLineSplit[1];
        this.protocol = firstLineSplit[2];

        header = new HashMap<>();
        data = new HashMap<>();
        data.put("request_url", url);
    }

    public void reBuildHttpRequest(BufferedReader br) throws IOException{
        bringHeader(br);

        if(!"GET".equals(method)){
            int len = Integer.parseInt(header.get("Content-Length"));
            data.putAll(getRequestData(br, len));
        }
    }

    private void bringHeader(BufferedReader br) throws IOException {
        Map<String, String> headerMap = new HashMap<>();

        String line = br.readLine();
        while(line != null && !"".equals(line)){
            headerMap.put(line.split(":")[0].trim(), line.split(":")[1].trim());
            line = br.readLine();
        }
        System.out.println(headerMap);
        header = headerMap;
    }

    private Map<String, String> getRequestData(BufferedReader br, int len) throws IOException{
        Map<String, String> message = new HashMap<>();
        char[] a = new char[len];

        br.read(a, 0, a.length);
        for(String data: new String(a).split("&")){
            message.put(data.split("=")[0], data.split("=")[1]);
        }

        return message;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getData() {
        return data;
    }
}
