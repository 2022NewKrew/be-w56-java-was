package util.view;

import util.response.FileType;
import util.response.HttpResponse;
import util.response.HttpStatus;
import util.response.ModelAndView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class ResponseSender {
    private static final String STATIC_FILE_BASE_DIRECTORY = "./webapp";

    public static void send(HttpResponse response, DataOutputStream dos)
            throws IOException, NoSuchFieldException, IllegalAccessException {

        byte[] body = getBodyBytes(response.getModelAndView());
        responseHeader(dos, body.length, response.getStatus(), response.getHeaders());
        responseBody(dos, body);
    }

    private static byte[] getBodyBytes(ModelAndView modelAndView) throws IOException, NoSuchFieldException, IllegalAccessException {
        if(modelAndView == null || modelAndView.getFileName() == null){
            return new byte[0];
        }

        if(modelAndView.getFileType() == FileType.STRING){
            return ViewRenderer.getRenderedView(modelAndView).getBytes(StandardCharsets.UTF_8);
        }

        String filePath = String.format("%s%s", STATIC_FILE_BASE_DIRECTORY, modelAndView.getFileName());
        return Files.readAllBytes(new File(filePath).toPath());
    }

    private static void responseHeader(DataOutputStream dos, int lengthOfBodyContent
            , HttpStatus status, Map<String, String> headers) throws IOException {

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
