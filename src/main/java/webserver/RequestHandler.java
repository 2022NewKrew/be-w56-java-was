package webserver;

import model.HttpRequest;
import model.request.Headers;
import model.request.HttpLocation;
import model.request.HttpMethod;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.Separator;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Void> {
    private static final String FILE_PREFIX = "./webapp";

    private static final String HEADER_SEPARATOR = ": ";
    private static final String HEADER_NEWLINE = "\r\n";

    private static final String RESPONSE_TOP_HEADER_OK = "HTTP/1.1 200 OK \r\n";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public Void call() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            final HttpRequest httpRequest = readRequest(in);

            final HttpMethod method = httpRequest.getMethod();
            final String location = httpRequest.getHttpLocation().getLocation();

            log.debug("Port : {}, method: {}, location: {}",
                    connection.getPort(),
                    method,
                    location);

            if (method == HttpMethod.GET) {
                writeFileResponse(out, location);
            }
            else {
                writeErrorResponse(out);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // signal that thread is gracefully ended
        return null;
    }

    private HttpRequest readRequest(final InputStream in) throws IOException {
        final String[] topHeaderValues = Separator.splitString(readOneHeader(in), " ");
        final HttpMethod method = HttpMethod.valueOf(topHeaderValues[0]);
        final HttpLocation location = new HttpLocation(topHeaderValues[1]);

        final List<HttpRequestUtils.Pair> list = new ArrayList<>();
        String header = readOneHeader(in);
        while (!header.isEmpty()) {
            list.add(HttpRequestUtils.parseHeader(header));
            header = readOneHeader(in);
        }
        final Headers headers = new Headers(list);

        return new HttpRequest(method, location, headers);
    }

    private String readOneHeader(final InputStream in) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int data;
        while (true) {
            data = in.read();
            if (data == '\n') {
                break;
            }
            else if (data == '\r') {
                continue;
            }
            sb.append((char)data);
        }

        return sb.toString();
    }

    private void writeFileResponse(final OutputStream out, final String filePath) throws IOException {
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

    private void writeErrorResponse(final OutputStream out) {
        byte[] body = "Error".getBytes(StandardCharsets.UTF_8);
        writeResponse(out, body, "text/html");
    }

    private void writeResponse(final OutputStream out, final byte[] body, final String mime) {
        final DataOutputStream dos = new DataOutputStream(out);

        response200Header(dos, body.length, mime);
        responseBody(dos, body);
    }

    private String getHeaderString(final String key, final String value) {
        return key + HEADER_SEPARATOR + value + HEADER_NEWLINE;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, final String mime) {
        try {
            dos.writeBytes(RESPONSE_TOP_HEADER_OK);
            dos.writeBytes(getHeaderString("Content-Type", mime + ";charset=utf-8"));
            dos.writeBytes(getHeaderString("Content-Length", String.valueOf(lengthOfBodyContent)));
            dos.writeBytes(HEADER_NEWLINE);
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
