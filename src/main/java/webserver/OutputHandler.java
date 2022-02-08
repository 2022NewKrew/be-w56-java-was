package webserver;

import http.body.ResponseBody;
import http.header.Headers;
import http.message.ResponseMessage;
import http.startline.HttpStatus;
import http.startline.StatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputHandler {
    private OutputHandler() {
    }

    public static void sendResponseMessage(OutputStream out, ResponseMessage response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        writeResponse(dos, response);
    }

    private static void writeResponse(DataOutputStream dos, ResponseMessage response) {
        writeStatusLine(dos, response.getStatusLine());
        writeHeaders(dos, response.getHeaders());
        write(dos, "\r\n");
        writeBody(dos, response.getBody());
    }

    private static void writeStatusLine(DataOutputStream dos, StatusLine statusLine) {
        HttpStatus status = statusLine.getStatus();
        write(dos, String.format("%s %s %s\r\n", statusLine.getVersion().getValue(), status.getStatusCode(), status.getReasonPhase()));
    }

    private static void writeHeaders(DataOutputStream dos, Headers headers) {
        headers.getHeaders()
                .forEach((key, value) -> write(dos, String.format("%s: %s\r\n", key.getName(), value.getValue())));

    }

    private static void write(DataOutputStream dos, String message) {
        try {
            dos.writeBytes(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBody(DataOutputStream dos, ResponseBody responseBody) {
        byte[] bytes = responseBody.getBytes();
        try {
            dos.write(bytes, 0, bytes.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
