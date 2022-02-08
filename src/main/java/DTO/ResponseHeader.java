package DTO;

import java.util.HashMap;
import java.util.Map;

public class ResponseHeader {
    private String protocolVersion;

    private int statusCode;
    private String body;
    private final Map<String, String> headerList = new HashMap<>();

    public ResponseHeader(RequestHeader requestHeader){
        statusCode = 200;
        setContentType(requestHeader.getContentType());
    }

    public void setContentType(String type){
        headerList.put("Content-Type", type);
    }

    public void setRedirect(String url){
        headerList.put("Location",url);
        statusCode = 302;
    }

    public void setCookie(String name, String value, Map<String,String> options){
        String cookie = name + "=" + value;
        for ( String option : options.keySet() ){
            cookie = cookie + "; " + option + "=" + options.get(option);
        }
        headerList.put("Set-Cookie", cookie);
    }


    public int getStatusCode() {return statusCode;}

    public Map<String, String> getHeaderItems(){
        return headerList;
    }

    public void setContentLength(int length){
        headerList.put("Content-Length", Integer.toString(length));
    }


}
