package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import db.DataBase;
import model.User;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            try {
                handleRequest(br, dos);
            } catch (IOException e) {
                printResponse(dos, HttpStatus.INTERNAL_SERVER_ERROR);
                log.error(e.getMessage());
            } catch (InvalidHttpRequestException e) {
                printResponse(dos, HttpStatus.BAD_REQUEST);
                log.error(e.getMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void printResponse(DataOutputStream dos, HttpStatus status, Map<String, String> responseHeader, byte[] body)
            throws IOException {

        dos.writeBytes("HTTP/1.1 " + status.getStatusCode() + " " + status.getStatusMsg() + "\r\n");
        if(responseHeader != null) {
            for (Map.Entry<String, String> entry : responseHeader.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
        }
        int bodyLength = body == null ? 0 : body.length;
        dos.writeBytes("Content-Length: " + bodyLength + "\r\n\r\n");
        if (bodyLength > 0) {
            dos.write(body, 0, bodyLength);
        }
        dos.flush();
    }

    // Body가 없고 추가 헤더 필드도 없는 경우의 응답입니다. (400, 404, 500 등)
    private static void printResponse(DataOutputStream dos, HttpStatus status) throws IOException {
        printResponse(dos, status, null, null);
    }

    private static void handleRequest(BufferedReader br, DataOutputStream dos) throws IOException, InvalidHttpRequestException {
        HttpRequestStartLine startLine = parseStartLine(br);
        Map<String, String> requestHeader = parseHeader(br);
        Map<String, String> responseHeader = new HashMap<>();

        switch (startLine.getMethod()) {
            case "GET":
                handleGetRequest(dos, startLine.getUrl(), requestHeader, responseHeader);
                break;

            case "POST":
                handlePostRequest(br, dos, startLine.getUrl(), requestHeader, responseHeader);
                break;

            default:
                throw new InvalidHttpRequestException("잘못된 HTTP method");
        }
    }

    private static HttpRequestStartLine parseStartLine(BufferedReader br) throws IOException, InvalidHttpRequestException {
        String line = br.readLine();
        log.debug(line);

        try {
            String[] tokens = line.split(" ");
            return new HttpRequestStartLine(tokens[0], tokens[1].equals("/") ? "/index.html" : tokens[1], tokens[2]);
        } catch (NullPointerException e) {
            throw new InvalidHttpRequestException("빈 HTTP 요청", e);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidHttpRequestException("잘못된 HTTP 요청 시작 줄", e);
        }
    }

    private static Map<String, String> parseHeader(BufferedReader br) throws IOException {
        Map<String, String> header = new HashMap<>();
        StringBuilder headerLog = new StringBuilder("\n");

        String line = br.readLine();
        while (!"".equals(line)) {
            headerLog.append(line).append('\n');
            HttpRequestUtils.Pair keyValues = HttpRequestUtils.parseHeader(line);
            if (keyValues != null) {
                header.put(keyValues.getKey(), keyValues.getValue());
            }
            line = br.readLine();
        }

        log.debug(headerLog.toString());
        return header;
    }

    private static void handleGetRequest(DataOutputStream dos, String url,
                                         Map<String, String> requestHeader, Map<String, String> responseHeader) throws IOException {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

            String extension = FilenameUtils.getExtension(url);
            switch (extension) {
                case "html":
                    responseHeader.put("Content-Type", "text/html; charset=UTF-8");

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    os.write(getHtmlHeader(requestHeader));
                    os.write(body);
                    body = os.toByteArray();
                    os.close();
                    break;

                case "css":
                    responseHeader.put("Content-Type", "text/css; charset=UTF-8");
                    break;

                default:
                    printResponse(dos, HttpStatus.NOT_FOUND);
                    return;
            }
            printResponse(dos, HttpStatus.OK, responseHeader, body);
        } catch (FileNotFoundException e) {
            printResponse(dos, HttpStatus.NOT_FOUND);
        }
    }

    private static void handlePostRequest(BufferedReader br, DataOutputStream dos, String url,
                                          Map<String, String> requestHeader, Map<String, String> responseHeader) throws IOException {
        try {
            int contentLength = Integer.parseInt(requestHeader.get("Content-Length"));
            switch (url) {
                case "/user":
                    handleUserPostRequest(br, dos, contentLength, responseHeader);
                    break;

                case "/login":
                    handleLoginPostRequest(br, dos, contentLength, responseHeader);
                    break;

                default:
                    printResponse(dos, HttpStatus.NOT_FOUND);
            }

        } catch (NoSuchElementException | NumberFormatException | InvalidPostBodyException e) {
            log.debug(e.getMessage());
            if (e instanceof NoSuchElementException) {
                log.debug("Content-Length 필드가 없음");
            }
            if (e instanceof NumberFormatException) {
                log.debug("Content-Length 필드가 잘못됨");
            }
            printResponse(dos, HttpStatus.BAD_REQUEST);
        } catch (DuplicateUserException e) {
            log.debug("유저 ID 중복");
            responseHeader.put("Location", "/user/form.html");
            printResponse(dos, HttpStatus.FOUND, responseHeader, null);
        } catch (Exception e) {
            log.debug(e.getMessage());
            printResponse(dos, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static void handleUserPostRequest(BufferedReader br, DataOutputStream dos, int contentLength, Map<String, String> responseHeader)
            throws IOException, InvalidPostBodyException, DuplicateUserException {
        String body = IOUtils.readData(br, contentLength);
        Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(body);

        if (!(parseQueryString.containsKey("userId") && parseQueryString.containsKey("password")
                && parseQueryString.containsKey("name") && parseQueryString.containsKey("email"))) {
            throw new InvalidPostBodyException("/user 잘못된 body 형식");
        }

        DataBase.getInstance().addUser(new User(parseQueryString.get("userId"),
                parseQueryString.get("password"),
                parseQueryString.get("name"),
                parseQueryString.get("email")));

        responseHeader.put("Location", "/index.html");
        printResponse(dos, HttpStatus.FOUND, responseHeader, null);
    }

    private static void handleLoginPostRequest(BufferedReader br, DataOutputStream dos, int contentLength, Map<String, String> responseHeader)
            throws IOException, InvalidPostBodyException {
        String body = IOUtils.readData(br, contentLength);
        Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(body);

        if (!(parseQueryString.containsKey("userId") && parseQueryString.containsKey("password"))) {
            throw new InvalidPostBodyException("/login 잘못된 body 형식");
        }

        User user = DataBase.getInstance().findUserById(parseQueryString.get("userId"));

        if (user == null || !user.getPassword().equals(parseQueryString.get("password"))) {
            responseHeader.put("Set-Cookie", "login=false");
            responseHeader.put("Location", "/user/login_failed.html");
            printResponse(dos, HttpStatus.FOUND, responseHeader, null);
            return;
        }

        responseHeader.put("Set-Cookie", "login=true");
        responseHeader.put("Location", "/index.html");
        printResponse(dos, HttpStatus.FOUND, responseHeader, null);
    }

    // 동적인 HTML을 생성하는 부분입니다.
    private static byte[] getHtmlHeader(Map<String, String> requestHeader) throws IOException {
        StringBuilder sb = new StringBuilder(Files.readString(new File("./webapp/header.html").toPath()));
        String candidate = "{login_button}";
        String target;
        String cookies = requestHeader.get("Cookie");
        if (cookies == null) {
            target = "<a href=\"/user/login.html\" role=\"button\">로그인</a>";
            return sb.replace(sb.indexOf(candidate), sb.indexOf(candidate) + candidate.length(), target)
                    .toString().getBytes(StandardCharsets.UTF_8);
        }

        Map<String, String> parsedCookie = HttpRequestUtils.parseCookies(cookies);
        String is_login = parsedCookie.get("login");
        target = (is_login == null || is_login.equals("false")) ?
                "<a href=\"/user/login.html\" role=\"button\">로그인</a>" :
                "<a href=\"/logout\" role=\"button\">로그아웃</a>";
        return sb.replace(sb.indexOf(candidate), sb.indexOf(candidate) + candidate.length(), target)
                .toString().getBytes(StandardCharsets.UTF_8);
    }
}
