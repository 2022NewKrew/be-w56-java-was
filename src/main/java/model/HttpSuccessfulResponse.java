package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;

public class HttpSuccessfulResponse extends HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpSuccessfulResponse.class);

    public static HttpSuccessfulResponse of(HttpStatus httpStatus, String url) throws IOException {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1.getVersion(), httpStatus.getCode(),
                httpStatus.getMessage());
        byte[] body = View.get(url);
        Map<String, String> headerKeyMap = Map.of(
                "Content-Type", getContentType(url),
                "Content-Length", Integer.toString(body.length)
        );
        return new HttpSuccessfulResponse(statusLine, new Header(headerKeyMap), body);
    }

    private static String getContentType(String path) {
        List<String> splitResult = List.of(path.split("\\."));
        int length = splitResult.size();
        String extension = splitResult.get(length - 1);

        return Mime.getMime(extension);
    }

    private final byte[] body;

    public HttpSuccessfulResponse(StatusLine statusLine, Header header, byte[] body) {
        super(statusLine, header);
        this.body = body;
    }

    private void responseBody(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void sendResponse(Socket connection) {
        try (OutputStream out = connection.getOutputStream();) {
            DataOutputStream dos = new DataOutputStream(out);

            responseHeader(dos);
            responseBody(dos);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
