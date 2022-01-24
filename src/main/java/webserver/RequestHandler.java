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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public void sendResponseMessage(Socket socket){
        log.debug("New Client Connect! Connected IP : {}, Port : {}", socket.getInetAddress(),
                socket.getPort());

        try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            BufferedReader br = new BufferedReader( new InputStreamReader(in,"UTF-8") );

            String[] firstLine = br.readLine().split(" ");

            String method = firstLine[0];
            String url = firstLine[1];
            String protocol = firstLine[2];
            String respondType = "";

            Map<String, String> headerMap = getHeader(br);

            if(headerMap.containsKey("Accept"))
                respondType = headerMap.get("Accept").split(",")[0];

            if("/".equals(url))
                url = "/index.html";

            if(!"GET".equals(method)){
                log.info("{}", br.readLine());
            }

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

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

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type + ";charset=utf-8\r\n");
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
