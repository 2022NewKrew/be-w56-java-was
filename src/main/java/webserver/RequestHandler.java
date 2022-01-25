package webserver;

import Controller.Controller;
import mapper.UrlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final UrlMapper urlMapper = new UrlMapper();

    public void sendResponseMessage(Socket socket){
        log.debug("New Client Connect! Connected IP : {}, Port : {}", socket.getInetAddress(),
                socket.getPort());

        try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            BufferedReader br = new BufferedReader( new InputStreamReader(in,"UTF-8") );

            String firstLine = br.readLine();
            if(firstLine == null)
                return;

            String[] firstLineSplit = firstLine.split(" ");
            String method = firstLineSplit[0];
            String url = firstLineSplit[1];
            String protocol = firstLineSplit[2];
            String respondType = "";

            Map<String, String> headerMap = getHeader(br);

            if(headerMap.containsKey("Accept"))
                respondType = headerMap.get("Accept").split(",")[0];

            if("/".equals(url))
                url = "/index.html";

            log.info("{} {} {}", method, url, protocol);
            log.info("{}", headerMap);

            Map<String, String> message = new HashMap<>();

            if(!"GET".equals(method)){
                int len = Integer.parseInt(headerMap.get("Content-Length"));
                message = getRequestData(br, len);
            }

            message.put("request_url", url);
            Map<String, Object> result = urlMapper.mappingResult(method, url, message);
            String filename = (String)result.get("name");

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = Files.readAllBytes(new File("./webapp" + filename).toPath());

            response200Header(dos, body.length, respondType);
            responseBody(dos, body);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, String> getHeader(BufferedReader br) throws IOException{
        Map<String, String> headerMap = new HashMap<>();

        String line = br.readLine();
        while(line != null && !"".equals(line)){
            headerMap.put(line.split(":")[0].trim(), line.split(":")[1].trim());
            line = br.readLine();
        }

        return headerMap;
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

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            //dos.writeBytes("Content-Type: " + type + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
