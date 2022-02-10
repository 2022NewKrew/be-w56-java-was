package webserver.http.response;

import webserver.http.response.templateEngine.TemplateEngine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String statusMessage;
    private DataOutputStream dos;
    private String Url;
    private byte[] body;
    private Map<String, String> header = new HashMap<>();
    private Model model = new Model();

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
        statusCode = 200;
        statusMessage = "Ok";
    }

    public void forward() {
        if(statusCode == 200)
            response200();
        else if(statusCode == 302)
            redirect();
        else if(statusCode == 404) {
            setUrl("/error.html");
            response200();
        }
    }

    private void response200() {
        try{
            setBody();
            responseHeader();
            responseBody();
        } catch (IOException e) {
            setStatusCode(404, "Not Found")
                    .forward();
            e.printStackTrace();
        }
    }

    private void redirect() {
        try{
            responseHeader();
            dos.writeBytes("Location: " + Url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
            setUrl("/error.html");
            response200();
        }
    }

    private void setBody() throws IOException {
        this.body = TemplateEngine.run(this);

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

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public HttpResponse setStatusCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.statusMessage = message;
        return this;
    }

    public String getUrl() {
        return Url;
    }

    public HttpResponse setUrl(String url) {
        Url = url;
        return this;
    }

    public byte[] getBody() {
        return body;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
