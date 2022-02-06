package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.HttpResponse;
import response.ResHeader;
import response.ResponseHeader;

public class ViewResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ViewResolver.class);
    private static final String NEW_LINE = System.lineSeparator();
    private static final Integer BLANK_OFFSET = 0;

    private final DataOutputStream dos;

    public ViewResolver(DataOutputStream dos) {
        this.dos = dos;
    }

    public void render(HttpResponse httpResponse) {
        respondStatusLine(httpResponse.getResponseStatusLine());
        respondHeader(httpResponse.getResponseHeader());
        respondBody(httpResponse.getResponseBody());
    }

    private void respondStatusLine(String responseStatusLine) {
        dosWrite(responseStatusLine + NEW_LINE);
    }

    private void respondHeader(ResponseHeader responseHeader) {
        Map<ResHeader, String> headerMap = responseHeader.getHeaderMap();
        headerMap.forEach((key, value) ->
                dosWrite(String.format("%s: %s%s", key.getText(), value, NEW_LINE)));
        dosWrite(NEW_LINE);
    }

    private void respondBody(byte[] responseBody) {
        dosWrite(responseBody);
        dosFlush();
    }

    private void dosWrite(String msg) {
        try {
            dos.writeBytes(msg);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private void dosWrite(byte[] msg) {
        try {
            dos.write(msg, BLANK_OFFSET, msg.length);
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
