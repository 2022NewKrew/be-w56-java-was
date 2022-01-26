package webserver;

import Controller.Controller;
import mapper.AssignedModelKey;
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
import java.nio.charset.StandardCharsets;
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
            BufferedReader br = new BufferedReader( new InputStreamReader(in, StandardCharsets.UTF_8) );

            String firstLine = br.readLine();
            if(firstLine == null)
                return;

            String[] firstLineSplit = firstLine.split(" ");
            Map<String, String> header = makeHeaderMap(br);

            Map<String, String> body = new HashMap<>();
            if(!"GET".equals(firstLineSplit[0])){
                int len = Integer.parseInt(header.get("Content-Length"));
                body = makeBodyMap(br, len);
            }

            HttpRequest httpRequest = new HttpRequest(firstLineSplit, header, body);

            Map<String, Object> result = urlMapper.mappingResult(httpRequest);
            String filename = (String)result.get(AssignedModelKey.NAME);

            String cookieSet = "";
            if(result.containsKey(AssignedModelKey.LOGIN)){
                if((Boolean)result.get(AssignedModelKey.LOGIN))
                    cookieSet = "Set-Cookie: logined=true; Path=/";
                else
                    cookieSet = "Set-Cookie: logined=false; Path=/";
            }

            DataOutputStream dos = new DataOutputStream(out);

            if(filename.matches("redirect:.*")){
                response302Header(dos, filename.substring(9), cookieSet);
                return;
            }

            byte[] Responsebody = Files.readAllBytes(new File("./webapp" + filename).toPath());


            response200Header(dos, Responsebody.length, cookieSet);
            responseBody(dos, Responsebody);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            if(!"".equals(cookie))
                dos.writeBytes(cookie + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            if(!"".equals(cookie))
                dos.writeBytes(cookie + "\r\n");
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

    private Map<String, String> makeHeaderMap(BufferedReader br) throws IOException {
        Map<String, String> headerMap = new HashMap<>();

        String line = br.readLine();
        while(line != null && !"".equals(line)){
            headerMap.put(line.split(":")[0].trim(), line.split(":")[1].trim());
            line = br.readLine();
        }

        return headerMap;
    }

    private Map<String, String> makeBodyMap(BufferedReader br, int len) throws IOException{
        Map<String, String> body = new HashMap<>();
        char[] a = new char[len];

        br.read(a, 0, a.length);
        for(String query: new String(a).split("&")){
            body.put(query.split("=")[0], query.split("=")[1]);
        }

        return body;
    }
}
