package webserver.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import webserver.util.Constant;
import webserver.util.ModelAndView;

@Slf4j
public class HttpResponse {
    private DataOutputStream dos;
    private String cookie;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void send(String path, String contentType) throws IOException {
        byte[] body = Files.readAllBytes(new File(Constant.ROOT_PATH + path).toPath());

        response200Header(contentType, body.length);
        responseBody(body);
    }

    public void send(ModelAndView modelAndView) throws IOException {
        String view = modelAndView.getView();
        Map<String, String> model = modelAndView.getModel();

        if (StringUtils.startsWith(view, "redirect:")) {
            response302Header(StringUtils.replace(view, "redirect:", ""), cookie);
        } else {
            sendDynamicHtml(view, model);
        }
    }

    private void sendDynamicHtml(String path, Map<String, String> model) throws IOException {
        File file = new File(Constant.ROOT_PATH + path);
        String html = new String(Files.readAllBytes(file.toPath()));

        if (ObjectUtils.isNotEmpty(model)) {
            for (String key : model.keySet()) {
                html = html.replaceAll(key, model.get(key));
            }
        }

        byte[] body = html.getBytes(StandardCharsets.UTF_8);
        response200Header(Constant.TEXT_HTML, body.length);
        responseBody(body);
    }

    private void response200Header(String contentType, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + contentType + ";\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void response302Header(String path, String cookie) throws IOException {
        dos.writeBytes("HTTP/1.1 302 OK \r\n");
        dos.writeBytes("Location: " + path + " \r\n");
        if (StringUtils.isNotEmpty(cookie)) {
            dos.writeBytes("Set-Cookie: " + cookie + "; Path=/ \r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
