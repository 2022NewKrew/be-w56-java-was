package webserver;

import model.HttpRequest;
import model.Pair;
import model.ValueMap;
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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Void> {
    private static final String FILE_PREFIX = "./webapp";

    private static final String TOP_HEADER_SEPARATOR = " ";
    private static final String LOCATION_AND_PARAMETER_SEPARATOR = "\\?";
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
        final String[] topHeaderValues = Separator.splitString(readOneHeader(in), TOP_HEADER_SEPARATOR);
        final HttpMethod method = HttpMethod.valueOf(topHeaderValues[0]);

        final String[] locAndParams = Separator.splitString(topHeaderValues[1], LOCATION_AND_PARAMETER_SEPARATOR);
        final HttpLocation location = new HttpLocation(locAndParams[0]);
        final ValueMap params = getParams(locAndParams);

        final Headers headers = getHeaders(in);

        return new HttpRequest(method, location, params, headers);
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

    private ValueMap getParams(final String[] locAndParams) {
        if (locAndParams.length != 2) {
            return new ValueMap(Collections.emptyMap());
        }

        final String decodedParams = URLDecoder.decode(locAndParams[1], StandardCharsets.UTF_8);
        final Map<String, String> map = HttpRequestUtils.parseQueryString(decodedParams);
        return new ValueMap(map);
    }

    private Headers getHeaders(final InputStream in) throws IOException {
        final List<Pair> list = new ArrayList<>();
        while (true) {
            final String header = readOneHeader(in);
            if (header.isEmpty()) {
                break;
            }
            list.add(HttpRequestUtils.parseHeader(header));
        }
        return new Headers(list);
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
        writeResponse(out, body, "text/plain");
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
