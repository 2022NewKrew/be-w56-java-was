package webserver;

import model.Pair;
import model.request.Headers;
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

    private static final String RESPONSE_TOP_HEADER_OK = "HTTP/1.1 200 OK\r\n";
    private static final String RESPONSE_TOP_HEADER_SEE_OTHER = "HTTP/1.1 303 See Other\r\n";
    private static final String RESPONSE_BODY_SEPARATOR = "\r\n";

    public void writeFileResponse(final OutputStream out, final String filePath) throws IOException {
        final File file = new File(FILE_PREFIX + filePath);
        if (file.exists() && file.isFile()) {
            final Path path = file.toPath();
            final byte[] body = Files.readAllBytes(path);
            write200Response(out, body);
            return;
        }

        writeErrorResponse(out);
    }

    /**
     * @param headers Location 키 값을 가진 Pair가 없으면 Error 문자열 페이지를 출력한다
     */
    public void writeRedirectResponse(final OutputStream out, final Headers headers)
    {
        if (headers.getPair(Headers.HEADER_LOCATION).isNone()) {
            writeErrorResponse(out);
            return;
        }
        write303Response(out, headers);
    }

    public void writeErrorResponse(final OutputStream out) {
        byte[] body = "Error".getBytes(StandardCharsets.UTF_8);
        write200Response(out, body);
    }

    private void write200Response(final OutputStream out, final byte[] body) {
        final DataOutputStream dos = new DataOutputStream(out);

        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void write303Response(final OutputStream out, final Headers headers) {
        final DataOutputStream dos = new DataOutputStream(out);
        response303Header(dos, headers);
    }

    private String createHeaderString(final String key, final String value) {
        return key + Headers.HEADER_VALUE_SEPARATOR + value + Headers.HEADER_NEWLINE;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes(RESPONSE_TOP_HEADER_OK);
            dos.writeBytes(createHeaderString(Headers.HEADER_CONTENT_LENGTH, String.valueOf(lengthOfBodyContent)));
            dos.writeBytes(RESPONSE_BODY_SEPARATOR);
            dos.flush();
        } catch (IOException e) {
            log.error("response200Header: " + e.getMessage());
        }
    }

    private void response303Header(DataOutputStream dos, final Headers headers) {
        try {
            dos.writeBytes(RESPONSE_TOP_HEADER_SEE_OTHER);
            for (Pair pair : headers.getList()) {
                dos.writeBytes(createHeaderString(pair.getKey(), pair.getValue()));
            }
            dos.writeBytes(RESPONSE_BODY_SEPARATOR);
            dos.flush();
        } catch (IOException e) {
            log.error("response301Header: " + e.getMessage());
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
