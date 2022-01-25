package webserver.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpRequestHeader {
    private String protocol;
    private String method;
    private String requestURI;
    private String mimeType;

    public HttpRequestHeader(String method, String requestURI, String protocol) {
        this.method = method;
        this.requestURI = requestURI;
        this.protocol = protocol;
        this.mimeType = getMimeTypeFromPath(requestURI);
    }

    private String getMimeTypeFromPath(String requestURI) {
        try {
            Path path = new File("./webapp" + requestURI).toPath();
            String fileName = path.getFileName().toString();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            Path tmpPath = new File("file" + fileExtension).toPath();
            String mimeType = Files.probeContentType(tmpPath);
            if (mimeType == null) {
                if (fileExtension.equals(".woff")) {
                    return "application/font-woff";
                } else {
                    return "text/html";
                }
            }
            return mimeType;
        } catch (IOException e) {
            return null;
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getMimeType() {
        return mimeType;
    }
}
