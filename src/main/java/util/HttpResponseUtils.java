package util;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.Router;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseBody;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

// 임시로 처리, Response 양식에 맞춰서 다시 만들어줘야함
public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static Response createResponse(OutputStream out, Request request) throws ParseException, IOException {
        if (request == null) return null;
        String routingResult = Router.roting(request);
        ResponseBody responseBody = null;
        DataOutputStream dos = new DataOutputStream(out);
        if(routingResult.equals("static")){
            responseBody = new ResponseBody(Files.readAllBytes(new File("./webapp" + request.getRequestLine().getRequestUri().getUrl()).toPath()));

            response200Header(dos, responseBody.getBody().length);
            responseBody(dos, responseBody.getBody());
        } else {
            response200Header(dos, 0);
        }

//        return Response.of();
        return null;
    }

    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
