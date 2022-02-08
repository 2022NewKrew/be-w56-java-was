package util;

import DTO.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static void writeResponseHeader(ResponseHeader responseHeader, DataOutputStream dos){
        try{
            Map<String, String> headerItems = responseHeader.getHeaderItems();

            dos.writeBytes( getFirstLine(responseHeader.getStatusCode()));

            for(String item : headerItems.keySet()){
                dos.writeBytes( getItemLine(item, headerItems.get(item)));
            }

            dos.writeBytes("Content-Type: text/html; charset=utf-8\r\n");

            dos.writeBytes("\r\n");

        } catch (IOException e) {
            log.error("Response Header write failed !");
        }
    }

    private static String getItemLine(String item, String value){
        String line = item + ": " + value+ "\r\n";
        log.debug(line);
        return line;
    }

    private static String getFirstLine(int statusCode){
        return "HTTP/1.1 " + Integer.toString(statusCode) + " " +HttpStatus.valueOf(statusCode) + " \r\n";
    }


    public static void writeResponseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error("Response Body write failed !");
        }
    }
}
