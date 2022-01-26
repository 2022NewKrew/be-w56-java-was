package webserver.view;

import util.response.HttpResponse;
import util.response.HttpResponseDataType;
import util.response.HttpResponseStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class ViewRenderer {
    private static final String STATIC_FILE_BASE_DIRECTORY = "./webapp";

    public static <T> void render(HttpResponse<T> response, DataOutputStream dos) throws IOException {
        byte[] body = getBodyBytes(response);
        responseHeader(dos, body.length, response.getStatus(), response.getHeaders());
        responseBody(dos, body);
    }

    private static <T> byte[] getBodyBytes(HttpResponse<T> response) throws IOException {
        if(response.getDataType() == HttpResponseDataType.FILE_NAME){
            String filePath = String.format("%s%s", STATIC_FILE_BASE_DIRECTORY, response.getData());
            return Files.readAllBytes(new File(filePath).toPath());
        }

        if(response.getDataType() == HttpResponseDataType.STRING){
            return  ((String)response.getData()).getBytes();
        }

        return new byte[0];
    }

    private static <T> void responseHeader(DataOutputStream dos, int lengthOfBodyContent
            ,  HttpResponseStatus status, Map<String, String> headers) throws IOException {

        dos.writeBytes(String.format("HTTP/1.1 %s %s \r\n", status.getStatusCode(), status.getText()));
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
