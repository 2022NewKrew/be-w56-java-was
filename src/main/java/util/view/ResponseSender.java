package util.view;

import util.response.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ResponseSender {
    private static final String STATIC_FILE_BASE_DIRECTORY = "./webapp";

    public static void send(HttpResponse response, DataOutputStream dos)
            throws IOException, NoSuchFieldException, IllegalAccessException {

        byte[] body = getBodyBytes(response.getStatus(), response.getModelAndView(), response.getHeaders().getContentType());
        responseHeader(dos, response.getStatus(), response.getHeaders().with(body.length));
        responseBody(dos, body);
    }

    private static byte[] getBodyBytes(HttpStatus status, ModelAndView modelAndView, ContentType contentType) throws IOException, NoSuchFieldException, IllegalAccessException {
        if(modelAndView == null || modelAndView.getFileName() == null){
            return new byte[0];
        }

        if(status == HttpStatus.REDIRECT){
            return new byte[0];
        }

        if(contentType.isText()){
            return ViewRenderer.getRenderedView(modelAndView).getBytes(StandardCharsets.UTF_8);
        }

        String filePath = String.format("%s%s", STATIC_FILE_BASE_DIRECTORY, modelAndView.getFileName());
        return Files.readAllBytes(new File(filePath).toPath());
    }

    private static void responseHeader(DataOutputStream dos, HttpStatus status, ResponseHeaders responseHeaders)
            throws IOException {
        dos.writeBytes(String.format("HTTP/1.1 %s %s \r\n", status.getStatusCode(), status.getText()));
        dos.writeBytes(responseHeaders.toString());
        dos.writeBytes("\r\n");
    }

    private static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
