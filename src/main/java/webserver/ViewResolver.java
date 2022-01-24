package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class ViewResolver {
    private static final String newLine = "\r\n";
    private static final String DEFAULT_PREFIX = "./webapp";

    private static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    public void render(OutputStream out, String viewPath) {
        render(out, viewPath, HttpStatus.OK);
    }

    public void render(OutputStream out, String viewPath, HttpStatus httpStatus) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            byte[] body = getBytesOfFile(viewPath);
            writeResponseHeader(dos, httpStatus);
            writeResponseBody(dos, body);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            renderNotFound(out);
        }
    }

    private byte[] getBytesOfFile(String viewPath) throws IOException {
        return Files.readAllBytes(new File(DEFAULT_PREFIX + viewPath).toPath());
    }

    private void writeResponseHeader(DataOutputStream dos, HttpStatus httpStatus) throws IOException {
        dos.writeBytes(httpStatus.getHttpResponseHeader());
        dos.writeBytes(newLine);
    }

    private void writeResponseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
    }

    private void renderNotFound(OutputStream out) {
        render(out, "/error/404.html", HttpStatus.NOT_FOUND);
    }

}
