package webserver;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResponseWriter {
    private static final Logger log = LoggerFactory.getLogger(ResponseWriter.class);

    private static final String FILE_PREFIX = "./webapp";

    private static final String RESPONSE_TOP_HEADER_OK = "HTTP/1.1 200 OK \r\n";
    private static final String RESPONSE_BODY_SEPARATOR = "\r\n";

    private static final String HEADER_VALUE_SEPARATOR = ": ";
    private static final String HEADER_NEWLINE = "\r\n";

    public void writeFileResponse(final OutputStream out, final String filePath) throws IOException {
        final File file = new File(FILE_PREFIX + filePath);
        if (file.exists() && file.isFile()) {
            final Path path = file.toPath();
            final Tika tika = new Tika();
            final byte[] body = Files.readAllBytes(path);
            writeResponse(out, body, tika.detect(path));

            return;
        }

        writeErrorResponse(out);
    }

    public void writeErrorResponse(final OutputStream out) {
        byte[] body = "Error".getBytes(StandardCharsets.UTF_8);
        writeResponse(out, body, "text/plain");
    }

    private void writeResponse(final OutputStream out, final byte[] body, final String mime) {
        final DataOutputStream dos = new DataOutputStream(out);

        response200Header(dos, body.length, mime);
        responseBody(dos, body);
    }

    private String createHeaderString(final String key, final String value) {
        return key + HEADER_VALUE_SEPARATOR + value + HEADER_NEWLINE;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, final String mime) {
        try {
            dos.writeBytes(RESPONSE_TOP_HEADER_OK);
            dos.writeBytes(createHeaderString("Content-Type", mime + ";charset=utf-8"));
            dos.writeBytes(createHeaderString("Content-Length", String.valueOf(lengthOfBodyContent)));
            dos.writeBytes(RESPONSE_BODY_SEPARATOR);
            dos.flush();
        } catch (IOException e) {
            log.error("response200Header: " + e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error("responseBody: " + e.getMessage());
        }
    }
}
