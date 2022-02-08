package DTO;

import java.util.HashMap;
import java.util.Map;

public class ResponseHeader {
    private String protocolVersion;

    private int statusCode;
    private String statusMsg;
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


    public int getStatusCode() {return statusCode;}

    public Map<String, String> getHeaderItems(){
        return headerList;
    }

    public void setContentLength(int length){
        headerList.put("Content-Length", Integer.toString(length));
    }


}
