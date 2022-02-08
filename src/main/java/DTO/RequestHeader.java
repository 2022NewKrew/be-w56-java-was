package DTO;

import util.HttpRequestUtils.Pair;

import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.parseHeader;
import static util.HttpRequestUtils.parseQueryString;

public class RequestHeader {
    final static private String CONTENT_LENG = "Content-Length";
    final static private String GET = "GET";
    final static private String POST = "POST";

    private String protocolVersion;
    private String requestUrl;
    private String body;
    private boolean isGet;

    private final Map<String, String> headerList = new HashMap<>();

    public void addBufferLine(String line){
        Pair pair= parseHeader(line);
        if(pair == null){
            extractRequestInfo(line);
            return;
        }
        headerList.put(pair.getKey(), pair.getValue());

    }

    public void addBody(String body){
        this.body =  body;
    }

    public Map<String, String> getBody(){
        return parseQueryString(body);
    }


    public int getContentLength() {
        return Integer.parseInt(headerList.getOrDefault(CONTENT_LENG, "0"));
    }

    public String getContentType(){
        return headerList.get("Content-Type");
    }

    public String getRequestUrl(){
        return requestUrl;
    }

    public void extractRequestInfo(String line){
        String[] tokens = line.split(" ");
        setMethod(tokens[0]);
        setRequestUrl(tokens[1]);
        setProtocolVersion(tokens[2]);

    }

    private void setProtocolVersion(String token) {
        protocolVersion = token;
    }

    private void setRequestUrl(String url) {
        requestUrl = url;
    }

    private void setMethod(String method){
        if (method.equals(GET)){
            isGet = true;
            return;
        }
        isGet = false;
    }
    public void setCookie(String name, String value){
        String cookie = name+"="+value;
        headerList.put("Cookie",cookie);
    }


    public void removeCookie(String name, String value){
        String cookie = name + "=" + "removed" +"; Max-Age=0;";
        headerList.put("Cookie", cookie);
    }


}
