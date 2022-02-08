package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import db.DataBase;
import model.RequestData;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpHeaderUtils;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String requestLine = br.readLine();
            log.info("Request line = {}", requestLine);
            RequestData request = HttpHeaderUtils.parseRequestLine(requestLine);
            log.info("request = {}", request);
            String method = request.getMethod();
            String urlPath = request.getUrlPath();
            String urlQuery = request.getUrlQuery();
            String httpVersion = request.getHttpVersion();

            Map<String, String> headers = getHeaders(br);
            String requestBody = "";
            if(headers.containsKey("Content-Length")) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                requestBody = IOUtils.readData(br, contentLength);
                log.info("requestBody = {}", requestBody);
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] response = execByRequestUrl(method, urlPath, urlQuery, headers, requestBody);
            dos.write(response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] execByRequestUrl(String method, String requestUrlPath, String requestUrlQuery, Map<String, String> headers, String requestBody) throws IOException {
        switch(requestUrlPath) {
            case "/user/create": return postUserCreate(requestBody);
            case "/user/login": return postUserLogin(requestUrlPath, requestBody);
            default: return staticPageResponse(requestUrlPath);
        }
    }

    private byte[] postUserCreate(String requestBody) {
        Optional<User> user = HttpHeaderUtils.parseUserInfo(requestBody);
        user.ifPresent(DataBase::addUser);
        log.info("user = {}", user);
        Map<String, String> headers = Map.of("Content-Type", "text/html;charset=utf-8",
                "Content-Length", "0",
                "Location","/index.html"
        );
        return getResponseHeader(302, headers);
    }

    private byte[] postUserLogin(String requestUrlPath, String requestBody) {
        final String contentType = HttpHeaderUtils.getContentTypeFromUrl(requestUrlPath);
        Map<String, String> requestBodyMap = HttpRequestUtils.parseQueryString(requestBody);
        Map<String, String> responseHeaders = new HashMap<>() {{
            put("Content-Type", contentType + ";charset=utf-8");
            put("Content-Length", "0");
        }};
        User user = DataBase.findUserById(requestBodyMap.get("userId"));
        if(user == null || !user.getPassword().equals(requestBodyMap.get("password"))) {
            responseHeaders.put("Set-Cookie", "logined=false; Path=/");
            responseHeaders.put("Location", "/user/login_failed.html");
            return getResponseHeader(302, responseHeaders);
        }
        responseHeaders.put("Set-Cookie", "logined=true; Path=/");
        responseHeaders.put("Location", "/index.html");
        return getResponseHeader(302, responseHeaders);
    }

    private byte[] staticPageResponse(String requestUrlPath) throws IOException {
        String contentType = HttpHeaderUtils.getContentTypeFromUrl(requestUrlPath);
        if(new File("./webapp" + requestUrlPath).exists()) {
            byte[] responseBody = Files.readAllBytes(new File("./webapp" + requestUrlPath).toPath());
            Map<String, String> headers = Map.of(
                    "Content-Type",contentType + ";charset=utf-8",
                    "Content-Length", Integer.toString(responseBody.length)
            );
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(getResponseHeader(200, headers));
            baos.write(responseBody);
            return baos.toByteArray();
        }
        log.warn("Undefined requestUrlPath = {} (redirect to /index.html)", requestUrlPath);
        Map<String, String> headers = Map.of("Content-Type", "text/html;charset=utf-8",
                "Content-Length", "0",
                "Location","/index.html"
        );
        return getResponseHeader(302, headers);
    }

    private Map<String, String> getHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line = br.readLine();
        while(line != null && !"".equals(line)) {
            log.info("Http header = {}", line);
            String[] token = line.split(": ");
            headers.put(token[0], token[1]);
            line = br.readLine();
        }
        return headers;
    }

    private byte[] getResponseHeader(int httpCode, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();
        switch(httpCode) {
            case(200): sb.append("HTTP/1.1 200 OK \n");
            case(302): sb.append("HTTP/1.1 302 Found \n");
        }
        sb.append(headers.entrySet().stream().map(x -> x.getKey() + ": " + x.getValue() + "\r\n").collect(Collectors.joining()));
        sb.append("\r\n");
        return sb.toString().getBytes();
    }
}
