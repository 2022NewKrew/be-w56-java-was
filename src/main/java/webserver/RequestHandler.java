//package webserver;
//
//import java.io.*;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//import lombok.extern.slf4j.Slf4j;
//import webserver.model.KinaHttpRequest;
//
//@Slf4j
//public class RequestHandler extends Thread {
//    private Socket connection;
//
//    public RequestHandler(Socket connectionSocket) {
//        this.connection = connectionSocket;
//    }
//
//    public void run() {
//        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
//                connection.getPort());
//
//        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
//            BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
//            String request = buffer.readLine();
//            log.info(request);
//            KinaHttpRequest kinaHttpRequest = HttpRequestHeaderUtils.parseRequestHeader(request);
//            DataOutputStream dos = new DataOutputStream(out);
//            Path path = new File("./webapp" + kinaHttpRequest.getRequestURI()).toPath();
//            log.info(String.format("%-100s", "[" + kinaHttpRequest.getMethod() + "] " + kinaHttpRequest.getRequestURI()) + "Mime Type: " + kinaHttpRequest.getMimeType());
//            byte[] body = Files.readAllBytes(path);
//            response200Header(dos, body.length, kinaHttpRequest.getMimeType());
//            responseBody(dos, body);
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String mimeType) {
//        try {
//            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
//            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    private void responseBody(DataOutputStream dos, byte[] body) {
//        try {
//            dos.write(body, 0, body.length);
//            dos.flush();
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//}
