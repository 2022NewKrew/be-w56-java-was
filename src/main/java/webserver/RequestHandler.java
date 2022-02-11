package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import model.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Function;
import service.WebService;
import util.Header;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private static Login login = new Login(false);

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            HashMap<String, String> parseRequest = WebService.parseRequest(br);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = loadHTMLBody(dos, parseRequest);
            addHeader(dos, parseRequest);
            responseBody(dos, body);
        } catch (Exception e) {
            e.printStackTrace();
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

    private byte[] loadHTMLBody(DataOutputStream dos, HashMap<String, String> parameters) {
        String functionName = WebService.extractFunction(parameters.get("URL"));
        Function function = Function.loadFunction(functionName);
        byte[] body = function.callFunction(parameters, login);
        parameters.put("Content-Length", String.valueOf(body.length));
        log.debug("loadHTMLBody results, function : {}, redirectURL : {}, login {}", function, parameters.get("redirectURL"), login.getLogin());
        return body;
    }

    private void addHeader(DataOutputStream dos, HashMap<String, String> parameters) {
        if (parameters.get("method").equals("GET")) {
            Header header = Header.HEADER200;
            header.addParameter("Content-Type", "text/" + parameters.get("type") + ";charset=utf-8");
            header.addParameter("Content-Length", parameters.get("Content-Length"));
            header.setCookie(login.getLogin());
            header.generateStream(dos);
        } else {
            Header header = Header.HEADER302;
            header.addParameter("Location", parameters.get("redirectURL"));
            header.setCookie(login.getLogin());
            header.generateStream(dos);
        }
    }

}
