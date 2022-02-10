package DTO;

import util.HttpRequestUtils;
import util.HttpResponseUtils;

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

    public void setContentType(String format){
        String type = HttpResponseUtils.mapFileFormatToType(format);
        headerList.put("Content-Type", type);
    }

    public void setRedirect(String url){
        headerList.put("Location",url);
        statusCode = 302;
    }

    public void setCookie(String name, String value, Map<String,String> options){
        StringBuilder cookie = new StringBuilder(name + "=" + value);
        for ( String option : options.keySet() ){
            cookie.append("; ").append(option).append("=").append(options.get(option));
        }
        headerList.put("Set-Cookie", cookie.toString());
    }

    public void removeCookie(String name, String value){
        String cookie = name + "=" + value + "; Path=/; Max-age=0";
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
