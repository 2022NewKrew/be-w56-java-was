package webserver;

import model.HttpRequest;
import model.Pair;
import model.ValueMap;
import model.request.Headers;
import model.request.HttpLocation;
import model.request.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.Separator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Void> {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String TOP_HEADER_SEPARATOR = " ";
    private static final String LOCATION_AND_PARAMETER_SEPARATOR = "\\?";

    private final Socket connection;
    private final ResponseWriter responseWriter;

    public RequestHandler(final Socket connectionSocket, final ResponseWriter responseWriter) {
        this.connection = connectionSocket;
        this.responseWriter = responseWriter;
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
                responseWriter.writeFileResponse(out, location);
            }
            else {
                responseWriter.writeErrorResponse(out);
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
}
