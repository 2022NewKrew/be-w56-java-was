package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Objects;

import controller.RequestController;
import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            RequestHeader requestHeader = new RequestHeader();
            HttpRequestUtils.setRequest(requestHeader, bufferedReader.readLine());
            System.out.println("---------------" + requestHeader.getHeader("uri"));

            bufferedReader.lines()
                    .takeWhile(header -> header.contains(": "))
                    .peek(System.out::println)
                    .forEach(header -> HttpRequestUtils.setHeader(requestHeader, header));
            if(requestHeader.getHeader("method").equals("POST")){
                String parameters = IOUtils.readData(bufferedReader,
                        Integer.parseInt(requestHeader.getHeader("Content-Length")));
                HttpRequestUtils.setRequestParameter(requestHeader, parameters);
            }

            DataOutputStream dos = new DataOutputStream(out);
            ResponseHeader responseHeader = RequestController.controlRequest(requestHeader);
            byte[] body = Files.readAllBytes(new File(Constants.RETURN_BASE + responseHeader.getUri()).toPath());
            outResponseHeader(dos, body.length, responseHeader);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void outResponseHeader(DataOutputStream dos, int lengthOfBodyContnet, ResponseHeader responseHeader){
        if(responseHeader.getStatusCode() == 200){
            response200Header(dos, lengthOfBodyContnet, responseHeader);
        }
        if(responseHeader.getStatusCode() == 302){
            response302Header(dos, responseHeader);
        }
        if(responseHeader.getStatusCode() == 310){
            response302LoginSuccess(dos,responseHeader);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, ResponseHeader responseHeader) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+ responseHeader.getAccept() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, ResponseHeader responseHeader){
        try{
            log.info("302: " + responseHeader.getUri());
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + responseHeader.getUri() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302LoginSuccess(DataOutputStream dos, ResponseHeader responseHeader){
        try{
            log.info("Login 302: " + responseHeader.getUri());
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: " + responseHeader.getUri() + "\r\n");
            dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
            dos.writeBytes("\r\n");
        }catch(IOException e){
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
