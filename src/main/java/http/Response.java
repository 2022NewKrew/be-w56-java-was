package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

import static util.HttpCookieUtils.ATTR_DELIMITER;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private String path;
    private DataOutputStream dos;
    private Request request;
    private byte[] body;
    private boolean isRedirection;
    private boolean isError;

    public Response(Request request, String nextPath, OutputStream out) throws IOException {
        this.request = request;
        this.path = nextPath;
        this.dos = new DataOutputStream(out);
        this.isRedirection = false;
        this.isError = false;

        //redirect
        if(path.startsWith("redirect:")){
            path = path.substring("redirect:".length(), path.length());
            isRedirection = true;
        }

        try {
            this.body = Files.readAllBytes(new File("./webapp" + path).toPath());
        } catch (IOException e){
            log.error(String.format("path %s doesn't exist.", path));
            isError = true;
        }

        setHeader();
        setBody();
    }

    private void setHeader() {
        if(isRedirection){
            response302Header(dos, path);
        }

        response200Header(dos, body.length);
    }


    private void setBody() throws IOException {
        try {
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void send() throws IOException {
        dos.flush();
    }

    private void addNewCookies(DataOutputStream dos) throws IOException {
        String sessionId = request.getCookieValue("sessionId");
        List<String> updateCookieList = CookieManager.getNewCookie(sessionId); //sessionId에 해당하는 갱신해줘야 할 쿠키들을 가져온다.
        if(updateCookieList.size() > 0){
            dos.writeBytes("Set-Cookie: ");

            for(String keyValueStr : updateCookieList) {
                dos.writeBytes(keyValueStr + ATTR_DELIMITER + " ");
            }

            dos.writeBytes("Path=/");
        }
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            addNewCookies(dos);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String path) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + path + "\r\n");
            addNewCookies(dos);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
