package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public enum Header {
    HEADER200("HTTP/1.1 200 OK \r\n"){
        @Override
        public void exampleHeader(String parameter, Boolean logined) {
            addParameter("Content-Type", "text/html;charset=utf-8");
            addParameter("Content-Length", parameter);
            addParameter("Set-Cookie", "logined="+ logined.toString() +"; Path=/");
        }
    },
    HEADER302("HTTP/1.1 302 Found \r\n"){
        @Override
        public void exampleHeader(String redirectURL, Boolean logined){
            addParameter("Location", redirectURL);
            addParameter("Set-Cookie", "logined="+ logined.toString() +"; Path=/");
        }
    };

    private static final Logger log = LoggerFactory.getLogger(Header.class);
    private String headerContent;
    private HashMap<String, String> parameters = new HashMap<>();

    Header(String headerContent) {
        this.headerContent = headerContent;
    }

    public void addParameter(String key, String value){
        this.parameters.put(key, value);
    }

    public void generateStream(DataOutputStream dos){
        try{
            dos.writeBytes(headerContent);
            for (String key : parameters.keySet()){
                dos.writeBytes(key+": "+parameters.get(key)+"\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void exampleHeader(String parameter, Boolean logined);

    public void setCookie(Boolean logined){
        addParameter("Set-Cookie", "logined="+ logined.toString() +"; Path=/");
    }


}
