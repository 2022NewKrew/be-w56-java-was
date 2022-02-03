package infrastructure.view;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import http.common.Status;
import infrastructure.dto.AppResponse;
import http.response.HttpResponse;

public class View {

    public static final String FILE_DIRECTORY = "./webapp";

    public static HttpResponse render(AppResponse appResponse) {
        String absolutePath = String.format("%s%s", FILE_DIRECTORY, appResponse.getPath());
        byte[] body = readTemplate(absolutePath, appResponse.getModel());
        byte[] header = makeHeader(appResponse, body.length);
        return HttpResponse.of(header, body);
    }

    private static byte[] makeHeader(AppResponse appResponse, int length) {
        if (appResponse.getStatus() == Status.OK && length == 0) {
            return "HTTP/1.1 404 Not Found\r\n\r\n".getBytes();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ");
        sb.append(appResponse.getStatus().getCodeAndMessage());
        sb.append(" \r\n");
        sb.append(appResponse.getHeaderString());

        if (length > 0) {
            writeOneHeaderLine(sb, "Content-Length", String.valueOf(length));
        }
        if (appResponse.getStatus() == Status.FOUND) {
            writeOneHeaderLine(sb, "Location", appResponse.getPath());
        }

        // Content-Type 필요
        // dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        sb.append("\r\n");

        return sb.toString().getBytes();
    }

    private static void writeOneHeaderLine(StringBuilder sb, String type, String value) {
        sb.append(type);
        sb.append(": ");
        sb.append(value);
        sb.append("\r\n");
    }

    private static byte[] readTemplate(String absolutePath, Map<String, String> model) {
        try {
            if (model.isEmpty()){
                return Files.readAllBytes(Path.of(absolutePath));
            }
            String template = Files.readString(Path.of(absolutePath));
            for (Entry<String, String> e : model.entrySet()) {
                template = template.replaceAll(String.format("\\{\\{%s}}", e.getKey()), e.getValue());
            }
            return template.getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
