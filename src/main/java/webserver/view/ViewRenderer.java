package webserver.view;

import util.response.HttpResponse;
import util.response.HttpResponseDataType;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewRenderer {
    private static final String STATIC_FILE_BASE_DIRECTORY = "./webapp";

    public static <T> void render(HttpResponse<T> response, DataOutputStream dos) throws IOException {
        byte[] body = getBodyBytes(response);
        response200Header(dos, body.length);
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

    static void response200Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
