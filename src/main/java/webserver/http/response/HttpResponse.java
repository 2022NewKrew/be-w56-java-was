package webserver.http.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String statusMessage;
    private DataOutputStream dos;
    private String Url;
    private byte[] body;
    private Map<String, String> header = new HashMap<>();
    private List<String> cookieValues = new ArrayList<>();

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
        statusCode = 200;
        statusMessage = "Ok";
    }


    public void forward() {
        try{
            handleBody();
            responseHeader();
            responseBody();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleBody() throws IOException {
        body = Files.readAllBytes(new File("./webapp" + Url).toPath());
        if(Url.endsWith(".css")) {
            header.put("Content-Type", "text/css");
        } else if(Url.endsWith(".js")) {
            header.put("Content-Type", "application/javascript");
        } else {
            header.put("Content-Type", "text/html;charset=utf-8");
        }
        header.put("Content-Length", body.length + "");
    }

    private void responseHeader() {
        try {
            dos.writeBytes(String.format("HTTP/1.1 %s %s \r\n", statusCode, statusMessage));
            header.keySet().stream().forEach(k -> {
                try {
                    dos.writeBytes(k + ": " + header.get(k) + "\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseBody() {
        try {
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirect() {
        try{
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            responseHeader();
            dos.writeBytes("Location: " + Url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCookie(String value) {
        cookieValues.add(value);
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.statusMessage = message;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public byte[] getBody() {
        return body;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
}
