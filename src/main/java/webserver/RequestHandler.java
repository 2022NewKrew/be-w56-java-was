package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import db.DataBase;
import exception.InvalidHttpMethodException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import javax.xml.crypto.Data;


public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final DataBase dataBase;

    public RequestHandler(Socket connectionSocket, DataBase dataBase) {
        this.connection = connectionSocket;
        this.dataBase = dataBase;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            String line = br.readLine();
            //log.debug("request line : {}", line);
            Map<String, String> requestLine = HttpRequestUtils.parseRequestLine(line);
            Map<String, String> header = HttpRequestUtils.parseHeader(br);
            RequestMapper(requestLine, header, dos, br);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    private void RequestMapper(Map<String, String> requestLine, Map<String, String> header, DataOutputStream dos, BufferedReader br) throws InvalidHttpMethodException, IOException {
        String url = requestLine.get("url");
        switch (requestLine.get("method")) {
            case "GET":
                if (url.contains("?")) {
                    int index = url.indexOf("?");
                    String requestPath = url.substring(0, index);
                    String queryString = url.substring(index + 1);
                    Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
                    urlMapper(dos, requestPath, header, params);
                    break;
                }
                urlMapper(dos, url, header, br);
                break;
            case "POST":
                urlMapper(dos, url, header, br);
                break;
            default:
                throw new InvalidHttpMethodException("유효하지 않은 HTTP 메소드입니다");
        }
    }

    private void urlMapper(DataOutputStream dos, String url, Map<String, String> header, BufferedReader br) throws IOException {
        if (url.contains(".")) {
            getFile(dos, url, header);
        } else if (url.startsWith("/create")) {
            createUserByPost(dos, url, header, br);
        } else if (url.equals("/login")) {
            login(dos, url, header, br);
        } else if (url.equals("/logout")) {
            logout(dos, url, header, br);
        } else if (url.equals("/user/list")) {
            if (header.get("Cookie").contains("logined=true")) {
                showUserList(dos, "/user/list.html", header, br);
            } else {
                redirect(dos, "/user/login.html", header, "");
            }
        }
    }

    private void urlMapper(DataOutputStream dos, String url, Map<String, String> header, Map<String, String> params) throws IOException {
        if (url.startsWith("/create")) {
            createUserByGet(dos, url, header, params);
        }

    }

    private void getFile(DataOutputStream dos, String url, Map<String, String> header) throws IOException {
        view(dos, url, header);
    }

    private void createUserByGet(DataOutputStream dos, String url, Map<String, String> header, Map<String, String> params) throws IOException {
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        System.out.println(user);
        DataBase.addUser(user);
        dataBase.save(user);
        view(dos, "/index.html", header);
    }

    private void createUserByPost(DataOutputStream dos, String url, Map<String, String> header, BufferedReader br) throws IOException {
        String body = IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")));
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        System.out.println(user);
        DataBase.addUser(user);
        dataBase.save(user);
        redirect(dos, "/index.html", header, "");
    }

    private void login(DataOutputStream dos, String url, Map<String, String> header, BufferedReader br) throws IOException {
        String body = IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")));
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);
        User user = DataBase.findUserById(params.get("userId"));
        if (user == null) {
            throw new IOException("존재하지 않는 아이디입니다");
        } else if (!user.getPassword().equals(params.get("password"))) {
            throw new IOException("유저 아이디와 비밀번호가 일치하지 않습니다");
        }
        redirect(dos, "/index.html", header, "logined=true");
    }


    private void logout(DataOutputStream dos, String url, Map<String, String> header, BufferedReader br) throws IOException {
        redirect(dos, "/index.html", header, "logined=false");
    }

    private void showUserList(DataOutputStream dos, String url, Map<String, String> header, BufferedReader br) throws IOException {
        Collection<User> userList = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        int id = 0;
        for (User user : userList) {
            sb.append("<th scope=\"row\">")
                    .append(id)
                    .append("</th><td>")
                    .append(user.getUserId())
                    .append("</td> <td>")
                    .append(user.getName())
                    .append("</td> <td>")
                    .append(user.getEmail())
                    .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
            id++;
        }
        byte[] body = new String(Files.readAllBytes(new File("./webapp" + url).toPath()))
                .replace("{{userList}}", URLDecoder.decode(sb.toString(), StandardCharsets.UTF_8))
                .getBytes();
        response200Header(dos, body.length, "html");
        responseBody(dos, body);
    }

    private void view(DataOutputStream dos, String url, Map<String, String> header) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        String[] tokens = url.split("\\.");
        String fileType = tokens[tokens.length-1];
        if(fileType == null){
            throw new IOException();
        }
        response200Header(dos, body.length, fileType);
        responseBody(dos, body);
    }

    private void redirect(DataOutputStream dos, String url, Map<String, String> header, String cookie) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response302Header(dos, body.length, url, cookie);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String fileType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/"+ fileType +";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void response302Header(DataOutputStream dos, int lengthOfBodyContent, String url, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + " \r\n");
            if (!cookie.equals("")) {
                dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            }
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

    private void show(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

}
