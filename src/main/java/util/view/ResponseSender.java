package util.view;

import util.response.HttpResponse;
import util.response.HttpResponseStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ResponseSender {

    public static <T> void send(HttpResponse<T> response, DataOutputStream dos)
            throws IOException, NoSuchFieldException, IllegalAccessException {

        byte[] body = ViewRenderer.getRenderedView(response.getModelAndView()).getBytes(StandardCharsets.UTF_8);
        responseHeader(dos, body.length, response.getStatus(), response.getHeaders());
        responseBody(dos, body);
    }

    private static void responseHeader(DataOutputStream dos, int lengthOfBodyContent
            , HttpResponseStatus status, Map<String, String> headers) throws IOException {

        dos.writeBytes(String.format("HTTP/1.1 %s %s \r\n", status.getStatusCode(), status.getText()));
        dos.writeBytes("Content-Type: */*;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");

        for (Map.Entry<String, String> entry : headers.entrySet()){
            String headerLine = String.format("%s: %s\r\n", entry.getKey(), entry.getValue());
            dos.writeBytes(headerLine);
        }

        dos.writeBytes("\r\n");
    }

    private static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
