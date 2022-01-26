package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.HttpResponse;

public class ViewResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ViewResolver.class);
    private static final String NEW_LINE = System.lineSeparator();

    private final DataOutputStream dos;

    public ViewResolver(DataOutputStream dos) {
        this.dos = dos;
    }

    public void render(HttpResponse httpResponse) {
        respondStatusLine(httpResponse.getResponseStatusLine());
        respondHeader(httpResponse.getResponseHeader());
        respondBody(httpResponse.getResponseBody(), 0);
    }

    private void respondStatusLine(String responseStatusLine) {
        dosWrite(responseStatusLine + NEW_LINE);
    }

    private void respondHeader(Map<String, String> responseHeader) {
        responseHeader.forEach((key, value) -> dosWrite(key + ": " + value + NEW_LINE));
        dosWrite(NEW_LINE);
    }

    private void respondBody(byte[] responseBody, Integer offset) {
        dosWrite(responseBody, offset, responseBody.length);
        dosFlush();
    }

    private void dosWrite(String msg) {
        try {
            dos.writeBytes(msg);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private void dosWrite(byte[] msg, Integer offset, Integer length) {
        try {
            dos.write(msg, offset, length);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private void dosFlush() {
        try {
            dos.flush();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
