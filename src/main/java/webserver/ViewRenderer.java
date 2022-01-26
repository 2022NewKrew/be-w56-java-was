package webserver;

import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class ViewRenderer {

    private static final ViewRenderer INSTANCE = new ViewRenderer();

    private ViewRenderer() {
    }

    public static ViewRenderer getInstance() {
        return INSTANCE;
    }

    public void redirect(DataOutputStream dos, String urlString) throws IOException {

        response302Header(dos, urlString);
    }

    public void render(DataOutputStream dos, String pathString) throws IOException {
        Path path = new File("./webapp" + pathString).toPath();

        byte[] body = Files.readAllBytes(path);
        String mimeType = getMimeType(pathString);
        response200Header(dos, body.length, mimeType);
        responseBody(dos, body);
    }

    private String getMimeType(String pathString) throws IOException {
        String extension = pathString.substring(pathString.lastIndexOf("."));
        Path tmpPath = new File("file" + extension).toPath();
        String mimeType = Files.probeContentType(tmpPath);
        if (mimeType == null) {
            if (extension.equals(".woff")) {
                return "application/font-woff";
            }
            return "text/html";
        }
        return mimeType;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String mimeType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String urlString) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: http://" + urlString + "\r\n");
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
}
