package DTO;

import java.util.HashMap;
import java.util.Map;

public class ResponseHeader {
    private String protocolVersion;
    private String location="/";
    private int statusCode;
    private String statusMsg;
    private String body;
    private final Map<String, String> headerList = new HashMap<>();

    public ResponseHeader(){
    }

    public void setRedirect(String url){
        location = url;
        statusCode = 302;
    }


}
