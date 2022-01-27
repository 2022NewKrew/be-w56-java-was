package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ResponseWriter {

    private static final Logger log = LoggerFactory.getLogger(ResponseWriter.class);
    private static final String HTTP_VERSION = "HTTP/1.1";

    private ResponseWriter() {
    }

    public static void write(OutputStream out, Response response) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            writeResponseLine(dos, response);
            writeHeaderLine(dos, response);
            writeBody(dos, response);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void writeResponseLine(DataOutputStream dos, Response response)
        throws IOException {
        dos.writeBytes(
            HTTP_VERSION + " " + response.getStatusCode().getStatus() + " "
                + response.getStatusCode().getDescription()
                + "\r\n");
        log.debug("http response line: {}",
            HTTP_VERSION + " " + response.getStatusCode().getStatus() + " "
                + response.getStatusCode().getDescription());
    }

    private static void writeHeaderLine(DataOutputStream dos, Response response)
        throws IOException {
        for (String key : response.getHeaders().keySet()) {
            dos.writeBytes(key + ": " + response.getHeaders().get(key) + "\r\n");
            log.debug("http header line: {}", key + ": " + response.getHeaders().get(key));
        }
        dos.writeBytes("\r\n");
    }

    private static void writeBody(DataOutputStream dos, Response response) throws IOException {
        if (response.getBody() != null && response.getBody().length > 0) {
            dos.write(response.getBody(), 0, response.getBody().length);
        }
    }
}
