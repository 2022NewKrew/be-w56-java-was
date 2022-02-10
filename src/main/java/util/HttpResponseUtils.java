package util;

import DTO.RequestHeader;
import DTO.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static void writeResponseHeader(RequestHeader requestHeader, ResponseHeader responseHeader, DataOutputStream dos){
        try{
            Map<String, String> headerItems = responseHeader.getHeaderItems();

            dos.writeBytes( writeFirstLine(responseHeader.getStatusCode()));

            for(String item : headerItems.keySet()){
                dos.writeBytes( writeItemLine(item, headerItems.get(item)));
            }

            dos.writeBytes( writeContentType(requestHeader) );
            dos.writeBytes("\r\n");

        } catch (IOException e) {
            log.error("Response Header write failed !");
        }
    }

    private static String writeItemLine(String item, String value){
        String line = item + ": " + value+ "\r\n";
        log.debug(line);
        return line;
    }

    private static String writeFirstLine(int statusCode){
        return "HTTP/1.1 " + Integer.toString(statusCode) + " " +HttpStatus.valueOf(statusCode) + " \r\n";
    }

    // todo : 아래 함수 파싱해서 작성~~!
    private static String writeContentType(RequestHeader requestHeader){
        String htmlContentType = "Content-Type: text/html; charset=utf-8\r\n";
        String cssContentType = "Content-Type: text/css; charset=utf-8\r\n";
        return htmlContentType;
    }


    public static void writeResponseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error("Response Body write failed !");
        }
    }

    public static String mapFileFormatToType(String format){
        Map<String, String> typeMap = new HashMap<>(){{
            put(".html","text/html");
            put(".css","text/css");
            put(".js","text/javascript");
        }};
        return typeMap.get(format);

    }
}
